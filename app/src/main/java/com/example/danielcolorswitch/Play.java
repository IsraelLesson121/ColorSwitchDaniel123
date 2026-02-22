package com.example.danielcolorswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class Play extends View {

    Paint paint = new Paint();

    int RED_COLOR = Color.RED;
    int BLUE_COLOR = Color.BLUE;
    int YELLOW_COLOR = Color.rgb(255, 170, 0);
    int GREEN_COLOR = Color.rgb(0, 150, 0);
    int[] colors = {RED_COLOR, BLUE_COLOR, YELLOW_COLOR, GREEN_COLOR};

    float ballX, ballY;
    float ballRadius = 30;
    float velocity = 0;
    float gravity = 1.2f;
    int ballColor = RED_COLOR;

    CircleObstacle[] circleObstacles;
    int circleCount = 2;

    SquareObstacle[] squareObstacles;
    int squareCount = 1;

    float obstacleSpacing = 1000;

    boolean gameOver = false;
    boolean exploding = false;

    int score = 0;

    float explosionRadius = 0;
    int explosionAlpha = 255;

    public Play(Context context) {
        super(context);

        ballX = getResources().getDisplayMetrics().widthPixels / 2;
        ballY = getResources().getDisplayMetrics().heightPixels / 2;

        circleObstacles = new CircleObstacle[circleCount];
        squareObstacles = new SquareObstacle[squareCount];

        float currentY = ballY - 700;

        // סדר אחד אחרי השני
        for (int i = 0; i < circleCount; i++) {
            circleObstacles[i] = new CircleObstacle(ballX, currentY, RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR);
            currentY -= obstacleSpacing;
        }

        // ריבוע מעט ימינה אך לא יותר מידי
        for (int i = 0; i < squareCount; i++) {
            float squareX = ballX + 100; // רק סטייה קטנה
            squareObstacles[i] = new SquareObstacle(squareX, currentY, RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR);
            currentY -= obstacleSpacing;
        }

        paint.setTextSize(80);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        if (!gameOver) {
            velocity += gravity;
            ballY += velocity;
        }

        if (!gameOver && !exploding) {
            if (ballY + ballRadius >= canvas.getHeight()) explode();
        }

        float middle = canvas.getHeight() / 2f;
        float move = 0;
        if (ballY < middle) {
            move = middle - ballY;
            ballY = middle;
        }

        // --- עדכון עיגולים ---
        for (int i = 0; i < circleCount; i++) {
            circleObstacles[i].update();
            circleObstacles[i].draw(canvas, paint);
            if (!gameOver && !exploding) {
                if (circleObstacles[i].checkCollision(ballX, ballY, ballRadius, ballColor)) explode();
            }
            if (move > 0) circleObstacles[i].move(move);

            if (!gameOver && ballY + ballRadius < circleObstacles[i].y - circleObstacles[i].radius) {
                score++;
                int newColor;
                do { newColor = colors[(int) (Math.random() * colors.length)]; }
                while (newColor == ballColor);
                ballColor = newColor;

                float minY = circleObstacles[0].y;
                for (int k = 1; k < circleCount; k++) if (circleObstacles[k].y < minY) minY = circleObstacles[k].y;
                for (int k = 0; k < squareCount; k++) if (squareObstacles[k].y < minY) minY = squareObstacles[k].y;
                circleObstacles[i].y = minY - obstacleSpacing;
            }
        }

        // --- עדכון ריבועים ---
        for (int i = 0; i < squareCount; i++) {
            squareObstacles[i].update();
            squareObstacles[i].draw(canvas, paint);
            if (!gameOver && !exploding) {
                if (squareObstacles[i].checkCollision(ballX, ballY, ballRadius, ballColor)) explode();
            }
            if (move > 0) squareObstacles[i].move(move);

            if (!gameOver && ballY + ballRadius < squareObstacles[i].y - squareObstacles[i].size) {
                score++;
                int newColor;
                do { newColor = colors[(int) (Math.random() * colors.length)]; }
                while (newColor == ballColor);
                ballColor = newColor;

                float minY = circleObstacles[0].y;
                for (int k = 1; k < circleCount; k++) if (circleObstacles[k].y < minY) minY = circleObstacles[k].y;
                for (int k = 0; k < squareCount; k++) if (squareObstacles[k].y < minY) minY = squareObstacles[k].y;
                squareObstacles[i].y = minY - obstacleSpacing;
            }
        }

        if (!exploding) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ballColor);
            canvas.drawCircle(ballX, ballY, ballRadius, paint);
        }

        if (exploding && explosionAlpha > 0) {
            paint.setColor(ballColor);
            paint.setAlpha(explosionAlpha);
            canvas.drawCircle(ballX, ballY, explosionRadius, paint);
            explosionRadius += 8;
            explosionAlpha -= 12;
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        canvas.drawText("Score: " + score, 50, 100, paint);

        invalidate();
    }

    private void explode() {
        gameOver = true;
        exploding = true;
        explosionRadius = ballRadius;
        explosionAlpha = 255;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gameOver) velocity = -20;
        return true;
    }
}