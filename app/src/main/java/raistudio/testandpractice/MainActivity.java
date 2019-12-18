package raistudio.testandpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, TestCategoryFragment.OnFragmentInteractionListener,
        QuestionFragment.OnFragmentInteractionListener, TestResultsFragment.OnFragmentInteractionListener,
        EditProfileFragment.OnFragmentInteractionListener, RankingCategoryFragment.OnFragmentInteractionListener,
        RankingFragment.OnFragmentInteractionListener, AdministrateFragment.OnFragmentInteractionListener,
        UnauthorizedFragment.OnFragmentInteractionListener, AdministrateCategoryFragment.OnFragmentInteractionListener {
    Fragment genericFragment;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genericFragment=null;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                genericFragment=new ProfileFragment();
                if (genericFragment!=null){
                    fragmentTransaction.replace(R.id.main_container,genericFragment);
                    fragmentTransaction.commit();
                }
                drawer.closeDrawers();
            }
        });
        TextView usernameView=(TextView)headerView.findViewById(R.id.text_view);
        CircleImageView imageView =(CircleImageView) headerView.findViewById(R.id.image_view);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String connectedUsername=sharedPreferences.getString("connected_username","");
        String connectedImage=sharedPreferences.getString("connected_image","");
        if (!connectedUsername.isEmpty()) {
            usernameView.setText(connectedUsername);
        }
        if (!connectedImage.isEmpty()){
            Picasso.get().load(connectedImage)
                    .placeholder(R.drawable.profile_image).error(R.drawable.default_profile)
                    .into(imageView);
        }

        genericFragment=null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        genericFragment=new HomeFragment();
        if (genericFragment!=null){
            fragmentTransaction.replace(R.id.main_container,genericFragment);
            fragmentTransaction.commit();
        }

        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                if (firebaseUser==null){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        genericFragment=null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
            // Handle the camera action
            genericFragment=new HomeFragment();

        } else if (id == R.id.nav_test) {
            genericFragment=TestCategoryFragment.newInstance(new HashMap<String, Boolean>(),"");

        } else if (id == R.id.nav_ranking) {
            genericFragment=RankingCategoryFragment.newInstance(new HashMap<String, Boolean>(),"");

        } else if (id == R.id.nav_administrate) {
            genericFragment=new AdministrateFragment();

        } else if (id == R.id.nav_sign_out) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("connected_username", "");
            editor.putString("connected_email", "");
            editor.putString("connected_image", "");
            editor.putString("connected_password", "");
            editor.commit();

            firebaseAuth.signOut();
        }

        if (genericFragment!=null){
            fragmentTransaction.replace(R.id.main_container,genericFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                if (firebaseUser==null){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        firebaseAuth.removeAuthStateListener(authStateListener);
    }

}
