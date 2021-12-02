package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.mlkit.vision.digitalink.Ink;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {
    myCanvas myCanvas;
    private Button translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCanvas = new myCanvas(this,null);
        setContentView(R.layout.activity_main);
        translate = findViewById(R.id.ear);

        translate.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view == this.translate) {
            myCanvas.recognizeScreen();
        }
    }
}