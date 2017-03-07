package com.example.gregorio.gregorioapp;

import android.app.Application;

import com.example.gregorio.gregorioapp.utils.Constants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Gregorio on 03/02/2017.
 */

public class GregogramApplication extends Application {

    private StorageReference storageReference;

    private DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();

        //FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference =firebaseStorage.getReferenceFromUrl(Constants.FIREBASE_STORAGE_URL);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        this.databaseReference = firebaseDatabase.getReference(Constants.FIREBASE_DATABASE_LOCATION_POST);
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
