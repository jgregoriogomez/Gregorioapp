package com.example.gregorio.gregorioapp.views.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gregorio.gregorioapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button buttonTakePicture;
    private ImageView imageViewPicture;
    private String mCurrentPhotoPath;

    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);
        showToolbar(getResources().getString(R.string.new_post_title), false, view);
        this.buttonTakePicture = (Button) view.findViewById(R.id.btnTakePicture);
        this.imageViewPicture = (ImageView)view.findViewById(R.id.ivPicture);

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        return view;
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            try {
                photoFile = this.createImageFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(getActivity(),"com.example.gregorio.gregorioapp",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePictureIntent,NewPostFragment.REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NewPostFragment.REQUEST_IMAGE_CAPTURE
                && resultCode == getActivity().RESULT_OK){
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            this.imageViewPicture.setImageBitmap(imageBitmap);*/
            Picasso.with(getActivity()).load(mCurrentPhotoPath).into(imageViewPicture);
            addPictureToGallery();
            Toast.makeText(getActivity(),mCurrentPhotoPath,Toast.LENGTH_LONG).show();

        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date());
        StringBuilder imageFileName = new StringBuilder().append("JPEG")
                .append(timeStamp).append("_");
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName.toString(),
                ".jpg",storageDir
        );
        this.mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void addPictureToGallery(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void showToolbar(String tittle, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
