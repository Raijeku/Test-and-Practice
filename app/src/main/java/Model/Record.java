package Model;

/**
 * Created by Davquiroga on 8/05/2018.
 */

public class Record {
    private int correctQuestions;
    private int amountQuestions;
    private String testedId;
    private String username;
    private String id;

    public Record(int correctQuestions, int amountQuestions, String testedId, String username, String id) {
        this.correctQuestions = correctQuestions;
        this.amountQuestions = amountQuestions;
        this.testedId = testedId;
        this.username = username;
        this.id=id;
    }

    public Record(){
        this.correctQuestions=0;
        this.amountQuestions=0;
        this.testedId="";
        this.username="";
        this.id="";
    }

    public int getCorrectQuestions() {
        return correctQuestions;
    }

    public void setCorrectQuestions(int correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    public int getAmountQuestions() {
        return amountQuestions;
    }

    public void setAmountQuestions(int amountQuestions) {
        this.amountQuestions = amountQuestions;
    }

    public String getTestedId() {
        return testedId;
    }

    public void setTestedId(String testedId) {
        this.testedId = testedId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
