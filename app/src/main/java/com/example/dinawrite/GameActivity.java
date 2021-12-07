package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {
    TextView navigation;
    Boolean lock;
    float xPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048);
        navigation = findViewById(R.id.navigation);
        navigation.setOnTouchListener(this);
        lock = false;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == this.navigation) {
            float xPos = event.getX();

            if (this.lock == false) {
                this.xPoint = xPos;
                this.lock = true;
            }

            int action = event.getAction();
            // La gestion des commandes seront effectué grâce aux gestes
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("xPos",String.valueOf(xPos));
                    Log.i("xPoint",String.valueOf(xPoint));

                    this.lock = false;
                    // On efface l'écran et on revient sur l'écran de menu
                    if (xPoint > xPos) {
                        finish();
                    }
                    break;
            }
        }
        return true;
    }
}