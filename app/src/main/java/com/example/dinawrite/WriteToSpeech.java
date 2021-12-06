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

public class WriteToSpeech extends View {
    Paint paint;
    Path path;
    public Ink.Builder inkBuilder = Ink.builder();
    public Ink.Stroke.Builder strokeBuilder;
    public DigitalInkRecognitionModel model;
    public DigitalInkRecognizer recognizer;
    public TextToSpeech textToSpeech;
    public RemoteModelManager remoteModelManager;
    DigitalInkRecognitionModelIdentifier modelIdentifier;

    // check si on a téléchargé le model
    public Task<Boolean> checkIsDownload;

    public WriteToSpeech(Context context){
        this(context, null);
    }
    public WriteToSpeech(Context context, AttributeSet attrs) {
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
        //checkIsDownload = remoteModelManager.isModelDownloaded(model);
        //checkIfModelIfDownloaded();


        // Get a recognizer for the language
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



    public void checkIfModelIfDownloaded(){
        checkIsDownload.addOnSuccessListener(
                i -> Log.i(TAG, "Déjà téléchargé"))
                .addOnFailureListener(
                        aVoid -> {
                            downloadModel();
                        }
                        );

    }

    /// Méthode qui check si on possède un model
    public void downloadModel(){
        this.remoteModelManager
                .download(model, new DownloadConditions.Builder().build())
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Model téléchargé"))
                .addOnFailureListener(
                        e -> Log.e(TAG, "Erreur durant le téléchargement du model" + e));
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawPath(path,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos,yPos);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos,yPos);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        addNewTouchEvent(event);
        invalidate();
        return true;
    }
    // Call this each time there is a new event.
    public void addNewTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        long t = System.currentTimeMillis();
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds((t));


        int action = event.getActionMasked();
        switch (action) {
            // Lorsqu'on écrit à la main on ajoute les coordonnées des points du texte
            // à notre strokeBuilder
            case MotionEvent.ACTION_DOWN:
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_MOVE:
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_UP:
                // Lorsque l'utilisateur va enlever son doigt de l'écran, on appelle nos
                // méthodes qui vont reconnaitre le
                this.strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
        }
    }
    // On efface l'écran
    public void clearScreen(){
        path.reset();
        inkBuilder = Ink.builder();
        strokeBuilder = Ink.Stroke.builder();
    }
    public void recognizeScreen(){
        this.inkBuilder.addStroke(this.strokeBuilder.build());
        // This is what to send to the recognizer.
        Ink ink = inkBuilder.build();
        this.recognizer.recognize(ink)
                .addOnSuccessListener(
                        // `result` contains the recognizer's answers as a RecognitionResult.
                        // Logs the text from the top candidate.

                        result -> {
                            Log.i(TAG, result.getCandidates().get(0).getText(),null);

                            String text = result.getCandidates().get(0).getText();
                            this.textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                        })

                .addOnFailureListener(
                        e -> Log.e(TAG, "Error during recognition: " + e));
    }

}
