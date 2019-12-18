package Model;

import android.net.Uri;

import java.util.Map;

/**
 * Created by Davquiroga on 29/03/2018.
 */

public class User {
    private String id;
    private String username;
    private String email;
    private String image;
    private String password;
    private int testsTaken;
    private Map<String, Boolean> records;

    public User(String id, String username, String email, String image, String password, int testsTaken, Map<String, Boolean> records) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.image = image;
        this.password = password;
        this.records=records;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTestsTaken() {
        return testsTaken;
    }

    public void setTestsTaken(int testsTaken) {
        this.testsTaken = testsTaken;
    }

    public Map<String, Boolean> getRecords() {
        return records;
    }

    public void setRecords(Map<String, Boolean> records) {
        this.records = records;
    }
}
