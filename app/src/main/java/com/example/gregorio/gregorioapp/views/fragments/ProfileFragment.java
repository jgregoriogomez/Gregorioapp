package com.example.gregorio.gregorioapp.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gregorio.gregorioapp.model.Picture;
import com.example.gregorio.gregorioapp.R;
import com.example.gregorio.gregorioapp.adapter.PictureAdapterRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        RecyclerView pictureRecycler = (RecyclerView) view
                .findViewById(R.id.pictureProfileRecycler);

        TextView userNameText = (TextView)view.findViewById(R.id.userNameProfile);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        pictureRecycler.setLayoutManager(linearLayoutManager);

        PictureAdapterRecyclerView pictureAdapterRecyclerView = new
                PictureAdapterRecyclerView(buildPictures(),R.layout
                .cardview_picture,getActivity());

        pictureRecycler.setAdapter(pictureAdapterRecyclerView);
        showToolbar(null,false,view);

        //TODO añadir datos del usuario logeado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            userNameText.setText(user.getEmail());
        }else{
            //userNameText.setText(getContext().getResources().getString(R.string.username_card));
        }

        return view;
    }

    public ArrayList<Picture> buildPictures(){
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        pictures.add(new Picture("http://www.novalandtours.com/images/guide/guilin.jpg","Uriel Ramírez","4 días","20 Me Gusta"));
        pictures.add(new Picture("http://www.enjoyart.com/library/landscapes/tuscanlandscapes/large/Tuscan-Bridge--by-Art-Fronckowiak-.jpg","Juan Pable","3 días","10 Me Gusta"));
        pictures.add(new Picture("http://www.educationquizzes.com/library/KS3-Geography/river-1-1.jpg","Anahi salgado","2 días","9 Me Gusta"));
        pictures.add(new Picture("http://www.novalandtours.com/images/guide/guilin.jpg","Gregorio Gómez","2 días","200 Me Gusta"));
        return pictures;
    }

    public void showToolbar(String tittle, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}
