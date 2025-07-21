package com.example.it404.Flashcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.it404.LoginAndRegister.LoginActivity;
import com.example.it404.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicMainActivity extends AppCompatActivity implements PublicDeckAdapter.PublicDeckListener{

    private RecyclerView FCPRecyclerView;
    private PublicDeckAdapter publicDeckAdapter;

    private SearchView publicFCSearchView;
    private TextView searchTV, backTV, pDeck, NPD;

    final List<DeckModel> deckModels = new ArrayList<DeckModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_main);

        FCPRecyclerView = findViewById(R.id.flashCardPublicRV);
        FCPRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        publicFCSearchView = findViewById(R.id.flashCardPublicSV);
        pDeck = findViewById(R.id.mainPDeckTV);
        searchTV = findViewById(R.id.flashCardPublicSearchTV);
        backTV = findViewById(R.id.flashCardPublicSearchBackTV);
        NPD = findViewById(R.id.emptyNoticePD);


        searchTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTV.setVisibility(View.INVISIBLE);
                pDeck.setVisibility(View.INVISIBLE);
                publicFCSearchView.setVisibility(View.VISIBLE);
                publicFCSearchView.setIconifiedByDefault(true);
                publicFCSearchView.setIconified(false);
                publicFCSearchView.setFocusable(true);
                publicFCSearchView.requestFocusFromTouch();
                backTV.setVisibility(View.VISIBLE);
            }
        });

        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTV.setVisibility(View.VISIBLE);
                pDeck.setVisibility(View.VISIBLE);
                publicFCSearchView.setVisibility(View.INVISIBLE);
                publicFCSearchView.setQuery("",false);
                backTV.setVisibility(View.INVISIBLE);
            }
        });

        publicFCSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                PublicDeckAdapter.publicDeckSearch = false;
                publicDeckAdapter.getFilter().filter(s);

                return false;
            }
        });

        initRecyclerView();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseAuth.getInstance().addAuthStateListener(this);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        FirebaseAuth.getInstance().removeAuthStateListener(this);
////        if (publicDeckAdapter != null) {
////            publicDeckAdapter.stopListening();
////        }
//    }
//
//    @Override
//    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//        if (firebaseAuth.getCurrentUser() == null) {
//            startActivity(new Intent(PublicMainActivity.this, LoginActivity.class));
//            finish();
//            return;
//        }
//        initRecyclerView(firebaseAuth.getCurrentUser());
//    }

    private void initRecyclerView() {


        Query query = FirebaseDatabase.getInstance().getReference("PublicDeck")
                .orderByChild("title");

        publicDeckAdapter = new PublicDeckAdapter(PublicMainActivity.this, deckModels);

        FCPRecyclerView.setLayoutManager(new LinearLayoutManager(PublicMainActivity.this));
        FCPRecyclerView.setAdapter(publicDeckAdapter);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //Create new collection<Profile> person;

                    for (DataSnapshot child : snapshot.getChildren()) {
                        DeckModel deckModel = child.getValue(DeckModel.class);
                        deckModels.add(deckModel);
                    }

                    publicDeckAdapter.notifyDataSetChanged();

                }else{
                    NPD.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    @Override
    public void PublicDeckClick(int position, DeckModel model) {
        Intent intent = new Intent(PublicMainActivity.this, ViewCardActivity.class);
        intent.putExtra("DeckTitle", model.getTitle());
        intent.putExtra("DeckCards", model);
        startActivity(intent);
    }


//    private void publicFCSearch(String searchText){
//        Query query = FirebaseDatabase.getInstance().getReference("PublicDeck")
//                .orderByChild("courseCode").startAt(searchText).endAt(searchText+"\uf8ff");
//
//        FirebaseRecyclerOptions<DeckModel> options = new FirebaseRecyclerOptions.Builder<DeckModel>()
//                .setQuery(query, DeckModel.class)
//                .build();
//        publicDeckAdapter = new PublicDeckAdapter(options, this);
//        FCPRecyclerView.setLayoutManager(new LinearLayoutManager(PublicMainActivity.this));
//        FCPRecyclerView.setAdapter(publicDeckAdapter);
//        publicDeckAdapter.startListening();
//    }

}