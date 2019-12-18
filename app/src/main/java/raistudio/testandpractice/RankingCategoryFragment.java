package raistudio.testandpractice;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Category;
import Model.CategoryAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RankingCategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RankingCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingCategoryFragment extends Fragment implements CategoryAdapter.OnRecyclerItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private HashMap mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<Category> categories;
    private Category selectedCategory;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference categoryDatabaseReference;
    private ChildEventListener categoryChildEventListener;

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private ImageButton backButton, selectButton, continueButton;
    private View previousClickedView;

    public RankingCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingCategoryFragment newInstance(HashMap param1, String param2) {
        RankingCategoryFragment fragment = new RankingCategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (HashMap<String, Boolean>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking_category, container, false);

        categories=new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.ranking_category_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        categoryAdapter=new CategoryAdapter(categories,getContext(),this);
        recyclerView.setAdapter(categoryAdapter);

        backButton=view.findViewById(R.id.ranking_back_button);
        selectButton=view.findViewById(R.id.ranking_select_button);
        continueButton=view.findViewById(R.id.ranking_continue_button);

        previousClickedView=null;

        backButton.setEnabled(false);
        selectButton.setEnabled(false);
        continueButton.setEnabled(false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RankingCategoryFragment rankingCategoryFragment=new RankingCategoryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, rankingCategoryFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCategory!=null) {
                    RankingFragment rankingFragment = RankingFragment.newInstance(selectedCategory.getRanking(), "");
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container, rankingFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCategory!=null){
                    Log.d("continueButton",((HashMap<String, Boolean>) selectedCategory.getSubcategories()).toString());
                    RankingCategoryFragment rankingCategoryFragment = RankingCategoryFragment.newInstance((HashMap<String, Boolean>) selectedCategory.getSubcategories(),"");
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container,rankingCategoryFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        firebaseDatabase=FirebaseDatabase.getInstance();

        categoryDatabaseReference=firebaseDatabase.getReference().child("categories");
        categoryChildEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Category category = dataSnapshot.getValue(Category.class);
                Log.d("mParam1Category",mParam1.toString());
                if (mParam1.isEmpty()) {
                    if (category.isRoot()) {
                        categories.add(category);
                    }
                } else {
                    if (mParam1.get(category.getId())!=null) {
                        categories.add(category);
                    }
                }
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
        //categoryDatabaseReference.addChildEventListener(categoryChildEventListener);

        selectedCategory=null;

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
    public void onResume() {
        super.onResume();

        categories=new ArrayList<>();
        categoryChildEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Category category = dataSnapshot.getValue(Category.class);
                Log.d("mParam1Category",mParam1.toString());
                if (mParam1.isEmpty()) {
                    if (category.isRoot()) {
                        categories.add(category);
                    }
                } else {
                    if (mParam1.get(category.getId())!=null) {
                        categories.add(category);
                    }
                }                categoryAdapter.setCategories(categories);
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
        categoryDatabaseReference.addChildEventListener(categoryChildEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        categoryDatabaseReference.removeEventListener(categoryChildEventListener);
    }

    @Override
    public void onCategoryItemClicked(View v, int position) {
        //Test test= new Test(categories.get(position).getQuestions(),"First Test",0,categories.get(position).getId());

        if (previousClickedView!=null){
            previousClickedView.setBackgroundColor(Color.TRANSPARENT);
        }

        selectedCategory=categories.get(position);
        v.setBackgroundColor(Color.argb(100,50,50,50));
        selectButton.setEnabled(true);
        if (!selectedCategory.getSubcategories().isEmpty()) {
            continueButton.setEnabled(true);
        }
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
