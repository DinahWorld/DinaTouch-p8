package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button voice;
    Button game;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        voice = findViewById(R.id.voice);
        game = findViewById(R.id.game);
        search = findViewById(R.id.search);

        voice.setOnClickListener(this);
        game.setOnClickListener(this);
        search.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == this.voice) {
            Intent myIntent = new Intent(MainActivity.this, VoiceActivity.class);
            startActivity(myIntent);
        }
        else if (v == this.game) {
            Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(myIntent);
        }
        else if (v == this.search) {
            Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(myIntent);
        }
    }
}