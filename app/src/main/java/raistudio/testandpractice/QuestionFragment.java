package raistudio.testandpractice;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Question;
import Model.Ranking;
import Model.Test;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView questionView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private TextView answer1View;
    private TextView answer2View;
    private TextView answer3View;
    private TextView answer4View;

    private boolean isImage1FitToScreen;
    private boolean isImage2FitToScreen;
    private boolean isImage3FitToScreen;
    private boolean isImage4FitToScreen;

    private Ranking ranking;
    private Test test;
    private int index;
    private int amountCorrect;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static QuestionFragment newInstance(Ranking ranking, Test test, int index, int amountCorrect){
        QuestionFragment questionFragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable("ranking",ranking);
        args.putParcelable("test",test);
        args.putInt("index",index);
        args.putInt("amountCorrect",amountCorrect);
        questionFragment.setArguments(args);
        return questionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.ranking = getArguments().getParcelable("ranking");
        this.test = getArguments().getParcelable("test");
        this.index=getArguments().getInt("index");
        this.amountCorrect=getArguments().getInt("amountCorrect");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        imageView1=(ImageView)view.findViewById(R.id.image1_view);
        if (!this.test.getQuestions().get(this.index).getImages().get(0).isEmpty()) {
            Log.d("images",this.test.getQuestions().get(this.index).getImages().toString());
            Picasso.get().load(this.test.getQuestions().get(this.index).getImages().get(0))
                    .into(imageView1);
        } else {
            imageView1.setVisibility(View.INVISIBLE);
            imageView1.setEnabled(false);
            imageView1.getLayoutParams().height=0;
        }

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(isImage1FitToScreen) {
                    isImage1FitToScreen=false;
                    imageView1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView1.setAdjustViewBounds(true);
                }else{
                    isImage1FitToScreen=true;
                    imageView1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                }*/
            }
        });
        imageView2=(ImageView)view.findViewById(R.id.image2_view);
        if (!this.test.getQuestions().get(this.index).getImages().get(1).isEmpty()){
            Picasso.get().load(this.test.getQuestions().get(this.index).getImages().get(1))
                    .into(imageView2);
        } else {
            imageView2.setVisibility(View.INVISIBLE);
            imageView2.setEnabled(false);
            imageView2.getLayoutParams().height=0;
        }
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(isImage2FitToScreen) {
                    isImage2FitToScreen=false;
                    imageView2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView2.setAdjustViewBounds(true);
                }else{
                    isImage2FitToScreen=true;
                    imageView2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
                }*/
            }
        });
        imageView3=(ImageView)view.findViewById(R.id.image3_view);
        if (!this.test.getQuestions().get(this.index).getImages().get(2).isEmpty()){
            Picasso.get().load(this.test.getQuestions().get(this.index).getImages().get(2))
                    .into(imageView3);
        }
        else {
            imageView3.setVisibility(View.INVISIBLE);
            imageView3.setEnabled(false);
            imageView3.getLayoutParams().height=0;
        }
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(isImage3FitToScreen) {
                    isImage3FitToScreen=false;
                    imageView3.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView3.setAdjustViewBounds(true);
                }else{
                    isImage3FitToScreen=true;
                    imageView3.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
                }*/
            }
        });
        imageView4=(ImageView)view.findViewById(R.id.image4_view);
        if (!this.test.getQuestions().get(this.index).getImages().get(3).isEmpty()){
            Picasso.get().load(this.test.getQuestions().get(this.index).getImages().get(3))
                    .into(imageView4);
        } else {
            imageView4.setVisibility(View.INVISIBLE);
            imageView4.setEnabled(false);
            imageView4.getLayoutParams().height=0;
        }
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(isImage4FitToScreen) {
                    isImage4FitToScreen=false;
                    imageView4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    imageView4.setAdjustViewBounds(true);
                }else{
                    isImage4FitToScreen=true;
                    imageView4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    imageView4.setScaleType(ImageView.ScaleType.FIT_XY);
                }*/
            }
        });

        final Question currentQuestion=test.getQuestions().get(index);

        questionView=(TextView)view.findViewById(R.id.question_view);
        questionView.setText(currentQuestion.getQuestion());
        answer1View=(TextView)view.findViewById(R.id.answer1_view);
        List<String> answers=currentQuestion.getAnswers();
        answer1View.setText(answers.get(0));
        answer1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateAnswer(answer1View.getText().toString(),currentQuestion.getCorrectAnswer());
            }
        });
        answer2View=(TextView)view.findViewById(R.id.answer2_view);
        answer2View.setText(answers.get(1));
        answer2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateAnswer(answer2View.getText().toString(),currentQuestion.getCorrectAnswer());
            }
        });
        answer3View=(TextView)view.findViewById(R.id.answer3_view);
        answer3View.setText(answers.get(2));
        answer3View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateAnswer(answer3View.getText().toString(),currentQuestion.getCorrectAnswer());
            }
        });
        answer4View=(TextView)view.findViewById(R.id.answer4_view);
        answer4View.setText(answers.get(3));
        answer4View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateAnswer(answer4View.getText().toString(),currentQuestion.getCorrectAnswer());
            }
        });

        return view;
    }

    private void evaluateAnswer(String answer, String correctAnswer){
        Fragment genericFragment=null;
        Log.d("resultados",answer.concat(correctAnswer));
        if (index+1<test.getQuestions().size()){
            if (answer.equals(correctAnswer)){
                Log.d("resultadosbien",String.valueOf(amountCorrect+1));
                genericFragment= QuestionFragment.newInstance(ranking, test, index+1, amountCorrect+1);
            } else {
                Log.d("resultadosmal",String.valueOf(amountCorrect));
                genericFragment= QuestionFragment.newInstance(ranking, test, index+1, amountCorrect);
            }
        } else {
            if (answer.equals(correctAnswer)){
                Log.d("resultadosmalisimos",String.valueOf(amountCorrect+1));
                genericFragment= TestResultsFragment.newInstance(ranking, test,amountCorrect+1);
            } else {
                Log.d("resultadosbuenisimos",String.valueOf(amountCorrect));
                genericFragment= TestResultsFragment.newInstance(ranking, test,amountCorrect);
            }
        }

        Log.d("categoryidquestion",test.getCategoryId());

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, genericFragment)
                .addToBackStack(null)
                .commit();
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
