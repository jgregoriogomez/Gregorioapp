package com.example.gregorio.gregorioapp.adapter;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gregorio.gregorioapp.R;
import com.example.gregorio.gregorioapp.model.Picture;
import com.example.gregorio.gregorioapp.model.Post;
import com.example.gregorio.gregorioapp.views.PictureDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gregorio on 03/12/2016.
 */

public class PostAdapterRecyclerView extends RecyclerView.Adapter<PostAdapterRecyclerView.PictureViewHolder> {

    private ArrayList<Post> posts;
    private int resource;
    private Activity activity;

    public PostAdapterRecyclerView(ArrayList<Post> posts, int resource, Activity activity) {
        this.posts = posts;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new PictureViewHolder(view);
    }

   private static String getRelativeTimeStamp(double timeStampCreated){
        return DateUtils.getRelativeTimeSpanString((long)timeStampCreated,
                System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_WEEKDAY).toString();
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        Post post= posts.get(position);
        holder.userNameCard.setText(post.getAuthor());

        //holder.timeCard.setText(String.valueOf(post.getRelativeTimeStamp()));
        holder.timeCard.setText(getRelativeTimeStamp(post.getTimeStampCreated()));
        holder.likeNumberCard.setText("5");
        Picasso.with(activity).load(post.getImageUrl()).into(holder.pictureCard);

        holder.pictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PictureDetailsActivity.class);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Explode explode = new Explode();
                    explode.setDuration(5000);
                    activity.getWindow().setExitTransition(explode);
                    activity.startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity,view,
                                    activity.getString(R.string.transtionname_picture)).toBundle());
                }else{
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder{

        private ImageView pictureCard;
        private TextView userNameCard;
        private TextView timeCard;
        private TextView likeNumberCard;

        public PictureViewHolder(View itemView) {
            super(itemView);
            pictureCard    = (ImageView) itemView.findViewById(R.id.pictureCard);
            userNameCard   = (TextView) itemView.findViewById(R.id.userNameCard);
            timeCard       = (TextView) itemView.findViewById(R.id.timeCard);
            likeNumberCard = (TextView) itemView.findViewById(R.id.likeNumberCard);
        }
    }
}
