package raistudio.testandpractice;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.Category;
import Model.CategoryAdapter;
import Model.Ranking;
import Model.Record;
import Model.RecordAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RankingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment implements RecordAdapter.OnRecyclerItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Ranking ranking;
    private List<Record> records;

    private TextView difficultyView, scoreView, amountView, rankView;
    private RecyclerView recyclerView;

    private List<Category> categories;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference recordDatabaseReference;
    private ChildEventListener recordChildEventListener;

    private RecordAdapter recordAdapter;

    public RankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ranking Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(Ranking ranking, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, ranking);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.ranking=getArguments().getParcelable(ARG_PARAM1);

        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        difficultyView=view.findViewById(R.id.ranking_difficulty_view);
        scoreView=view.findViewById(R.id.ranking_average_view);
        amountView=view.findViewById(R.id.ranking_amount_taken_view);
        rankView=view.findViewById(R.id.ranking_rank_view);

        if (this.ranking.getDifficulty()==0) {
            difficultyView.setText(getResources().getString(R.id.easy));
        } else if (this.ranking.getDifficulty()==1) {
            difficultyView.setText(getResources().getString(R.id.medium));
        } else if (this.ranking.getDifficulty()==2) {
            difficultyView.setText(getResources().getString(R.id.hard));
        }
        scoreView.setText(String.valueOf(this.ranking.getAverageCorrect()*100).substring(0,3).concat("%"));
        amountView.setText(String.valueOf(this.ranking.getAmountTaken()));
        rankView.setText(String.valueOf(this.ranking.getReputation()));

        records=new ArrayList<>();

        recyclerView = view.findViewById(R.id.ranking_record_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm= new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recordAdapter=new RecordAdapter(records,getContext(),this);
        recyclerView.setAdapter(recordAdapter);

        firebaseDatabase=FirebaseDatabase.getInstance();

        recordDatabaseReference=firebaseDatabase.getReference().child("records");
        Query recordQuery = recordDatabaseReference
                .orderByChild("correctQuestions");
        recordChildEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Record record = dataSnapshot.getValue(Record.class);
                if (ranking.getRecords().get(record.getId())!=null) {
                    records.add(0,record);
                    recordAdapter.setRecords(records);
                    recyclerView.setAdapter(recordAdapter);
                }
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
        recordQuery.addChildEventListener(recordChildEventListener);

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
    public void onRecordItemClicked(View v, int position) {

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
