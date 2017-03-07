package com.example.gregorio.gregorioapp.api;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Gregorio on 06/03/2017.
 */

public interface GregogramFirebaseService {
    @GET("post.json")
    Call<PostResponse> getPostlist();
}
