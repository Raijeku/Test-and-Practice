package raistudio.testandpractice;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.Ranking;
import Model.Record;
import Model.RecordAdapter;
import Model.Test;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TestResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestResultsFragment extends Fragment implements RecordAdapter.OnRecyclerItemClickListener, RewardedVideoAdListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Ranking currentRanking;
    private Test test;
    private int amountCorrect;
    private int connectedTestsTaken;

    private TextView scoreView, difficultyView, averageView, timesTakenView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference recordDatabaseReference;
    private DatabaseReference categoryDatabaseReference;
    private DatabaseReference specificCategoryDatabaseReference;
    private DatabaseReference rankingDatabaseReference;

    private Ranking ranking;
    private List<Record> records;
    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;

    private RewardedVideoAd mRewardedVideoAd;

    private PieChart pieChart;

    public TestResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestResultsFragment newInstance(String param1, String param2) {
        TestResultsFragment fragment = new TestResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static TestResultsFragment newInstance(Ranking currentRanking, Test test, int amountCorrect){
        TestResultsFragment testResultsFragment = new TestResultsFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentRanking",currentRanking);
        args.putParcelable("test",test);
        Log.d("resultadostest",String.valueOf(amountCorrect));
        args.putInt("amountCorrect",amountCorrect);
        testResultsFragment.setArguments(args);
        return testResultsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        this.currentRanking=getArguments().getParcelable("currentRanking");
        this.test=getArguments().getParcelable("test");
        this.amountCorrect=getArguments().getInt("amountCorrect");

        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544/5224354917");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_results, container, false);

        records=new ArrayList<>();

        recyclerView=view.findViewById(R.id.result_record_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recordAdapter=new RecordAdapter(records,getContext(),this);
        recyclerView.setAdapter(recordAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String connectedId=sharedPreferences.getString("connected_id","");
        String connectedUsername=sharedPreferences.getString("connected_username","");
        connectedTestsTaken=sharedPreferences.getInt("connected_tests_taken",0);

        firebaseDatabase=FirebaseDatabase.getInstance();
        userDatabaseReference=firebaseDatabase.getReference().child("users");
        recordDatabaseReference=firebaseDatabase.getReference().child("records");
        categoryDatabaseReference=firebaseDatabase.getReference().child("categories");
        Log.d("categoryid", this.test.getCategoryId());
        specificCategoryDatabaseReference=categoryDatabaseReference.child(this.test.getCategoryId());
        rankingDatabaseReference=specificCategoryDatabaseReference.child("ranking");

        final ValueEventListener rankingListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ranking=dataSnapshot.getValue(Ranking.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        rankingDatabaseReference.addValueEventListener(rankingListener);

        ChildEventListener recordsEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Record record=dataSnapshot.getValue(Record.class);
                Log.d("records",currentRanking.getRecords().toString());
                if (currentRanking.getRecords().get(record.getId())!=null){
                    records.add(0, record);
                }
                recordAdapter.setRecords(records);
                recyclerView.setAdapter(recordAdapter);
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
        Query recordQuery = recordDatabaseReference
                .orderByChild("correctQuestions");
        recordQuery.addChildEventListener(recordsEventListener);

        String recordId=recordDatabaseReference.push().getKey();
        Record record=new Record(amountCorrect,this.test.getQuestions().size(),connectedId,connectedUsername,recordId);
        recordDatabaseReference.child(recordId).setValue(record);

        userDatabaseReference.child(connectedId).child("testsTaken").setValue(connectedTestsTaken+1);
        userDatabaseReference.child(connectedId).child("records").child(recordId).setValue(true);
        double multiplication=currentRanking.getAverageCorrect()*currentRanking.getAmountTaken()*test.getQuestions().size();
        multiplication=multiplication+amountCorrect;
        //multiplication=((double)(amountCorrect)/(double)(test.getQuestions().size()))*100;
        int newAmountTaken=currentRanking.getAmountTaken()+1;
        double newAverageCorrect=multiplication/(double)(test.getQuestions().size());
        newAverageCorrect=(double)newAverageCorrect/newAmountTaken;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("connected_tests_taken",connectedTestsTaken+1);
        editor.commit();

        rankingDatabaseReference.child("amountTaken").setValue(newAmountTaken);
        rankingDatabaseReference.child("averageCorrect").setValue(newAverageCorrect);
        rankingDatabaseReference.child("records").child(recordId).setValue(true);


        scoreView=(TextView)view.findViewById(R.id.result_percent_view);
        difficultyView=view.findViewById(R.id.result_difficulty_text_view);
        averageView=view.findViewById(R.id.result_average_text_view);
        timesTakenView=view.findViewById(R.id.result_times_taken_text_view);

        double proportion=(double)amountCorrect/(double)test.getQuestions().size();
        double percent=proportion*100;
        scoreView.setText(String.valueOf(percent).substring(0,2).concat("%"));
        if (this.currentRanking.getDifficulty()==0) {
            difficultyView.setText(getResources().getString(R.id.easy));
        } else if (this.currentRanking.getDifficulty()==1) {
            difficultyView.setText(getResources().getString(R.id.medium));
        } else if (this.currentRanking.getDifficulty()==2) {
            difficultyView.setText(getResources().getString(R.id.hard));
        }
        averageView.setText(String.valueOf(newAverageCorrect*100).substring(0,4).concat("%"));
        timesTakenView.setText(String.valueOf(currentRanking.getAmountTaken()));

        this.pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        /*for (Record currentRecord:records) {
            pieEntries.add(new Entry())
        }*/
        pieEntries.add(new PieEntry(4,0));
        pieEntries.add(new PieEntry(12,1));
        pieEntries.add(new PieEntry(16,2));
        pieEntries.add(new PieEntry(32,3));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, getString(R.string.answer1));

        ArrayList<String> pieLabels = new ArrayList<>();
        pieLabels.add("Jan");
        pieLabels.add("Feb");
        pieLabels.add("Mar");
        pieLabels.add("May");

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setDescription(new Description());

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

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRecordItemClicked(View v, int position) {

    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

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
