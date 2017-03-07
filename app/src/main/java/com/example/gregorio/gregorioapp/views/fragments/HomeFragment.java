package com.example.gregorio.gregorioapp.views.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gregorio.gregorioapp.adapter.PostAdapterRecyclerView;
import com.example.gregorio.gregorioapp.api.GregogramClient;
import com.example.gregorio.gregorioapp.api.GregogramFirebaseService;
import com.example.gregorio.gregorioapp.api.PostResponse;
import com.example.gregorio.gregorioapp.R;
import com.example.gregorio.gregorioapp.model.Post;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView pictureRecycler;

    private LinearLayoutManager linearLayoutManager;

    private ArrayList<Post> postList;

    private PostAdapterRecyclerView postAdapterRecyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showToolbar(getResources().getString(R.string.tab_home), false, view);

        this.postList = new ArrayList<>();
        /*Post postTest = new Post();
        postTest.setAuthor("Grego");
        postList.add(postTest);*/
        populateData();

        this.pictureRecycler = (RecyclerView) view.findViewById(R.id.pictureRecycler);
        this.linearLayoutManager = new LinearLayoutManager(getContext());
        this.linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.pictureRecycler.setLayoutManager(this.linearLayoutManager);

        this.postAdapterRecyclerView = new
                PostAdapterRecyclerView(postList,R.layout.cardview_picture,getActivity());


        pictureRecycler.setAdapter(postAdapterRecyclerView);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPostFragment newPostFragment = new NewPostFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, newPostFragment)
                        .addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }

    private void populateData() {
        GregogramFirebaseService service = (new GregogramClient()).getService();

        final Call<PostResponse> postListCall = service.getPostlist();
        postListCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                if(response.isSuccessful()){
                    PostResponse result = response.body();
                    //postList = result.getPostList();
                    postList.clear();
                    postList.addAll(result.getPostList());
                    postAdapterRecyclerView.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });
    }

    /*private ArrayList<Picture> buildPictures(){
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        pictures.add(new Picture("http://www.novalandtours.com/images/guide/guilin.jpg","Uriel Ramírez","4 días","20 Me Gusta"));
        pictures.add(new Picture("http://www.enjoyart.com/library/landscapes/tuscanlandscapes/large/Tuscan-Bridge--by-Art-Fronckowiak-.jpg","Juan Pable","3 días","10 Me Gusta"));
        pictures.add(new Picture("http://www.educationquizzes.com/library/KS3-Geography/river-1-1.jpg","Anahi salgado","2 días","9 Me Gusta"));
        pictures.add(new Picture("http://www.novalandtours.com/images/guide/guilin.jpg","Gregorio Gómez","2 días","200 Me Gusta"));
        return pictures;
    }*/

    private void showToolbar(String tittle, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}
