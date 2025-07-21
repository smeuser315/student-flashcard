package com.example.it404.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.it404.Flashcard.CardMainActivity;
import com.example.it404.Flashcard.PublicMainActivity;
import com.example.it404.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dmax.dialog.SpotsDialog;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView profileName, profileId, profileMail, profileNo, editProfile;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        profileName = findViewById(R.id.profileName);
        profileId = findViewById(R.id.profileID);
        profileMail = findViewById(R.id.profileEmail);
        profileNo = findViewById(R.id.profileNO);
        editProfile = findViewById(R.id.editProfileTV);

        alertDialog = new SpotsDialog.Builder()
                .setCancelable(false)
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        alertDialog.show();

        FirebaseFirestore.getInstance()
                .collection("Students")
                .document(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        profileName.setText(documentSnapshot.getString("name"));
                        profileId.setText(documentSnapshot.getString(("studentID")));
                        profileMail.setText(documentSnapshot.getString("email"));
                        profileNo.setText(documentSnapshot.getString("contact"));
                        alertDialog.dismiss();
                    }
                });

//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    public void OpenMenus(View view){
        PopupMenu menu = new PopupMenu(ViewProfileActivity.this, view);
        menu.getMenu().add("Edit Profile").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                EditProf();

                return false;
            }
        });

        menu.getMenu().add("Change Password").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ChangePass();

                return false;
            }
        });

        menu.show();
    }

    private void EditProf(){
        Intent intent = new Intent(ViewProfileActivity.this, EditProfileActivity.class);
        intent.putExtra("name", profileName.getText().toString().trim());
        intent.putExtra("ID", profileId.getText().toString().trim());
        intent.putExtra("mail", profileMail.getText().toString().trim());
        intent.putExtra("NO", profileNo.getText().toString().trim());
        startActivity(intent);
    }

    private void ChangePass(){
        Intent intent = new Intent(ViewProfileActivity.this, PasswordChangeActivity.class);
        startActivity(intent);
    }
}