package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Davquiroga on 4/04/2018.
 */

public class Test implements Parcelable{
    private List<Question> questions;
    private String name;
    private float rating;
    private String categoryId;

    public Test(List<Question> questions, String name, float rating, String categoryId) {
        this.questions = questions;
        this.name = name;
        this.rating = rating;
        this.categoryId=categoryId;
    }

    protected Test(Parcel in) {
        questions = in.createTypedArrayList(Question.CREATOR);
        name = in.readString();
        rating = in.readFloat();
        categoryId=in.readString();
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(questions);
        parcel.writeString(name);
        parcel.writeFloat(rating);
        parcel.writeString(categoryId);
    }
}
