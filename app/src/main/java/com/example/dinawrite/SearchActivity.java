package com.example.dinawrite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    WriteToText writeToSearch;
    ImageButton searchBtn;
    TextView navigation;
    TextView searchText;
    Boolean lock;
    float xPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        navigation = findViewById(R.id.panel);
        searchBtn = findViewById(R.id.searchBtn);
        searchText = findViewById(R.id.searchText);

        lock = false;
        navigation.setOnTouchListener(this);
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == this.searchBtn){
            //On ouvre une fenetre qui va nous permetre de rechercher le texte entré
            writeToSearch = findViewById(R.id.drawing_view);
            String text = writeToSearch.getText();
            searchText.setText(text);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + searchText.getText())));
        }
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
                        writeToSearch = findViewById(R.id.drawing_view);
                        writeToSearch.clearScreen();
                        finish();
                    }// On reconnait ce que l'utilisateur a écrit puis on l'ajoute au textView
                    else if (xPoint < xPos) {
                        writeToSearch = findViewById(R.id.drawing_view);
                        writeToSearch.recognizeScreen();
                        //On remet à jour notre attrinut pour qu'il recupere le texte
                        writeToSearch.clearScreen();

                    }
            }
        }
        return true;
    }
}