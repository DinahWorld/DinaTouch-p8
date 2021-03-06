package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class VoiceActivity extends AppCompatActivity implements View.OnTouchListener {
    WriteToText WriteToText;
    TextView navigation;
    Boolean lock;
    float xPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myCanvas = new myCanvas(this,null);
        setContentView(R.layout.write_to_speech);
        navigation = findViewById(R.id.panel);
        lock = false;

        navigation.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == this.navigation) {
            float xPos = event.getX();
            float yPos = event.getY();
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
                    Log.i("yPoint",String.valueOf(this.xPoint));
                    this.lock = false;
                    // On efface l'écran et on revient sur l'écran de menu
                    if (xPoint> xPos) {
                        WriteToText = findViewById(R.id.drawing_view);
                        WriteToText.clearScreen();
                        finish();
                    }// On reconnait ce que l'utilisateur a écrit
                    else if (xPoint < xPos) {
                        WriteToText = findViewById(R.id.drawing_view);
                        WriteToText.recognizeScreen();
                        WriteToText.clearScreen();

                    }
            }
        }
        return true;
    }
}