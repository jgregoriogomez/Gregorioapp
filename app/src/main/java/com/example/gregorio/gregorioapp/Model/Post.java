package com.example.gregorio.gregorioapp.model;

import android.text.format.DateUtils;

import java.util.HashMap;

/**
 * Created by Gregorio on 02/03/2017.
 */

public class Post {

    private String uId;

    private String author;

    private String imageUrl;

    private double timeStampCreated;

    public Post(){

    }

    public Post(String author, String imageUrl, double timeStampCreated) {
        this.author = author;
        this.imageUrl = imageUrl;
        this.timeStampCreated = timeStampCreated;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getTimeStampCreated() {
        return timeStampCreated;
    }

    public void setTimeStampCreated(double timeStampCreated) {
        this.timeStampCreated = timeStampCreated;
    }


}
