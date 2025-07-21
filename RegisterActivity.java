package com.example.it404.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it404.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText stuID, stuName, email, contact, password, confirmPassword;

    private TextView registerTV;

    private String studID, studName, mail, pass, conpass;

    public static String cont;

    public static String type = "Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        stuID = findViewById(R.id.registerEditTextStudentID);
        stuName = findViewById(R.id.registerEditTextName);
        email = findViewById(R.id.registerEditTextEmailAddress);
        contact = findViewById(R.id.registerEditTextContact);
        password = findViewById(R.id.registerEditTextPassword);
        confirmPassword = findViewById(R.id.registerEditTextConfirmPassword);

        registerTV = findViewById(R.id.registerBtn);
        registerTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.registerBtn:
                registerUser();
                break;
        }
    }

    private void registerUser(){
        studID = stuID.getText().toString().trim();
        studName = stuName.getText().toString().trim();
        mail = email.getText().toString().trim();
        cont = contact.getText().toString().trim();
        pass = password.getText().toString().trim();
        conpass = confirmPassword.getText().toString().trim();

        if (studID.isEmpty()){
            stuID.setError("Please enter a username.");
            stuID.requestFocus();
            return;
        }
        else if (studName.isEmpty()){
            stuName.setError("Please enter a username.");
            stuName.requestFocus();
            return;
        }
        else if (cont.isEmpty()){
            contact.setError("Please enter your contact.");
            contact.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Please enter a valid email.");
            email.requestFocus();
            return;
        }
        else if(pass.isEmpty()){
            password.setError("Please enter a valid password.");
            password.requestFocus();
            return;
        }
        else if(!pass.equals(conpass)){
            password.setError("Passwords must match.");
            password.requestFocus();
            return;
        }
        else if(pass.length() < 6){
            password.setError("Password must be atleast 6 characters.");
            password.requestFocus();
            return;
        }



        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


                            //update username
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(studID).build();
                            firebaseUser.updateProfile(profileUpdates);


                            StudentModel studentModel = new StudentModel(studName, studID, mail, cont, type);


                            FirebaseFirestore.getInstance()
                                    .collection("Students").document(studID).set(studentModel);

                            Intent intent = new Intent(RegisterActivity.this, AccountCreatedActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Failed to register (Authentication)", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

}