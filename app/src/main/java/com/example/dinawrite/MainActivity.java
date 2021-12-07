package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        start = findViewById(R.id.btnStart);
        start.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == this.start) {
            Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(myIntent);
        }
    }
}