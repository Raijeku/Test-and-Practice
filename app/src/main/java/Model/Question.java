package Model;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davquiroga on 2/04/2018.
 */

public class Question implements Parcelable{
    private String question;
    private List<String> images;
    private List<String> answers;
    private String correctAnswer;

    public Question() {
        this.question = "";
        this.images = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.correctAnswer = "";
    }

    public Question(String question, List<String> images, List<String> answers, String correctAnswer) {
        this.question = question;
        this.images = images;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    protected Question(Parcel in) {
        question = in.readString();
        answers = in.createStringArrayList();
        correctAnswer = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeStringList(answers);
        parcel.writeString(correctAnswer);
    }

}
