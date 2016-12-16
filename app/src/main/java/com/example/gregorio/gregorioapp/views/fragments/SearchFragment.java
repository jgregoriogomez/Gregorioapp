package com.example.gregorio.gregorioapp.views.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.example.gregorio.gregorioapp.Model.Picture;
import com.example.gregorio.gregorioapp.R;
import com.example.gregorio.gregorioapp.adapter.PictureAdapterRecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.searchRecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL,false);
        //gridLayoutManager.
        Drawable horizontalDivider = ContextCompat.getDrawable(getContext(), R.drawable.plus);
        Drawable verticalDivider = ContextCompat.getDrawable(getContext(), R.drawable.plus);
        recyclerView.addItemDecoration(new GridDividerItemDecoration(horizontalDivider, verticalDivider,2));
        recyclerView.setLayoutManager(gridLayoutManager);
        //gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        PictureAdapterRecyclerView pictureAdapterRecyclerView = new
                PictureAdapterRecyclerView(buildPictures(),R.layout.cardview_picture,getActivity());
        recyclerView.setAdapter(pictureAdapterRecyclerView);
        return view;
    }

    private ArrayList<Picture> buildPictures(){
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        pictures.add(new Picture("http://www.novalandtours.com/images/guide/guilin.jpg","Uriel Ramírez","4 días","20 Me Gusta"));
        pictures.add(new Picture("http://www.enjoyart.com/library/landscapes/tuscanlandscapes/large/Tuscan-Bridge--by-Art-Fronckowiak-.jpg","Juan Pable","3 días","10 Me Gusta"));
        pictures.add(new Picture("http://www.educationquizzes.com/library/KS3-Geography/river-1-1.jpg","Anahi salgado","2 días","9 Me Gusta"));
        pictures.add(new Picture("http://www.novalandtours.com/images/guide/guilin.jpg","Gregorio Gómez","2 días","200 Me Gusta"));
        pictures.add(new Picture("http://www.novalandtours.com/images/guide/guilin.jpg","Gregorio Gómez","2 días","200 Me Gusta"));
        return pictures;
    }

}
