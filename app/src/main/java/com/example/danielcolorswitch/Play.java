package com.example.danielcolorswitch;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/*
 מחלקת המשחק הראשית
 אחראית על:
 - תנועת הכדור
 - מכשולים
 - ניקוד
 - סקינים
 - פיצוץ
 */

public class Play extends View {

    Paint paint = new Paint();
    Paint textPaint = new Paint();

    Random random = new Random();

    // צבעים
    int RED = Color.RED;
    int BLUE = Color.BLUE;
    int YELLOW = Color.rgb(255,170,0);
    int GREEN = Color.rgb(0,150,0);

    int[] colors = {RED, BLUE, YELLOW, GREEN};

    // כדור
    float ballX, ballY;
    float ballRadius = 30;

    float velocity = 0;
    float gravity = 1.2f;

    int ballColor = RED;

    // סקין
    int selectedSkin;

    // מכשולים
    Obstacle[] obstacles;
    int obstacleCount = 4;
    float obstacleSpacing = 900;

    boolean gameOver = false;
    boolean startPaused = true;

    int score = 0;

    // פיצוץ
    boolean exploding = false;
    float explosionRadius = 0;
    int explosionAlpha = 255;

    public Play(Context context) {

        super(context);

        selectedSkin = SkinManager.getSelectedSkin(context);

        ballX = getResources().getDisplayMetrics().widthPixels / 2f;
        ballY = getResources().getDisplayMetrics().heightPixels / 2f;

        obstacles = new Obstacle[obstacleCount];

        float currentY = ballY - 700;

        for(int i=0;i<obstacleCount;i++){
            obstacles[i] = createRandomObstacle(currentY);
            currentY -= obstacleSpacing;
        }

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(70);
        textPaint.setAntiAlias(true);
    }

    private Obstacle createRandomObstacle(float y){

        int type = random.nextInt(3);

        if(type == 0)
            return new CircleObstacle(ballX,y,RED,YELLOW,BLUE,GREEN);

        else if(type == 1)
            return new SquareObstacle(ballX,y,RED,YELLOW,BLUE,GREEN);

        else
            return new PlusObstacle(ballX+180,y,RED,YELLOW,BLUE,GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        // תנועה
        if(!gameOver && !startPaused){
            velocity += gravity;
            ballY += velocity;
        }

        // נפילה
        if(!gameOver && ballY + ballRadius >= canvas.getHeight()){
            explode();
        }

        float middle = canvas.getHeight()/2f;
        float move = 0;

        if(ballY < middle){
            move = middle - ballY;
            ballY = middle;
        }

        for(int i=0;i<obstacleCount;i++){

            obstacles[i].update();
            obstacles[i].draw(canvas,paint);

            if(!gameOver &&
                    obstacles[i].checkCollision(ballX,ballY,ballRadius,ballColor)){
                explode();
            }

            if(move>0)
                obstacles[i].move(move);

            if(!gameOver && ballY + ballRadius < obstacles[i].getTopY()){

                score++;
                changeBallColor();

                float minY = getMinY();
                obstacles[i] = createRandomObstacle(minY - obstacleSpacing);
            }
        }

        // ציור סקין
        paint.setColor(ballColor);
        paint.setStyle(Paint.Style.FILL);

        drawSkin(canvas);

        // פיצוץ
        if(exploding){

            Paint p = new Paint();
            p.setColor(ballColor);
            p.setStyle(Paint.Style.FILL);
            p.setAlpha(explosionAlpha);

            canvas.drawCircle(ballX,ballY,explosionRadius,p);

            explosionRadius += 15;
            explosionAlpha -= 25;

            if(explosionAlpha <= 0)
                exploding = false;
        }

        canvas.drawText("Score: "+score,50,100,textPaint);

        invalidate();
    }

    // 6 סקינים שונים
    private void drawSkin(Canvas canvas){

        if(selectedSkin == 1){
            canvas.drawCircle(ballX,ballY,ballRadius,paint);
        }

        else if(selectedSkin == 2){
            canvas.drawRect(ballX-ballRadius,ballY-ballRadius,
                    ballX+ballRadius,ballY+ballRadius,paint);
        }

        else if(selectedSkin == 3){ // משולש

            Path path = new Path();
            path.moveTo(ballX,ballY-ballRadius);
            path.lineTo(ballX-ballRadius,ballY+ballRadius);
            path.lineTo(ballX+ballRadius,ballY+ballRadius);
            path.close();

            canvas.drawPath(path,paint);
        }

        else if(selectedSkin == 4){ // יהלום

            Path path = new Path();
            path.moveTo(ballX,ballY-ballRadius);
            path.lineTo(ballX+ballRadius,ballY);
            path.lineTo(ballX,ballY+ballRadius);
            path.lineTo(ballX-ballRadius,ballY);
            path.close();

            canvas.drawPath(path,paint);
        }

        else if(selectedSkin == 5){ // X

            canvas.drawLine(ballX-ballRadius,ballY-ballRadius,
                    ballX+ballRadius,ballY+ballRadius,paint);

            canvas.drawLine(ballX+ballRadius,ballY-ballRadius,
                    ballX-ballRadius,ballY+ballRadius,paint);
        }

        else if(selectedSkin == 6){ // פלוס

            canvas.drawRect(ballX-10,ballY-ballRadius,
                    ballX+10,ballY+ballRadius,paint);

            canvas.drawRect(ballX-ballRadius,ballY-10,
                    ballX+ballRadius,ballY+10,paint);
        }
    }

    private void changeBallColor(){

        int newColor;

        do{
            newColor = colors[random.nextInt(colors.length)];
        }while(newColor == ballColor);

        ballColor = newColor;
    }

    private float getMinY(){

        float minY = Float.MAX_VALUE;

        for(int i=0;i<obstacleCount;i++){
            if(obstacles[i].getY() < minY)
                minY = obstacles[i].getY();
        }

        return minY;
    }

    private void explode(){

        if(gameOver) return;

        gameOver = true;
        exploding = true;

        explosionRadius = ballRadius;
        explosionAlpha = 255;

        postDelayed(() -> {

            GameOverDialog dialog =
                    new GameOverDialog(getContext());

            dialog.showGameOver(
                    score,
                    this::restartGame,
                    this::exitToHome
            );

        },500);
    }

    private void restartGame(){

        ballY = getResources().getDisplayMetrics().heightPixels/2f;
        velocity = 0;
        ballColor = RED;
        score = 0;

        gameOver = false;
        startPaused = true;
        exploding = false;

        float currentY = ballY - 700;

        for(int i=0;i<obstacleCount;i++){
            obstacles[i] = createRandomObstacle(currentY);
            currentY -= obstacleSpacing;
        }
    }

    private void exitToHome(){

        if(getContext() instanceof android.app.Activity){
            ((android.app.Activity)getContext()).finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_DOWN){

            if(startPaused)
                startPaused = false;

            if(!gameOver)
                velocity = -20;
        }

        return true;
    }
}