package com.example.gregorio.gregorioapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class LoginActivity extends AppCompatActivity {

    //Para implementar el login con facebook
    // es necesario crear un callbackmanager
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Es necesario crear un callbackmanager para implementar la autenticación con facebook
        callbackManager = CallbackManager.Factory.create();
        final LoginButton btnLoginFacebook = (LoginButton)findViewById(R.id.btn_login_facebook);

        //variables que manejan el login con correo y password con firebase
        final Button btnloginWithMail = (Button) findViewById(R.id.login);
        final TextInputEditText textInputUserName = (TextInputEditText)findViewById(R.id.username);
        final TextInputEditText TextInputPass = (TextInputEditText) findViewById(R.id.password);

        btnLoginFacebook.setReadPermissions("email");//TODO probar comentando esta instrucción

        //implementación de firebase para ingreso con facebook o con email
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    goContainer();
                }
            }
        };
        setButtonLoginFacebookBehavior(btnLoginFacebook);
        setButtonloginWithMailBehavior(btnloginWithMail, textInputUserName, TextInputPass);

    }

    private void setButtonloginWithMailBehavior(final Button btnloginWithMail,
                                                final TextInputEditText textInputUserName,
                                                final TextInputEditText TextInputPass){
        //implementación de login usando correo y password a traves de firebase
        btnloginWithMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        //goContainer();
    }

    private void setButtonLoginFacebookBehavior(final LoginButton btnLoginFacebook){
        //se manejan los eventos sobre el boton com.facebook.login.widget.LoginButton
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                singInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),R.string.cancel_facebook_login,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),R.string.error_facebook_login +
                        " " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void singInWithFacebook(AccessToken accessToken) {
        //Codigo basico para implementar autenticación con facebook
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //error de autenticacion
                    Toast.makeText(getApplicationContext(),
                            R.string.error_facebook_login_with_firebase,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //este metodo es necesario para implementar la autenticación en facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void goCreateAccount(View view){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        this.startActivity(intent);
    }

    private void goContainer(){
        Intent intent = new Intent(this, ContainerActivity.class);
        //esta instruccion se usa con el seteo de flag para que al
        //regresar no se muestre la ventana de login nuevamente
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
            | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }

    public void goSite(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://platzigram.com"));
        this.startActivity(intent);
    }
}
