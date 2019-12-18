package Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Davquiroga on 8/05/2018.
 */

public class Ranking implements Parcelable{
    private double reputation;
    private Map<String, Boolean> records;
    private double averageCorrect;
    private int difficulty;
    private int amountTaken;
    private int amountScored;

    public Ranking(double reputation, Map<String, Boolean> records, double averageCorrect, int difficulty,
                    int amountTaken, int amountScored) {
        this.reputation = reputation;
        this.records = records;
        this.averageCorrect = averageCorrect;
        this.difficulty = difficulty;
        this.amountTaken=amountTaken;
        this.amountScored=amountScored;
    }

    public Ranking(){
        this.reputation=0;
        this.records=new HashMap<String,Boolean>();
        this.averageCorrect=0;
        this.difficulty=0;
        this.amountTaken=0;
        this.amountScored=0;
    }

    protected Ranking(Parcel in) {
        reputation = in.readDouble();
        averageCorrect = in.readDouble();
        difficulty = in.readInt();
        amountTaken = in.readInt();
        amountScored = in.readInt();
    }

    public static final Creator<Ranking> CREATOR = new Creator<Ranking>() {
        @Override
        public Ranking createFromParcel(Parcel in) {
            return new Ranking(in);
        }

        @Override
        public Ranking[] newArray(int size) {
            return new Ranking[size];
        }
    };

    public double getReputation() {
        return reputation;
    }

    public void setReputation(double reputation) {
        this.reputation = reputation;
    }

    public Map<String, Boolean> getRecords() {
        return records;
    }

    public void setRecords(Map<String, Boolean> records) {
        this.records = records;
    }

    public double getAverageCorrect() {
        return averageCorrect;
    }

    public void setAverageCorrect(double averageCorrect) {
        this.averageCorrect = averageCorrect;
        if (averageCorrect>=0.5){
            this.difficulty=0;
        } else if (averageCorrect<0.5 && averageCorrect>=0.2) {
            this.difficulty=1;
        } else if (averageCorrect<0.2) {
            this.difficulty=2;
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getAmountTaken() {
        return amountTaken;
    }

    public void setAmountTaken(int amountTaken) {
        this.amountTaken = amountTaken;
    }

    public int getAmountScored() {
        return amountScored;
    }

    public void setAmountScored(int amountScored) {
        this.amountScored = amountScored;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(reputation);
        parcel.writeMap(records);
        parcel.writeDouble(averageCorrect);
        parcel.writeInt(difficulty);
        parcel.writeInt(amountTaken);
        parcel.writeInt(amountScored);
    }

}
