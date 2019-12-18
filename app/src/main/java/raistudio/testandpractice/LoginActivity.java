package raistudio.testandpractice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.User;

public class LoginActivity extends AppCompatActivity {
    private AdView mAdView;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDatabaseReference;
    ChildEventListener usersChildEventListener;
    FirebaseStorage firebaseStorage;
    StorageReference usersStorageReference;

    EditText emailText, passwordText;
    List<User> users= new ArrayList<>();

    private static final int RC_GOOGLE_SIGN_IN = 1;
    GoogleApiClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        firebaseAuth=FirebaseAuth.getInstance();

        emailText=findViewById(R.id.email_login);
        passwordText=findViewById(R.id.password_login);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                if (firebaseUser!=null){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getBaseContext(),"Connection failed.",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        findViewById(R.id.google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoogleSignInClick();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDatabaseReference = firebaseDatabase.getReference("users");
        usersChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Not sure if here or onChildChanged
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersDatabaseReference.addChildEventListener(usersChildEventListener);

        firebaseStorage = FirebaseStorage.getInstance();
        usersStorageReference=firebaseStorage.getInstance().getReference().child("users");

        findViewById(R.id.login_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterClick(view);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void onLoginClick(View view){
        boolean validEmail=true, validPassword=true;
        if (TextUtils.isEmpty(emailText.getText())){
            validEmail=false;
            emailText.setError(getString(R.string.email_error));
        }
        if (TextUtils.isEmpty(passwordText.getText())){
            validPassword=false;
            emailText.setError(getString(R.string.password_error));
        }
        if (validEmail && validPassword) {
            firebaseAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                        for (User user:users){
                            if (user.getEmail().equals(emailText.getText().toString())&&user.getPassword().equals(passwordText.getText().toString())){
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("connected_id",user.getId());
                                editor.putInt("connected_tests_taken",user.getTestsTaken());
                                editor.putString("connected_username", user.getUsername());
                                editor.putString("connected_email", user.getEmail());
                                editor.putString("connected_image", user.getImage());
                                editor.putString("connected_password", user.getPassword());
                                editor.commit();
                                break;
                            }
                        }

                        updateUI(firebaseUser);
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onRegisterClick(View view){
        Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onGoogleSignInClick(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void updateUI(FirebaseUser firebaseUser) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                handleGoogleSignInResult(account);
            }
        }
    }

    private void handleGoogleSignInResult(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("pooooooool","pooooooool1");
                            User loggedUser=new User();
                            Log.d("pooooooool1","pooooooool1");
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d("pooooooool1","pooooooool1");
                            boolean exists=false;
                            Log.d("pooooooool1","pooooooool1");
                            for (User user1:users){
                                if (user1.getEmail().equals(user.getEmail())){
                                    exists=true;
                                    loggedUser=user1;
                                }
                            }
                            Log.d("1pooooooool","1pooooooool");
                            if (!exists) {
                                final String userId = usersDatabaseReference.push().getKey();
                                Log.d("expooooooool","expooooooool");
                                StorageReference imageRef=firebaseStorage.getReference().child("users").child(userId);
                                Log.d("expoooooooonextl","expoooooooonextl");
                                Log.d("expoooocasitoo",user.getPhotoUrl().getLastPathSegment());
                                Log.d("pooooimportante",firebaseStorage.getReference().toString()+"    "+firebaseStorage.getReference().getPath());
                                Log.d("expoooocasi",imageRef.getPath());
                                Log.d("expoooocasiya",user.getPhotoUrl().toString());
                                /*imageRef.putFile(Uri.parse(user.getPhotoUrl().toString())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        @SuppressWarnings("VisibleForTests") Uri downloadUri=taskSnapshot.getDownloadUrl();*/
                                        Log.d("expoooooooolast","expoooooooolast");
                                        User currentUser = new User(userId, user.getDisplayName(), user.getEmail(), "", "",0,new HashMap<String, Boolean>());
                                        usersDatabaseReference.child(userId).setValue(currentUser);

                                        Log.d("not_exists","not exists");
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("connected_tests_taken",currentUser.getTestsTaken());
                                        editor.putString("connected_username", currentUser.getUsername());
                                        editor.putString("connected_email", currentUser.getEmail());
                                        editor.putString("connected_image", currentUser.getImage());
                                        editor.putString("connected_password", currentUser.getPassword());
                                        editor.commit();

                                        updateUI(user);
                                    /*}
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(),"Image upload failed.",Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                            }
                            else {
                                Log.d("exists","yes exists");
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("connected_tests_taken",loggedUser.getTestsTaken());
                                editor.putString("connected_username", user.getDisplayName());
                                editor.putString("connected_email", user.getEmail());
                                editor.putString("connected_image", loggedUser.getImage());
                                editor.putString("connected_password", loggedUser.getPassword());
                                editor.commit();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseAuth.addAuthStateListener(authStateListener);

        users=new ArrayList<>();
        usersChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersDatabaseReference.addChildEventListener(usersChildEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
        usersDatabaseReference.removeEventListener(usersChildEventListener);
    }
}
