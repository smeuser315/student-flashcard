package com.example.it404.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.it404.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordChangeActivity extends AppCompatActivity {

    private TextInputEditText pass, cPass;

    private String password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        pass = findViewById(R.id.CPP);
        cPass = findViewById(R.id.CPCP);

    }
    public void bp(View view){
        onBackPressed();
    }

    public void CPUpdate(View view){

        password = pass.getText().toString().trim();
        confirmPassword = cPass.getText().toString().trim();

        if(password.isEmpty()){
            pass.setError("Please enter a valid password.");
            pass.requestFocus();
            return;
        }
        else if(!password.equals(confirmPassword)){
            pass.setError("Passwords must match.");
            pass.requestFocus();
            return;
        }
        else if(password.length() < 6){
            pass.setError("Password must be atleast 6 characters.");
            pass.requestFocus();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Intent intent = new Intent(PasswordChangeActivity.this, PasswordChangeSuccessActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(PasswordChangeActivity.this, "Password update, failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}