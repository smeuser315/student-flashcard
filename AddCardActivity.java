package com.example.it404.Flashcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it404.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddCardActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText flashCardTitle, flashCardCourseCode, flashCardFront, flashCardBack;
    private TextView flashCardSave, flashCardAddAnother;
    private ImageView flashCardSaveIV, flashCardAddAnotherIV;
    private AutoCompleteTextView FCPACTV;

    private  List<List<String>> cards = new ArrayList<>();


    private String username, deckID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        String[] privacyType = new String[]{
                "Only me",
                "Public"
        };

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.flashcard_drop_down_privacy_item, privacyType);

        FCPACTV = findViewById(R.id.FCPrivacyACTV);
        FCPACTV.setAdapter(stringArrayAdapter);


        flashCardTitle = findViewById(R.id.flashCardAddTitle);
        flashCardCourseCode = findViewById(R.id.flashCardAddCourseCode);
        flashCardFront = findViewById(R.id.flashCardETFront);
        flashCardBack = findViewById(R.id.flashCardETBack);
        flashCardSave = findViewById(R.id.flashCardTVSave);
        flashCardAddAnother = findViewById(R.id.flashCardTVAddAnother);
        flashCardSaveIV = findViewById(R.id.flashCardIVSave);
        flashCardAddAnotherIV = findViewById(R.id.flashCardIVAddAnother);

        flashCardSaveIV.setOnClickListener(this);
        flashCardSave.setOnClickListener(this);
        flashCardAddAnotherIV.setOnClickListener(this);
        flashCardAddAnother.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        username = user.getDisplayName();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //set title name, deck contents
        }
        else{
            Date date = new Date();
            deckID = String.valueOf(date.getTime());
            deckID = deckID.substring(deckID.length()-9);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.flashCardIVAddAnother:
            case R.id.flashCardTVAddAnother:
                if (!flashCardFront.getText().toString().equals("") && !flashCardBack.getText().toString().equals("")){
                    cards.add(Arrays.asList(new String[]{flashCardFront.getText().toString(), flashCardBack.getText().toString()}));
                    flashCardFront.setText("");
                    flashCardBack.setText("");
                    flashCardFront.requestFocus();
                }
                else{
                    Toast.makeText(this, "Incomplete card." ,Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.flashCardIVSave:
            case R.id.flashCardTVSave:
                //adds the remaining card to the deck:
                if (!flashCardFront.getText().toString().equals("") && !flashCardBack.getText().toString().equals("")){
                    cards.add(Arrays.asList(new String[]{flashCardFront.getText().toString(), flashCardBack.getText().toString()}));
                }

                DeckModel deckModel = new DeckModel(flashCardTitle.getText().toString(),
                        username, FirebaseAuth.getInstance().getUid(),
                        deckID, flashCardCourseCode.getText().toString(), cards);

//                if (cards.size() == 0){
//
//                    Intent i = new Intent(AddCardActivity.this, CardMainActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//
//                }

                String getPrivacyType = FCPACTV.getText().toString();

                if(getPrivacyType.equals("Only me")){
                    if(cards.size() == 0){
                        Toast.makeText(this, "Card is empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        onlyMe(deckModel);
                    }
                }
                else if(getPrivacyType.equals("Public")){
                    if(cards.size() == 0){
                        Toast.makeText(this, "Card is empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        everyOne(deckModel);
                    }
                }
                else{
                    if(cards.size() == 0){
                        Toast.makeText(this, "Card is empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        everyOne(deckModel);
                    }
                }
        }

    }

    private void onlyMe(DeckModel deckModel){
        FirebaseDatabase.getInstance().getReference("Decks")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(deckID).setValue(deckModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Intent i = new Intent(AddCardActivity.this, CardMainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("DeckID", deckID);
                            startActivity(i);

                        }
                        else{
                            Toast.makeText(AddCardActivity.this, "Failed to upload new deck.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void everyOne(DeckModel deckModel){
        FirebaseDatabase.getInstance().getReference("Decks")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(deckID).setValue(deckModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            FirebaseDatabase.getInstance().getReference("PublicDeck").child(deckID).setValue(deckModel);

//                                    FirebaseDatabase.getInstance().getReference("Users").child(deckModel.getUserID()).child("MyDecks").child(deckID).setValue(deckID);

                            Intent i = new Intent(AddCardActivity.this, CardMainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("DeckID", deckID);
                            startActivity(i);

                        }
                        else{
                            Toast.makeText(AddCardActivity.this, "Failed to upload new deck.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}