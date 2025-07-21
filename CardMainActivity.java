package com.example.it404.Flashcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.it404.LoginAndRegister.LoginActivity;
import com.example.it404.LoginAndRegister.ViewProfileActivity;
import com.example.it404.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardMainActivity extends AppCompatActivity implements DeckAdapter.DeckListener{

    private RecyclerView FCRecyclerView;
    private SearchView mainFCSearchView;
    private TextView publicDeckTV, searchTV, backTV, MD;
    private FloatingActionButton FCFloatingActionButton;
    private ImageView searchIV;
    private DeckAdapter deckAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabaseP;
    private DatabaseReference databaseReferenceP;
    private List<List<List<String>>> deckModelArrayList = new ArrayList<List<List<String>>>();

    final List<DeckModel> deckModelss = new ArrayList<DeckModel>();

    DeckModel deckModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_main);


        publicDeckTV = findViewById(R.id.mainPublicDeckTV);

        mainFCSearchView = findViewById(R.id.flashCardMainSV);
        MD = findViewById(R.id.emptyNoticeMD);

        searchIV = findViewById(R.id.mainCardMore);
        backTV = findViewById(R.id.flashCardMainSearchBackTV);



        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIV.setVisibility(View.VISIBLE);
                publicDeckTV.setVisibility(View.VISIBLE);
                mainFCSearchView.clearFocus();
                mainFCSearchView.setQuery("",false);
                mainFCSearchView.setVisibility(View.INVISIBLE);
                backTV.setVisibility(View.INVISIBLE);
            }
        });

        mainFCSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                DeckAdapter.deckSearch = false;
                deckAdapter.getFilter().filter(s);

                return false;
            }
        });



        FCRecyclerView = findViewById(R.id.flashCardMainRV);
        FCFloatingActionButton = findViewById(R.id.flashCardMainFAB);
        FCFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardMainActivity.this, AddCardActivity.class);
                startActivity(intent);
            }
        });
        FCRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(CardMainActivity.this, LoginActivity.class));
            finish();
            return;
        }
        initRecyclerView(FirebaseAuth.getInstance().getCurrentUser());

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
////        FirebaseAuth.getInstance().addAuthStateListener(this);
//        deckAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
////        FirebaseAuth.getInstance().removeAuthStateListener(this);
////        if (deckAdapter != null) {
////            deckAdapter.stopListening();
////        }
//        deckAdapter.notifyDataSetChanged();
//    }

//    @Override
//    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//        if (firebaseAuth.getCurrentUser() == null) {
//            startActivity(new Intent(CardMainActivity.this, LoginActivity.class));
//            finish();
//            return;
//        }
//        initRecyclerView(firebaseAuth.getCurrentUser());
//    }

    private void initRecyclerView(FirebaseUser currentUser) {

        String DeckID = getIntent().getStringExtra("DeckID");


        deckAdapter = new DeckAdapter(CardMainActivity.this, deckModelss);
        FCRecyclerView.setLayoutManager(new LinearLayoutManager(CardMainActivity.this));
        FCRecyclerView.setAdapter(deckAdapter);


        Query query = FirebaseDatabase.getInstance().getReference("Decks/"+currentUser.getUid())
                .orderByChild("title");

//        query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                DataSnapshot dataSnapshot = task.getResult();
//
//                deckModel = dataSnapshot.getValue(DeckModel.class);
//
//                deckAdapter = new DeckAdapter(CardMainActivity.this, (List<DeckModel>) deckModel);
//                FCRecyclerView.setLayoutManager(new LinearLayoutManager(CardMainActivity.this));
//                FCRecyclerView.setAdapter(deckAdapter);
//            }
//        });

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for (DataSnapshot child : snapshot.getChildren()) {
                        deckModel = child.getValue(DeckModel.class);
                        deckModelss.add(deckModel);
                    }

                    deckAdapter.notifyDataSetChanged();
                }else{
                    MD.setVisibility(View.VISIBLE);
                }



//                Map<String,String> td = (HashMap<String, String>)snapshot.getValue();
//                deckModel =new DeckModel();
//                deckModel.getTitle();
//                deckModel.getCourseCode();
//                td.get(deckModel);
//                deckModelss.add(td);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void delInitRecyclerView(FirebaseUser currentUser) {


        Query query = FirebaseDatabase.getInstance().getReference("Decks/"+currentUser.getUid())
                .orderByChild("title");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()){
                    MD.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void DeckClick(int position, DeckModel model) {
        Intent intent = new Intent(CardMainActivity.this, ViewCardActivity.class);
        intent.putExtra("DeckTitle", model.getTitle());
        intent.putExtra("DeckCards", model);
        startActivity(intent);
    }

    @Override
    public void DeckDelete(int position, DeckModel deckModel) {
        String DeckID = deckModel.getDeckID();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Decks/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+DeckID);

        firebaseDatabaseP = FirebaseDatabase.getInstance();
        databaseReferenceP = firebaseDatabaseP.getReference("PublicDeck/"+DeckID);

        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    databaseReferenceP.removeValue();

                    delInitRecyclerView(FirebaseAuth.getInstance().getCurrentUser());

                }
            }
        });

    }

//    private void mainFCSearch(String searchText){
//        Query query = FirebaseDatabase.getInstance().getReference("Decks/"+FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .orderByChild("courseCode").startAt(searchText).endAt(searchText+"\uf8ff");
//
//        FirebaseRecyclerOptions<DeckModel> options = new FirebaseRecyclerOptions.Builder<DeckModel>()
//                .setQuery(query, DeckModel.class)
//                .build();
//        deckAdapter = new DeckAdapter(options, this);
//        FCRecyclerView.setLayoutManager(new LinearLayoutManager(CardMainActivity.this));
//        FCRecyclerView.setAdapter(deckAdapter);
//        deckAdapter.startListening();
//    }

    public void OpenMenu(View view){
        PopupMenu menu = new PopupMenu(CardMainActivity.this, view);
        menu.getMenu().add("Search").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                searchMeth();

                return false;
            }
        });

        menu.getMenu().add("Public Deck").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                PublicDeck();

                return false;
            }
        });

        menu.getMenu().add("Profile").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Profile();

                return false;
            }
        });

        menu.getMenu().add("Logout").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Logout();

                return false;
            }
        });

        menu.show();
    }

    private void searchMeth(){
        searchIV.setVisibility(View.INVISIBLE);
        publicDeckTV.setVisibility(View.INVISIBLE);
        mainFCSearchView.setVisibility(View.VISIBLE);
        mainFCSearchView.setIconifiedByDefault(true);
        mainFCSearchView.setIconified(false);
        mainFCSearchView.setFocusable(true);
        mainFCSearchView.requestFocusFromTouch();
        backTV.setVisibility(View.VISIBLE);
    }

    private void PublicDeck(){
        Intent intent = new Intent(CardMainActivity.this, PublicMainActivity.class);
        startActivity(intent);
    }

    private void Profile(){
        Intent intent = new Intent(CardMainActivity.this, ViewProfileActivity.class);
        startActivity(intent);
    }

    private void Logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(CardMainActivity.this, LoginActivity.class));
        finish();
    }

}