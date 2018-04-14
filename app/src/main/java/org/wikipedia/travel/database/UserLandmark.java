package org.wikipedia.travel.database;

/**
 * Represents a user's saved landmarks
 */

public class UserLandmark {

    public static final UserLandmarkTable DATABASE_TABLE = new UserLandmarkTable();

    private long id;
    private String title;

    public UserLandmark(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setString(String title) {
        this.title = title;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
