package com.example.danielcolorswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class Play extends View {

    Paint paint = new Paint();

    // ----- כדור -----
    float ballX, ballY;
    float ballRadius = 30;
    float velocity = 0;
    float gravity = 1.2f;
    int ballColor = Color.RED;

    int[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};

    // ----- מכשולים -----
    int obstacleCount = 3;
    float[] obstaclesY = new float[obstacleCount];
    float obstacleX;
    float obstacleRadius = 320;
    float strokeWidth = 16;
    float angle = 0;

    // ----- משחק -----
    boolean gameOver = false;
    boolean exploding = false;

    // ----- ניקוד -----
    int score = 0;

    // ----- פיצוץ -----
    float explosionRadius = 0;
    int explosionAlpha = 255;

    public Play(Context context) {
        super(context);

        ballX = getResources().getDisplayMetrics().widthPixels / 2;
        ballY = getResources().getDisplayMetrics().heightPixels / 2;

        obstacleX = ballX;

        for (int i = 0; i < obstacleCount; i++) {
            obstaclesY[i] = ballY - 700 - i * 1000;
        }

        paint.setTextSize(80);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        // ----- פיזיקה -----
        if (!gameOver) {
            velocity += gravity;
            ballY += velocity;
        }

        // ----- הזזת המסך -----
        float middle = canvas.getHeight() / 2f;
        if (ballY < middle) {
            float move = middle - ballY;
            ballY = middle;

            for (int i = 0; i < obstacleCount; i++) {
                obstaclesY[i] += move;
            }
        }

        // ----- ציור מכשולים -----
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        for (int i = 0; i < obstacleCount; i++) {

            int[] obsColors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};

            for (int j = 0; j < 4; j++) {
                paint.setColor(obsColors[j]);
                canvas.drawArc(
                        new RectF(
                                obstacleX - obstacleRadius,
                                obstaclesY[i] - obstacleRadius,
                                obstacleX + obstacleRadius,
                                obstaclesY[i] + obstacleRadius),
                        angle + j * 90,
                        90,
                        false,
                        paint
                );
            }
        }

        angle += 3;
        if (angle >= 360) angle = 0;

        // ----- ציור הכדור -----
        if (!exploding) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ballColor);
            canvas.drawCircle(ballX, ballY, ballRadius, paint);
        }

        // ----- פיצוץ -----
        if (exploding) {
            paint.setColor(ballColor);
            paint.setAlpha(explosionAlpha);
            canvas.drawCircle(ballX, ballY, explosionRadius, paint);

            explosionRadius += 8;
            explosionAlpha -= 12;
        }

        // ----- בדיקת פגיעה -----
        if (!gameOver && !exploding) {
            for (int i = 0; i < obstacleCount; i++) {

                float dx = ballX - obstacleX;
                float dy = ballY - obstaclesY[i];
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance > obstacleRadius - strokeWidth / 2 - ballRadius &&
                        distance < obstacleRadius + strokeWidth / 2 + ballRadius) {

                    double touchAngle = Math.toDegrees(Math.atan2(dy, dx));
                    if (touchAngle < 0) touchAngle += 360;
                    touchAngle = (touchAngle - angle + 360) % 360;

                    int obstacleColor;
                    if (touchAngle < 90) obstacleColor = Color.RED;
                    else if (touchAngle < 180) obstacleColor = Color.BLUE;
                    else if (touchAngle < 270) obstacleColor = Color.YELLOW;
                    else obstacleColor = Color.GREEN;

                    if (obstacleColor != ballColor) {
                        gameOver = true;
                        exploding = true;
                        explosionRadius = ballRadius;
                        explosionAlpha = 255;
                    }
                }

                // ----- עברנו מכשול -----
                if (obstaclesY[i] - obstacleRadius > canvas.getHeight()) {

                    float minY = obstaclesY[0];
                    for (int k = 1; k < obstacleCount; k++) {
                        if (obstaclesY[k] < minY) minY = obstaclesY[k];
                    }

                    obstaclesY[i] = minY - 1000;

                    // ניקוד עולה
                    score++;

                    int newColor;
                    do {
                        newColor = colors[(int) (Math.random() * 4)];
                    } while (newColor == ballColor);
                    ballColor = newColor;
                }
            }
        }

        // ----- ציור ניקוד -----
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        canvas.drawText("Score: " + score, 50, 100, paint);

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gameOver) {
            velocity = -20;
        }
        return true;
    }
}
