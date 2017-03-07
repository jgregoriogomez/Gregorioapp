package com.example.gregorio.gregorioapp.views.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gregorio.gregorioapp.GregogramApplication;
import com.example.gregorio.gregorioapp.R;
import com.example.gregorio.gregorioapp.model.Post;
import com.example.gregorio.gregorioapp.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button buttonTakePicture;
    private ImageView imageViewPicture;
    private String mCurrentPhotoPath;
    private String absoluteCurrentPhotoPath;
    private GregogramApplication gregogramApplication;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

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

        this.gregogramApplication = (GregogramApplication)getActivity().getApplicationContext();

        this.storageReference = this.gregogramApplication.getStorageReference();
        this.databaseReference = this.gregogramApplication.getDatabaseReference();

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

        List<ResolveInfo> resInfoList = getActivity().getPackageManager()
                .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            try {
                photoFile = this.createImageFile();

                if(photoFile != null){

                    Uri photoUri = FileProvider.getUriForFile(getActivity(),
                            "com.example.gregorio.gregorioapp",photoFile);

                    //Se añade el siguiente for ya que el manejo del provider sin
                    //esto hace que la camara no responda después de tomar la foto
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        getActivity().grantUriPermission(packageName,
                                photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(takePictureIntent,NewPostFragment.REQUEST_IMAGE_CAPTURE);
                }

            } catch (Exception e) {
                Log.d(TAG, getString(R.string.new_post_error_create_file));
                e.printStackTrace();
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
            //Toast.makeText(getActivity(),mCurrentPhotoPath,Toast.LENGTH_LONG).show();

            //guardar la imagen en firebase
            uploadFileToFirebase();

        }
    }

    private void uploadFileToFirebase() {
        File file = new File(this.absoluteCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        StorageReference imageReference = storageReference
                .child(Constants.FIREBASE_STORAGE_IMAGES + contentUri
                        .getLastPathSegment());
        UploadTask uploadTask = imageReference.putFile(contentUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),getResources()
                        .getString(R.string.error_upload_image_to_firebase),
                        Toast.LENGTH_LONG).show();
            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String imageUrlFirebase =  null;
                /*Toast.makeText(getActivity(),taskSnapshot.getDownloadUrl()
                        .toString(),Toast.LENGTH_LONG).show();*/
                imageUrlFirebase = taskSnapshot.getDownloadUrl().toString();

                createNewPost(imageUrlFirebase);
            }
        });
    }

    private void createNewPost(String imageUrlFirebase) {
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("USER", getActivity().MODE_PRIVATE);
        String email = sharedPreferences.getString("email","");
        String enCodedEmail = email.replace(".",",");

        /*HashMap<String, Object> timeStampCreated = new HashMap<>();
        timeStampCreated.put("timestamp", ServerValue.TIMESTAMP);*/

        //Post post = new Post(enCodedEmail, imageUrlFirebase,timeStampCreated);
        Post post = new Post(enCodedEmail, imageUrlFirebase,new Date().getTime());

        databaseReference.push().setValue(post);

        getFragmentManager().popBackStackImmediate();
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        StringBuilder imageFileName = new StringBuilder().append("JPEG_")
                .append(timeStamp).append("_");
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName.toString(),
                ".jpg",storageDir
        );
        this.mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        this.absoluteCurrentPhotoPath = image.getAbsolutePath();
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
