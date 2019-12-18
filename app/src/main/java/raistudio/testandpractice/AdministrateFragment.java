package raistudio.testandpractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Category;
import Model.CategoryAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdministrateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdministrateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdministrateFragment extends Fragment implements CategoryAdapter.OnRecyclerItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String connectedId;
    private View previousClickedView;
    private Category selectedCategory;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference adminDatabaseReference;
    private DatabaseReference categoryDatabaseReference;
    private ValueEventListener adminEventListener;
    private ChildEventListener categoryEventListener;

    private List<Category> categories;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    private ImageButton selectButton;

    public AdministrateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdministrateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdministrateFragment newInstance(String param1, String param2) {
        AdministrateFragment fragment = new AdministrateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_administrate, container, false);

        connectedId=null;

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        connectedId=sharedPreferences.getString("connected_id","");

        firebaseDatabase=FirebaseDatabase.getInstance();
        adminDatabaseReference=firebaseDatabase.getReference().child("admins");
        categoryDatabaseReference=firebaseDatabase.getReference().child("categories");

        adminEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("datasnapshot",dataSnapshot.getValue().toString());
                HashMap<String, Boolean> admins=(HashMap<String, Boolean>) dataSnapshot.getValue();
                //Log.d("adminsgetconnected",admins.get(connectedId).toString());
                if (admins.get(connectedId)==null){
                    UnauthorizedFragment unauthorizedFragment=new UnauthorizedFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, unauthorizedFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        adminDatabaseReference.addValueEventListener(adminEventListener);

            previousClickedView=null;
            selectedCategory=null;

            categories=new ArrayList<>();

            recyclerView = view.findViewById(R.id.administrate_recycler_view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            categoryAdapter=new CategoryAdapter(categories,getContext(),this);
            recyclerView.setAdapter(categoryAdapter);

            categoryEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Category category=dataSnapshot.getValue(Category.class);
                    categories.add(category);
                    categoryAdapter.setCategories(categories);
                    recyclerView.setAdapter(categoryAdapter);
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
            categoryDatabaseReference.addChildEventListener(categoryEventListener);

            selectButton=view.findViewById(R.id.administrate_select_button);
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdministrateCategoryFragment administrateCategoryFragment;
                    if (selectedCategory!=null){
                        administrateCategoryFragment = AdministrateCategoryFragment.newInstance(selectedCategory.getId(),"");
                    } else {
                        administrateCategoryFragment = AdministrateCategoryFragment.newInstance("","");
                    }
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, administrateCategoryFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCategoryItemClicked(View v, int position) {
        if (previousClickedView!=null){
            previousClickedView.setBackgroundColor(Color.TRANSPARENT);
        }

        selectedCategory=categories.get(position);
        v.setBackgroundColor(Color.argb(100,50,50,50));

        previousClickedView=v;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
