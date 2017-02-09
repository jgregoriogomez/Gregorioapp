package com.example.gregorio.gregorioapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gregorio.gregorioapp.views.ContainerActivity;
import com.example.gregorio.gregorioapp.views.CreateAccountActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private CallbackManager callbackManager;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button btnlogin = (Button) findViewById(R.id.login);
        final LoginButton btnLoginFacebook = (LoginButton)findViewById(R.id.btn_login_facebook);
        final TextInputEditText textInputUserName = (TextInputEditText)findViewById(R.id.username);
        final TextInputEditText TextInputPass = (TextInputEditText) findViewById(R.id.password);

        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebook.setReadPermissions("email");//TODO probar comentando esta instrucción

        /*authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Log.d("login: ", user.getUid());
                }else{
                    Log.d("login: ", "Signed out");
                }
            }
        };*/

        //firebaseAuth.addAuthStateListener(authStateListener);

        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                singInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                String user = textInputUserName.getText().toString().trim();
                String pass = TextInputPass.getText().toString().trim();

                if(user == null || pass == null
                        || user.isEmpty() || pass.isEmpty()){
                    Toast.makeText(getApplicationContext(), getResources()
                            .getString(R.string.login_fields_empty_message),Toast.LENGTH_SHORT);
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(user,pass)
                        .addOnCompleteListener(LoginActivity.this,
                                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            String msgErrFirebase = task.getException()
                                    .getMessage();
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.
                                            error_sing_in_on_firebase)
                                            + " "+ msgErrFirebase,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            goContainer();
                        }
                    }
                });
            }
        });
    }

    private void singInWithFacebook(AccessToken accessToken) {
        //cosas propias de firebase
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        goContainer();
        /*firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    goContainer();
                }
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void goCreateAccount(View view){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        this.startActivity(intent);
    }

    private void goContainer(){
        Intent intent = new Intent(this, ContainerActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void goSite(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://platzigram.com"));
        this.startActivity(intent);
    }
}
