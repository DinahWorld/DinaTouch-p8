package com.example.dinawrite;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;
import com.google.mlkit.vision.digitalink.Ink;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WriteToText extends View {
    Paint paint;
    Path path;
    public Ink.Builder inkBuilder = Ink.builder();
    public Ink.Stroke.Builder strokeBuilder;
    public DigitalInkRecognitionModel model;
    public DigitalInkRecognizer recognizer;
    public TextToSpeech textToSpeech;
    public RemoteModelManager remoteModelManager;
    DigitalInkRecognitionModelIdentifier modelIdentifier;
    public String text = "";
    // check si on a téléchargé le model
    public Task<Boolean> checkIsDownload;

    public WriteToText(Context context){
        this(context, null);
    }
    public WriteToText(Context context, AttributeSet attrs) {
        super(context,attrs);
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#96BAFF"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5f);
        strokeBuilder = Ink.Stroke.builder();

        // On spécifie quelle langue on veut pour la reconnaissance du texte
        try {
            modelIdentifier =
                    DigitalInkRecognitionModelIdentifier.fromLanguageTag("fr");
        } catch (MlKitException e) {
            Log.e(TAG, "Error  ");
        }
        if (modelIdentifier == null) {
            Log.e(TAG, "Error  ");
        }

        remoteModelManager = RemoteModelManager.getInstance();
        model = DigitalInkRecognitionModel.builder(modelIdentifier).build();
        downloadModel();

        // Un attribut qui nous permettre de reconnaitre
        // le texte pour la langue demandé
        recognizer =
                DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build());

        // Notre syntéthiseur vocal
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!= TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.FRANCE);
                }
            }
        });

    }


    /// Methode qui dessine nos tracé
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawPath(path,paint);
    }


    /// Méthode qui va télécharger toute les ressources nécessaire pour l'utilisation de l'API
    public void downloadModel(){
        this.remoteModelManager
                .download(model, new DownloadConditions.Builder().build())
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Model téléchargé"))
                .addOnFailureListener(
                        e -> Log.e(TAG, "Erreur durant le téléchargement du model" + e));
    }


    /// Methode qui recuperer la position de notre doigt afin de dessiner les lignes
    /// et de construire notre strokeBuilder qui va contenir les tracé afin de pouvoir
    /// reconnaitre le texte
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();
        long t = System.currentTimeMillis();
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds((t));

        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                strokeBuilder.addPoint(Ink.Point.create(xPos, yPos, t));
                path.moveTo(xPos,yPos);
                return true;
            case MotionEvent.ACTION_MOVE:
                strokeBuilder.addPoint(Ink.Point.create(xPos, yPos, t));
                path.lineTo(xPos,yPos);
                break;
            case MotionEvent.ACTION_UP:
                strokeBuilder.addPoint(Ink.Point.create(xPos, yPos, t));
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }


    /// Méthode pour effacer l'écran
    public void clearScreen(){
        path.reset();
        inkBuilder = Ink.builder();
        strokeBuilder = Ink.Stroke.builder();
        //On redessine notre écran
        invalidate();

    }

    /// Méthode pour reconnaitre ce qu'on a écrit
    public void recognizeScreen(){
        this.inkBuilder.addStroke(this.strokeBuilder.build());
        // This is what to send to the recognizer.
        Ink ink = inkBuilder.build();
        this.recognizer.recognize(ink)
                .addOnSuccessListener(
                        // Contient notre texte reconnu
                        result -> {
                            this.text = result.getCandidates().get(0).getText();
                            Log.i(TAG, this.text,null);

                            this.textToSpeech.speak(this.text,TextToSpeech.QUEUE_FLUSH,null);
                        })
                .addOnFailureListener(
                        e -> Log.e(TAG, "Error during recognition: " + e));

    }

    /// Getter pour récuperer le texte
    public String getText(){
        return this.text;
    }

}
