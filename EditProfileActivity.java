package com.example.it404.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it404.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    private TextView EPName, EPID, EPUpdate;
    private EditText EPMail, EPContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        EPName = findViewById(R.id.editProfileName);
        EPID = findViewById(R.id.editProfileID);
        EPMail = findViewById(R.id.editProfileEmail);
        EPContact = findViewById(R.id.editProfileNO);
        EPUpdate = findViewById(R.id.editProfileUpdateBTN);

        String name = getIntent().getStringExtra("name");
        String ID = getIntent().getStringExtra("ID");
        String mail = getIntent().getStringExtra("mail");
        String NO = getIntent().getStringExtra("NO");


        EPName.setText(name);
        EPID.setText(ID);
        EPMail.setText(mail);
        EPContact.setText(NO);

        EPUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference documentReference = FirebaseFirestore.getInstance()
                        .collection("Students").document("" + ID + "");

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.updateEmail(EPMail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            DocumentReference documentReference = FirebaseFirestore.getInstance()
                                    .collection("Students").document("" + ID + "");

                            documentReference.update("email", EPMail.getText().toString().trim());
                            documentReference.update("contact", EPContact.getText().toString().trim());

                            startActivity(new Intent(EditProfileActivity.this, ViewProfileActivity.class));
                            finish();

                        }
                        else {
                            Toast.makeText(EditProfileActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}