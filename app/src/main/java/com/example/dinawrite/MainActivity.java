package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    myCanvas myCanvas;
    TextView panel;
    Boolean lock;
    float xPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //myCanvas = new myCanvas(this,null);
        setContentView(R.layout.activity_main);
        panel = (TextView) findViewById(R.id.panel);
        lock = false;

        panel.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == this.panel) {
            float xPos = event.getX();
            float yPos = event.getY();
            if (this.lock == false) {
                this.xPoint = xPos;
                this.lock = true;
            }

            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("yPoint",String.valueOf(this.xPoint));
                    this.lock = false;
                    if (xPoint> xPos) {
                        myCanvas = findViewById(R.id.drawing_view);
                        myCanvas.clearScreen();
                    }else if (xPoint < xPos) {
                        myCanvas = findViewById(R.id.drawing_view);
                        myCanvas.recognizeScreen();
                        myCanvas.clearScreen();

                    }
            }
        }
        return true;
    }
}