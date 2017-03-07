package com.example.gregorio.gregorioapp.api;

import com.example.gregorio.gregorioapp.model.Post;

import java.util.ArrayList;

/**
 * Created by Gregorio on 06/03/2017.
 */

public class PostResponse {
    ArrayList<Post> postList = new ArrayList<>();

    public ArrayList<Post> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }
}
