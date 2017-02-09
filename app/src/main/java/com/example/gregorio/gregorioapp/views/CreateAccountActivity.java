package com.example.gregorio.gregorioapp.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gregorio.gregorioapp.LoginActivity;
import com.example.gregorio.gregorioapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        showToolbar(getResources().getString(R.string.toolbar_tittle_createaccount), true);

        final TextInputEditText textEmail = (TextInputEditText) findViewById(R.id.email);
        final TextInputEditText textName = (TextInputEditText) findViewById(R.id.name);
        final TextInputEditText textUser = (TextInputEditText) findViewById(R.id.user);
        final TextInputEditText textPass = (TextInputEditText) findViewById(R.id.
                password_createaccount);
        final TextInputEditText textConfirPass = (TextInputEditText) findViewById(R.id.
                confirmPassword);


        Button btnCreateAccount = (Button) findViewById(R.id.joinUs);
        try{
            btnCreateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    String email = textEmail.getText().toString().trim();
                    String name = textName.getText().toString().trim();
                    String user = textUser.getText().toString().trim();
                    String pass = textPass.getText().toString().trim();
                    String confirmPass = textConfirPass.getText().toString().trim();

                    if (email == null || email.isEmpty()){
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.
                                validation_email_create_acount) ,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pass == null || confirmPass == null
                            || pass.isEmpty() || confirmPass.isEmpty()){
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.
                                        validation_pass_and_confirm_create_acount),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!pass.equals(confirmPass)){
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.
                                        validation_pass_and_confirm_create_acount_no_match),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    firebaseAuth.createUserWithEmailAndPassword(email,pass).
                            addOnCompleteListener(CreateAccountActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()){
                                                String msgErrFirebase = task.getException()
                                                        .getMessage();
                                                Toast.makeText(getApplicationContext(),
                                                        getResources().getString(R.string.
                                                                error_creating_account_on_firebase)
                                                                + " "+ msgErrFirebase,
                                                        Toast.LENGTH_SHORT).show();
                                            }else{
                                                Intent intent = new Intent
                                                        (CreateAccountActivity.this,
                                                                LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                }
            });
        }catch (Exception e){
            System.out.print(e.getCause());
        }

    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
