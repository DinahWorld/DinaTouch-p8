package com.example.dinawrite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;


public class Circle {
    public int x;
    public int y;
    public float radius;
    public int initialRadius;
    public Paint paint;
    public static int numberOfCase = 0;
    public static String score = "0";
    public String number = "0";
    public String colorText = "#FFFFFF";
    public boolean changed = false;
    public  int r = 255;
    public  int g = 255;
    public  int b = 255;
    public boolean show;

    public Circle(int x, int y, int radius){
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setColor(Color.parseColor("#FFFFFF"));
        this.paint.setStyle(Paint.Style.FILL);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.initialRadius = radius;
        this.show = false;
    }

    public void drawCase(Canvas canvas){
        if(this.show == true){
            if(this.radius > this.initialRadius){
                this.radius -= 1;
            }else if(this.radius < this.initialRadius){
                this.radius = this.initialRadius;
            }
            this.paint.setColor(Color.rgb(r,g,b));
            canvas.drawCircle( this.x,this.y, this.radius, this.paint);
            this.paint.setColor(Color.parseColor(this.colorText));
            paint.setTextSize(this.radius / 2);
            canvas.drawText(this.number, this.x, this.y, paint);
        }
    }


    public boolean isEmpty(){
        return this.number.equals("0") ? true : false;
    }
    public void addNumber(int n){
        this.r = 84;
        this.g = 81;
        this.b = 168;
        this.show = true;
        this.number = String.valueOf(n);

    }
    public void changeState(Circle anotherCircle){
        this.r = anotherCircle.r;
        this.g = anotherCircle.g;
        this.b = anotherCircle.b;
        this.number = anotherCircle.number;
        this.colorText = anotherCircle.colorText;
        this.changed = false;
        this.show = true;
    }
    public void resetCircle(){
        this.colorText = "#FFFFFF";
        this.r = 255;
        this.g = 255;
        this.b = 255;
        this.number = "0";
        this.show = false;
        this.changed = false;
    }

    public void notChanged(){
        this.changed = false;
    }
    public boolean isChanged(){
        return this.changed == true ? true : false;
    }
    public int getNumber(){
        return Integer.parseInt(this.number);
    }

    //Méthode qui multiplie la valeur de case par 2
    public void multNumber() {
        this.number = String.valueOf(Integer.parseInt(this.number) * 2);
        if (Integer.parseInt(this.number) != 4) {
            //Seuls les nombres apres 4 auront une modification des couleurs
            this.r += 20;
            this.g += 20;
            this.b += 5;
        }
        this.score = String.valueOf(Integer.parseInt(this.score) + Integer.parseInt(this.number));
        this.changed = true;
    }

}
