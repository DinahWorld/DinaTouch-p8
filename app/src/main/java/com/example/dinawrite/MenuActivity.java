package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton voice;
    ImageButton game;
    ImageButton search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        voice = findViewById(R.id.btnVoice);
        game = findViewById(R.id.btnGame);
        search = findViewById(R.id.btnSearch);

        voice.setOnClickListener(this);
        game.setOnClickListener(this);
        search.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == this.voice) {
            Intent myIntent = new Intent(MenuActivity.this, VoiceActivity.class);
            startActivity(myIntent);
        }
        else if (v == this.game) {
            Intent myIntent = new Intent(MenuActivity.this, GameActivity.class);
            startActivity(myIntent);
        }
        else if (v == this.search) {
            Intent myIntent = new Intent(MenuActivity.this, SearchActivity.class);
            startActivity(myIntent);
        }
    }
}