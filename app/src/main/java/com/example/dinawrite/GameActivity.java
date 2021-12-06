package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {
    TextView movement;
    Boolean lock;
    Game game;
    float xPoint = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048);
        movement = findViewById(R.id.movement);
        lock = false;

        movement.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == this.movement){
            float xPos = event.getX();
            float yPos = event.getY();
            Log.i("TAGS",String.valueOf(xPos));
            if (this.lock == false) {
                this.xPoint = xPos;
                this.lock = true;
            }
            game = findViewById(R.id.game);

            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    game.movBas();
            }
        }
        return false;
    }
}