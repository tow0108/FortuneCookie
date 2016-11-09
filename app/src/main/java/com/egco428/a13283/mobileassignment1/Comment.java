package com.egco428.a13283.mobileassignment1;

/**
 * Created by Natamon Tangmo on 20-Oct-16.
 */
public class Comment {
    private long id;
    private String comment;
    private String date;
    private String position;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }


    public void setPosition(String position) {
        this.position = position;
    }
    public String getPosition() {
        return position;
    }


    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return comment;
    }
}