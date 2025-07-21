package com.example.it404.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it404.Flashcard.CardMainActivity;
import com.example.it404.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password;
    private TextView createAcc, login;

    private String lMail, lPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginEditTextEmailAddress);
        password = findViewById(R.id.loginEditTextPassword);

        createAcc = findViewById(R.id.loginCreateAcc);
        createAcc.setOnClickListener(this);
        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(this);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, CardMainActivity.class));
            this.finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.loginCreateAcc:
                OpenRegisterActivity();
                break;

            case R.id.loginBtn:
                loginUser();
                break;
        }
    }

    private void loginUser(){
        lMail = email.getText().toString().trim();
        lPass = password.getText().toString().trim();

        if (lMail.isEmpty()){
            email.setError("Please enter an email.");
            email.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(lMail).matches()){
            email.setError("Enter a valid email.");
            email.requestFocus();
            return;
        }
        else if(lPass.isEmpty()){
            password.setError("Please enter a password.");
            password.requestFocus();
            return;
        }
        else if(lPass.length() < 6){
            password.setError("Invalid password.");
            password.requestFocus();
            return;
        }


        FirebaseAuth.getInstance().signInWithEmailAndPassword(lMail, lPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(LoginActivity.this, CardMainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Incorrect E-mail or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Failed to login. Please retry.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void OpenRegisterActivity(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void OpenForgotPasswordActivity(View view){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}