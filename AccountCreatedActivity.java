package com.example.it404.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.it404.Flashcard.CardMainActivity;
import com.example.it404.R;

public class AccountCreatedActivity extends AppCompatActivity {

    private TextView regAccDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);

        regAccDone = findViewById(R.id.regAccDone);
        regAccDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountCreatedActivity.this, CardMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}