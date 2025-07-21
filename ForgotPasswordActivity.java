package com.example.it404.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it404.Flashcard.CardMainActivity;
import com.example.it404.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailTI;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailTI = findViewById(R.id.autoCompleteTextViewEM);

    }

    public void bp(View view){
        onBackPressed();
    }

    public void submit(View view){
        email = emailTI.getText().toString().trim();

        if (email.isEmpty()){
            emailTI.setError("Please enter an email.");
            emailTI.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTI.setError("Enter a valid email.");
            emailTI.requestFocus();
            return;
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Intent i = new Intent(ForgotPasswordActivity.this, ForgotPasswordSuccessActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(ForgotPasswordActivity.this, "Submission Failed. Please retry.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}