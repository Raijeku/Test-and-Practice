package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Davquiroga on 2/04/2018.
 */

public class Category {
    private String name;
    private String description;
    private Map<String, Boolean> subcategories;
    private List<Question> questions;
    private boolean isRoot;
    private Ranking ranking;
    private String id;

    public Category() {
        this.name = "";
        this.description = "";
        this.subcategories = new HashMap<String, Boolean>();
        this.questions = new ArrayList<>();
        this.isRoot = true;
        this.ranking=new Ranking();
        this.id="";
    }

    public Category(String name, String description, Map<String, Boolean> subcategories, List<Question> questions, boolean isRoot, Ranking ranking, String id) {
        this.name = name;
        this.description = description;
        this.subcategories = subcategories;
        this.questions = questions;
        this.isRoot = isRoot;
        this.ranking=ranking;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Boolean> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Map<String, Boolean> subcategories) {
        this.subcategories = subcategories;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
