package com.example.gregorio.gregorioapp.api;

import com.example.gregorio.gregorioapp.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gregorio on 06/03/2017.
 */

public class GregogramClient {

    private Retrofit retrofit;

    public GregogramClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PostResponse.class, new PostResponseTypeAdapter()).create();

        this.retrofit = new Retrofit.Builder().baseUrl(Constants.FIREBASE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public GregogramFirebaseService getService(){
        return retrofit.create(GregogramFirebaseService.class);
    }
}
