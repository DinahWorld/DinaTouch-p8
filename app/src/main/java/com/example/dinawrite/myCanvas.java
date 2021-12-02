package com.example.dinawrite;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;
import com.google.mlkit.vision.digitalink.Ink;

public class myCanvas extends View {
    Paint paint;
    Path path;
    public Ink.Builder inkBuilder = Ink.builder();
    public Ink.Stroke.Builder strokeBuilder;
    public DigitalInkRecognitionModel model;
    public DigitalInkRecognitionModelIdentifier modelIdentifier;
    public DigitalInkRecognizer recognizer;

    public myCanvas(Context context, AttributeSet attrs) {
        super(context,attrs);
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5f);

        strokeBuilder = Ink.Stroke.builder();

        // Specify the recognition model for a language
        try {
            modelIdentifier =
                    DigitalInkRecognitionModelIdentifier.fromLanguageTag("fr-FR");
        } catch (MlKitException e) {
             Log.e(TAG, "Error  " + e);
            // language tag failed to parse, handle error.
        }

        assert modelIdentifier != null;
        model = DigitalInkRecognitionModel.builder(modelIdentifier).build();
        RemoteModelManager remoteModelManager = RemoteModelManager.getInstance();

        remoteModelManager
                .download(model, new DownloadConditions.Builder().build())
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Model downloaded"))
                .addOnFailureListener(
                        e -> Log.e(TAG, "Error while downloading a model: " + e));

        // Get a recognizer for the language
        recognizer = DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build());

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
        this.addNewTouchEvent(event);
        invalidate();
        return true;
    }
    // Call this each time there is a new event.
    public void addNewTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        long t = System.currentTimeMillis();

        // If your setup does not provide timing information, you can omit the
        // third paramater (t) in the calls to Ink.Point.create
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                this.strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_UP:
                this.strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                this.inkBuilder.addStroke(this.strokeBuilder.build());
                recognizeScreen();
                this.path.reset();
                this.inkBuilder = Ink.builder();
                this.strokeBuilder = Ink.Stroke.builder();
                break;
        }
    }

    public void recognizeScreen(){
        // This is what to send to the recognizer.
        Ink ink = inkBuilder.build();
        recognizer.recognize(ink)
                .addOnSuccessListener(
                        // `result` contains the recognizer's answers as a RecognitionResult.
                        // Logs the text from the top candidate.
                        result -> Log.i(TAG, result.getCandidates().get(0).getText()))
                .addOnFailureListener(
                        e -> Log.e(TAG, "Error during recognition: " + e));
    }

}
