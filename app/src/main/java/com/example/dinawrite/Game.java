package com.example.dinawrite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class Game extends View {
    Paint paint;
    DisplayMetrics metrics;
    int[] x ;
    int y;
    int radius;
    Circle[][] circles = new Circle[4][4];
    Circle circle;
    int nbreColl = 4;
    boolean lock;
    //Attribut qui va compter chaque case remplie
    public int verify;
    public float xPoint;
    public float yPoint;
    public int textSize;
    public int textX;
    public int textY;

    /*
     * Cet attribut sera true lorsqu'on aura besoin de verifier s'il y a toujours un mouvement disponible
     * sinon elle sera false
     */
    public boolean check;

    public Game(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.paint = new Paint();

        this.lock = false;
        metrics = context.getResources().getDisplayMetrics();
        int bloc = metrics.widthPixels/5;
        this.x = new int[]{bloc,bloc*2,bloc*3,bloc*4};
        this.radius = (this.x[1] - this.x[0]) /2;
        this.y = metrics.heightPixels/4;
        verify = 0;

        this.circle = new Circle(-1,2,radius);

        for(int i = 0;i < 4;i++){
            for(int j = 0;j< 4;j++){
                circles[i][j] = new Circle(bloc * (j + 1),y,radius);
            }
            this.y += this.radius * 2;
        }
        this.drawRandomCase(2);
        this.check = false;

        this.textSize = this.radius * 2;
        this.textX =  (metrics.widthPixels/2) - this.textSize;
        this.textY =  metrics.heightPixels/6;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        if (this.lock == false) {
            this.xPoint = xPos;
            this.yPoint = yPos;
            this.lock = true;
        }
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                this.lock = false;
                if (check != true){
                    if(xPoint + 200 < xPos){
                        this.movDroite();
                    }else if(xPoint - 200 > xPos){
                        this.movGauche();
                    }else if(yPoint + 200 < yPos){
                        this.movBas();
                    }else if(yPoint - 200 > yPos){
                        this.movHaut();
                    }else{
                        break;
                    }
                    this.drawRandomCase(1);
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        this.paint.setColor(Color.parseColor("#FFFFFF"));
        this.paint.setTextSize(this.textSize);
        canvas.drawText("2048", this.textX , this.textY, paint);
        this.paint.setTextSize(this.textSize/3);
        canvas.drawText("score", 0, this.textY/2, paint);
        canvas.drawText(circle.score, 0, this.textY, paint);

        for(int i = 0;i < 4;i++){
            for(int j = 0;j< 4;j++){
                this.circles[i][j].drawCase(canvas);
            }
        }

        invalidate();

    }

    //Methode qui verifie s'il reste des mouvements disponible dans la partie
    public void checkToutLesMove() {

        /*
         * On met l'attribut check sur true qui va faire en sorte que le programme principale
         * ne bouge pas les differentes cases lors des verifications
         */

        check = true;

        movGauche();
        if (check != false) movDroite();
        if (check != false) movHaut();
        if (check != false) movBas();

        //Si elle est false on arrete la partie
        if (check != false) {

        }
    }


    //Methode qui déplace les cases en bas
    public void movBas() {
        /*
         * Cette methode consiste à envoyer à la methode "programmePrincipale", chaque information du label du tableau
         * Pour que la methode puisse utiliser le label en question
         */
        loop: for (int x = 0; x < nbreColl; x++) {
            for (int y = nbreColl - 2; y >= 0; y--) {
                y = programmePrincipale(y, x, +1, 0, nbreColl - 2, y);
                if (y == -10) break loop;
            }
        }
        reset();
    }
    //Methode qui déplace les cases en haut
    public void movHaut() {

        /*
         * Cette methode consiste à envoyer à la methode "programmePrincipale", chaque information du label du tableau
         * Pour que la methode puisse utiliser le label en question
         */

        loop: for (int x = 0; x < nbreColl; x++) {
            for (int y = 1; y < nbreColl; y++) {
                y = programmePrincipale(y, x, -1, 0, 1, y);
                if (y == -10) break loop;
            }
        }
        reset();
    }
    //Methode qui déplace les cases à gauche
    public void movGauche() {
        Log.i("gauche","d");
        /*
         * Cette methode consiste à envoyer à la methode "programmePrincipale", chaque information du label du tableau
         * Pour que la methode puisse utiliser le label en question
         */

        loop: for (int y = 0; y < nbreColl; y++) {
            for (int x = 1; x < nbreColl; x++) {
                x = programmePrincipale(y, x, 0, -1, 1, x);
                if (x == -10) break loop;
            }
        }
        reset();
    }
    //Methode qui déplace les cases à doite
    public void movDroite() {
        /*
         * Cette methode consiste à envoyer à la methode "programmePrincipale", chaque information du label du tableau
         * Pour que la methode puisse utiliser le label en question
         */
        loop: for (int y = 0; y < nbreColl; y++) {
            for (int x = nbreColl - 2; x >= 0; x--) {
                x = programmePrincipale(y, x, 0, +1, nbreColl - 2, x);
                if (x == -10) break loop;
            }
        }
        reset();
    }

    //On parcourt tout le tableau afin que chaque case soit a un etat "non changé"
    public void reset() {
        for (int y = 0; y < nbreColl; y++) {
            for (int x = 0; x < nbreColl; x++) {
                this.circles[x][y].notChanged();
            }
        }
    }
    //Cette methode va gerer les deplacements des cases
    private int programmePrincipale(int y, int x, int y2, int x2, int limit, int i) {
        /*
         * On recupere la valeur de la case à l'index qu'on a reçu
         * et on recupere aussi le nombre de la case suivante ou precedante
         */
        int number = circles[y][x].getNumber();
        int number2 = circles[y + y2][x + x2].getNumber();
        Log.i("1",String.valueOf(circles[y][x].isEmpty()));
        Log.i("numb",circles[y][x].number);
        //Si la case qu'on a eut n'est pas vide
        if (circles[y][x].isEmpty() != true) {
            /*
             * On verifie si la case precedante ou suivante (en fonction du mouvement)
             * est vide ou pas. Si elle l'est, on deplace la valeur de la case reçu à cette case
             * suivante ou precedante
             *
             * Une fois que l'etat de la case suivante ou precedante ait été modifié
             * Et qu'elle correspond à la case qu'on a reçu, on remet à l'etat initial la case
             *
             * Une fois deplace, on veut aller à l'index où on a deplacer la case
             * Donc en fonction l'attribut limit qui si elle est egale à 1 se situe à gauche
             * On renvoie i - 1 qui va correspondre une fois sortie du programme
             * à l'index de la case suivante
             */

            Log.i("2",String.valueOf(circles[y + y2][x + x2].isEmpty()));
            if (circles[y + y2][x + x2].isEmpty() == true) {

                circles[y + y2][x + x2].changeState(circles[y][x]);
                circles[y][x].resetCircle();

                if (i != limit)
                    return limit == 1 ? i - 2 : i + 2;
            } else if (number == number2 && circles[y + y2][x + x2].isChanged() != true) {
                /*
                 * Si la valeur de la case qu'on a est egale a la case suivante ou precedante,
                 * et que la case suivante ou precedante n'a pas eu de modification
                 * on les additionne
                 */

                //On verifie si le programme veut qu'on les déplace ou pas
                if (check != true) {
                    /*
                     * Une fois que l'etat de la case suivante ou precedante  ait ete modifie
                     * Et qu'elle correspond à la case qu'on a reçu, on remet à l'etat initial la case
                     */

                    circles[y + y2][x + x2].multNumber();
                    circles[y + y2][x + x2].radius *= 1.2;
                    circles[y][x].resetCircle();
                    verify -= 1;


                } else {
                    /*
                     * Si le programme ne veut juste verifier si il peut y avoir des changements
                     * on renvoit false et l'attribut check sera false
                     */
                    check = false;
                    return -10;
                }

            }
        }
        /*
         * En fonction de la valeur de i si elle egale a y on renvoit
         * sinon on renvoi x
         *
         * Elle va permettre au boucle des methodes mouv de parcourir normalement le tableau
         */
        return i == y ? y : x;
    }

    public void drawRandomCase(int limit){
        int numberOfCase = circle.numberOfCase;
        Log.i("TAG",String.valueOf(numberOfCase));
        if(verify != 4*4){
            Random rand = new Random();
            int n = 0;

            while(n != limit){
                //On crée deux variables aléatoires de 0 à au nombres de colonnes
                int randomPos_x = rand.nextInt(4);
                int randomPos_y = rand.nextInt(4);
                Log.i("TAG",String.valueOf(n));


                if (circles[randomPos_y][randomPos_x].isEmpty() == true) {

                    /*
                     * Pour qu'il choississe un chiffre entre 2 et 4 on genere une réponse true ou false
                     * si elle est true, on met le chiffre 2 sinon 4
                     */

                    circles[randomPos_y][randomPos_x].addNumber(rand.nextBoolean() == true ? 2 : 4);
                    circles[randomPos_y][randomPos_x].radius *= 1.2;
                    circles[0][0].numberOfCase += 1;
                    n += 1;
                    verify += 1;
                    /*
                     * Si l'attribut verif est égales aux nombres de cases du jeu
                     * On regarde s'il existe un mouvement disponible
                     */
                    if (numberOfCase == 4*4) checkToutLesMove();
                }
            }
        }
    }

}