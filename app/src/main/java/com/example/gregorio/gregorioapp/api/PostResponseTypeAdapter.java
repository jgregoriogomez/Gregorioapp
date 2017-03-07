package com.example.gregorio.gregorioapp.api;

import com.example.gregorio.gregorioapp.model.Post;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Gregorio on 06/03/2017.
 */

public class PostResponseTypeAdapter extends TypeAdapter {
    @Override
    public void write(JsonWriter out, Object value) throws IOException {

    }

    @Override
    public PostResponse read(JsonReader reader) throws IOException {
        final PostResponse response = new PostResponse();
        ArrayList<Post> postList = new ArrayList<Post>();
        Post post = null;
        reader.beginObject();
        while(reader.hasNext()){
            try {
                post = readPost(reader);
            } catch (Exception e) {
                e.printStackTrace();
            }
            postList.add(post);
        }
        reader.endObject();
        response.setPostList(postList);
        return response;
    }

    public Post readPost(JsonReader reader) throws Exception{

        Post post = new Post();
        try{
            reader.nextName();
            reader.beginObject();
            while(reader.hasNext()){
                String next = reader.nextName();
                switch (next){
                    case "author":
                        post.setAuthor(reader.nextString());
                        break;
                    case "imageUrl":
                        post.setImageUrl(reader.nextString());
                        break;
                    case "timeStampCreated":
                        /*reader.beginObject();
                        reader.nextName();*/
                        post.setTimeStampCreated(reader.nextDouble());
                        //reader.endObject();
                        break;
                }
            }
        }catch(Exception e){
            System.out.print(e.getCause());
        }finally {
            reader.endObject();
        }
        return post;
    }
}
