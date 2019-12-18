package raistudio.testandpractice;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Category;
import Model.Question;
import Model.Ranking;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdministrateCategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdministrateCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdministrateCategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    
    private EditText nameView, descriptionView, question1view, question2view, question3view,question4view,
            question5view,question1answer1view, question1answer2view, question1answer3view,
            question1answer4view,question2answer1view,question2answer2view,question2answer3view,question2answer4view,
            question3answer1view,question3answer2view,question3answer3view,question3answer4view,question4answer1view,
            question4answer2view,question4answer3view,question4answer4view,question5answer1view,question5answer2view,
            question5answer3view,question5answer4view,question1answer,question2answer,question3answer,question4answer,
            question5answer;

    private ImageButton selectButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference categoryReference;

    public AdministrateCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdministrateCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdministrateCategoryFragment newInstance(String param1, String param2) {
        AdministrateCategoryFragment fragment = new AdministrateCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_administrate_category, container, false);

        firebaseDatabase=FirebaseDatabase.getInstance();
        categoryReference=firebaseDatabase.getReference().child("categories");

        nameView=view.findViewById(R.id.administrate_category_name_view);
        descriptionView=view.findViewById(R.id.administrate_category_description_view);
        question1view=view.findViewById(R.id.administrate_category_question1_question_view);
        question2view=view.findViewById(R.id.administrate_category_question2_question_view);
        question3view=view.findViewById(R.id.administrate_category_question3_question_view);
        question4view=view.findViewById(R.id.administrate_category_question4_question_view);
        question5view=view.findViewById(R.id.administrate_category_question5_question_view);

        question1answer=view.findViewById(R.id.administrate_category_question1_correct_answer_view);
        question2answer=view.findViewById(R.id.administrate_category_question2_correct_answer_view);
        question3answer=view.findViewById(R.id.administrate_category_question3_correct_answer_view);
        question4answer=view.findViewById(R.id.administrate_category_question4_correct_answer_view);
        question5answer=view.findViewById(R.id.administrate_category_question5_correct_answer_view);


        question1answer1view=view.findViewById(R.id.administrate_category_question1_answer1_view);
        question1answer2view=view.findViewById(R.id.administrate_category_question1_answer2_view);
        question1answer3view=view.findViewById(R.id.administrate_category_question1_answer3_view);
        question1answer4view=view.findViewById(R.id.administrate_category_question1_answer4_view);

        question2answer1view=view.findViewById(R.id.administrate_category_question2_answer1_view);
        question2answer2view=view.findViewById(R.id.administrate_category_question2_answer2_view);
        question2answer3view=view.findViewById(R.id.administrate_category_question2_answer3_view);
        question2answer4view=view.findViewById(R.id.administrate_category_question2_answer4_view);

        question3answer1view=view.findViewById(R.id.administrate_category_question3_answer1_view);
        question3answer2view=view.findViewById(R.id.administrate_category_question3_answer2_view);
        question3answer3view=view.findViewById(R.id.administrate_category_question3_answer3_view);
        question3answer4view=view.findViewById(R.id.administrate_category_question3_answer4_view);

        question4answer1view=view.findViewById(R.id.administrate_category_question4_answer1_view);
        question4answer2view=view.findViewById(R.id.administrate_category_question4_answer2_view);
        question4answer3view=view.findViewById(R.id.administrate_category_question4_answer3_view);
        question4answer4view=view.findViewById(R.id.administrate_category_question4_answer4_view);

        question5answer1view=view.findViewById(R.id.administrate_category_question5_answer1_view);
        question5answer2view=view.findViewById(R.id.administrate_category_question5_answer2_view);
        question5answer3view=view.findViewById(R.id.administrate_category_question5_answer3_view);
        question5answer4view=view.findViewById(R.id.administrate_category_question5_answer4_view);

        selectButton=view.findViewById(R.id.administrate_category_select_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> images= new ArrayList<String>();
                images.add("");
                images.add("");
                images.add("");
                images.add("");
                images.add("");


                List<String> answers1= new ArrayList<String>();
                answers1.add(question1answer1view.getText().toString());
                answers1.add(question1answer2view.getText().toString());
                answers1.add(question1answer3view.getText().toString());
                answers1.add(question1answer4view.getText().toString());
                Log.d("ansers",answers1.toString());

                Question question1=new Question(question1view.getText().toString(),images,answers1,question1answer.getText().toString());

                List<String> answers2= new ArrayList<String>();
                answers2= new ArrayList<String>();
                answers2.add(question2answer1view.getText().toString());
                answers2.add(question2answer2view.getText().toString());
                answers2.add(question2answer3view.getText().toString());
                answers2.add(question2answer4view.getText().toString());

                Question question2=new Question(question2view.getText().toString(),
                        images,answers2,
                        question2answer.getText().toString());


                List<String> answers3= new ArrayList<String>();
                answers3= new ArrayList<String>();
                answers3.add(question3answer1view.getText().toString());
                answers3.add(question3answer2view.getText().toString());
                answers3.add(question3answer3view.getText().toString());
                answers3.add(question3answer4view.getText().toString());

                Question question3=new Question(question3view.getText().toString(),
                        images,answers3,
                        question3answer.getText().toString());

                List<String> answers4= new ArrayList<String>();
                answers4= new ArrayList<String>();
                answers4.add(question4answer1view.getText().toString());
                answers4.add(question4answer2view.getText().toString());
                answers4.add(question4answer3view.getText().toString());
                answers4.add(question4answer4view.getText().toString());

                Question question4=new Question(question4view.getText().toString(),
                        images,answers4,
                        question4answer.getText().toString());

                List<String> answers5= new ArrayList<String>();
                answers5= new ArrayList<String>();
                answers5.add(question5answer1view.getText().toString());
                answers5.add(question5answer2view.getText().toString());
                answers5.add(question5answer3view.getText().toString());
                answers5.add(question5answer4view.getText().toString());

                Question question5=new Question(question5view.getText().toString(),
                        images,answers5,
                        question5answer.getText().toString());


                List<Question> questions=new ArrayList<Question>();
                questions.add(question1);
                questions.add(question2);
                questions.add(question3);
                questions.add(question4);
                questions.add(question5);

                String key = categoryReference.push().getKey();
                Category newCategory;

                if (mParam1!="") {
                    newCategory = new Category(nameView.getText().toString(), descriptionView.getText().toString(),
                            new HashMap<String, Boolean>(), questions, false, new Ranking(), key);
                } else {
                    newCategory = new Category(nameView.getText().toString(), descriptionView.getText().toString(),
                            new HashMap<String, Boolean>(), questions, true, new Ranking(), key);
                }

                categoryReference.child(key).setValue(newCategory);
                /*categoryReference.child(key).child("questions").child("answers").setValue(answers);
                categoryReference.child(key).child("questions").child("images").setValue(images);*/
                if (mParam1!="") {
                    categoryReference.child(mParam1).child("subcategories").child(key).setValue(true);
                }
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
