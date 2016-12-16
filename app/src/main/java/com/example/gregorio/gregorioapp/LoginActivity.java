package com.example.gregorio.gregorioapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.gregorio.gregorioapp.views.ContainerActivity;
import com.example.gregorio.gregorioapp.views.CreateAccountActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goCreateAccount(View view){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        this.startActivity(intent);
    }

    public void goContainer(View view){
        Intent intent = new Intent(this, ContainerActivity.class);
        this.startActivity(intent);
    }

    public void goSite(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://platzigram.com"));
        this.startActivity(intent);
    }
}
