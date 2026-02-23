package com.example.danielcolorswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class Play extends View {

    Paint paint = new Paint();
    Random random = new Random();

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

    // עכשיו כל המכשולים באותו מערך
    Object[] obstacles;
    int obstacleCount = 4;

    float obstacleSpacing = 900;

    boolean gameOver = false;
    boolean exploding = false;

    int score = 0;

    float explosionRadius = 0;
    int explosionAlpha = 255;

    public Play(Context context) {
        super(context);

        ballX = getResources().getDisplayMetrics().widthPixels / 2;
        ballY = getResources().getDisplayMetrics().heightPixels / 2;

        obstacles = new Object[obstacleCount];

        float currentY = ballY - 700;

        for (int i = 0; i < obstacleCount; i++) {
            obstacles[i] = createRandomObstacle(currentY);
            currentY -= obstacleSpacing;
        }

        paint.setTextSize(80);
        paint.setAntiAlias(true);
    }

    // פונקציה שיוצרת מכשול רנדומלי
    private Object createRandomObstacle(float y) {
        int type = random.nextInt(2); // 0 או 1

        if (type == 0) {
            return new CircleObstacle(ballX, y,
                    RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR);
        } else {
            float squareX = ballX + random.nextInt(200); // סטייה קטנה
            return new SquareObstacle(squareX, y,
                    RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR);
        }
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

        for (int i = 0; i < obstacleCount; i++) {

            if (obstacles[i] instanceof CircleObstacle) {

                CircleObstacle circle = (CircleObstacle) obstacles[i];
                circle.update();
                circle.draw(canvas, paint);

                if (!gameOver && !exploding) {
                    if (circle.checkCollision(ballX, ballY, ballRadius, ballColor))
                        explode();
                }

                if (move > 0) circle.move(move);

                if (!gameOver && ballY + ballRadius < circle.y - circle.radius) {
                    score++;
                    changeBallColor();

                    float minY = getMinY();
                    obstacles[i] = createRandomObstacle(minY - obstacleSpacing);
                }

            } else if (obstacles[i] instanceof SquareObstacle) {

                SquareObstacle square = (SquareObstacle) obstacles[i];
                square.update();
                square.draw(canvas, paint);

                if (!gameOver && !exploding) {
                    if (square.checkCollision(ballX, ballY, ballRadius, ballColor))
                        explode();
                }

                if (move > 0) square.move(move);

                if (!gameOver && ballY + ballRadius < square.y - square.size) {
                    score++;
                    changeBallColor();

                    float minY = getMinY();
                    obstacles[i] = createRandomObstacle(minY - obstacleSpacing);
                }
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

    private void changeBallColor() {
        int newColor;
        do {
            newColor = colors[random.nextInt(colors.length)];
        } while (newColor == ballColor);

        ballColor = newColor;
    }

    private float getMinY() {
        float minY = Float.MAX_VALUE;

        for (int i = 0; i < obstacleCount; i++) {

            if (obstacles[i] instanceof CircleObstacle) {
                CircleObstacle c = (CircleObstacle) obstacles[i];
                if (c.y < minY) minY = c.y;
            }

            if (obstacles[i] instanceof SquareObstacle) {
                SquareObstacle s = (SquareObstacle) obstacles[i];
                if (s.y < minY) minY = s.y;
            }
        }

        return minY;
    }

    private void explode() {
        gameOver = true;
        exploding = true;
        explosionRadius = ballRadius;
        explosionAlpha = 255;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gameOver)
            velocity = -20;

        return true;
    }
}