package com.example.it404.Flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it404.R;

import java.util.ArrayList;
import java.util.List;

public class ViewCardActivity extends AppCompatActivity {

    private TextView viewCardTV, viewCardTitle;

    private List<List<String>> cards = new ArrayList<>();
    private List currentCard;
    private boolean front = true;
    private static int f = 0, b = 1;
    private DeckModel thisDeck;
    private int inc = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);


        viewCardTV = findViewById(R.id.ViewCardTV);
        viewCardTitle = findViewById(R.id.flashCardViewCardTitle);


        Bundle bundle = getIntent().getExtras();
        String s = getIntent().getStringExtra("DeckTitle");
        if(s != null){
            viewCardTitle.setText(s);
        }

        thisDeck = (DeckModel) getIntent().getSerializableExtra("DeckCards");
        if(thisDeck != null){
            cards = thisDeck.getCards();
            currentCard = cards.get(inc);
            viewCardTV.setText(currentCard.get(f).toString());
            viewCardTV.setTypeface(Typeface.DEFAULT_BOLD);
        }

        viewCardTV.setOnTouchListener(new OnSwipeTouchListener(ViewCardActivity.this){
            @Override
            public void onSwipeLeft () {
                front = true;
                inc++;
                viewCardTV.setBackground(getResources().getDrawable(R.drawable.view_card_bg));
                viewCardTV.setTextColor(Color.parseColor("#e6cc8a"));
                viewCardTV.setTypeface(Typeface.DEFAULT_BOLD);
                try {
                    currentCard = cards.get(inc);
                    viewCardTV.setText(currentCard.get(f).toString());
                } catch(Exception e) {
                    inc = 0;
                    currentCard = cards.get(inc);
                    viewCardTV.setText(currentCard.get(f).toString());
                    Toast.makeText(ViewCardActivity.this,"Done", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSwipeRight () {
                front = true;
                inc--;
                viewCardTV.setBackground(getResources().getDrawable(R.drawable.view_card_bg));
                viewCardTV.setTextColor(Color.parseColor("#e6cc8a"));
                viewCardTV.setTypeface(Typeface.DEFAULT_BOLD);
                try {
                    currentCard = cards.get(inc);
                    viewCardTV.setText(currentCard.get(f).toString());
                } catch (Exception e) {
                    inc = cards.size() - 1;
                    currentCard = cards.get(inc);
                    viewCardTV.setText(currentCard.get(f).toString());
                }
            }
            @Override
            public void onClick(){
                if (front){
                    front = false;
                    viewCardTV.setBackground(getResources().getDrawable(R.drawable.view_card_back));
                    viewCardTV.setTextColor(Color.parseColor("#2e2f4c"));
                    viewCardTV.setText(currentCard.get(b).toString());
                    viewCardTV.setTypeface(Typeface.DEFAULT);

                    Log.d("dhk", currentCard.get(b).toString());
                }
                else{
                    front = true;
                    viewCardTV.setTypeface(Typeface.DEFAULT_BOLD);
                    viewCardTV.setBackground(getResources().getDrawable(R.drawable.view_card_bg));
                    viewCardTV.setTextColor(Color.parseColor("#e6cc8a"));
                    viewCardTV.setText(currentCard.get(f).toString());
                    Log.d("dhk", currentCard.get(f).toString());
                }
            }
        });

    }

    public static class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }
        public void onClick(){
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick();
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

}