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

    // ----- צבעים מותאמים לעיוורון צבעים -----
    int RED_COLOR = Color.RED;
    int BLUE_COLOR = Color.BLUE;

    // צהוב יותר כתום וברור
    int YELLOW_COLOR = Color.rgb(255, 170, 0);

    // ירוק כהה וחזק
    int GREEN_COLOR = Color.rgb(0, 150, 0);

    // ----- כדור -----
    float ballX, ballY;
    float ballRadius = 30;
    float velocity = 0;
    float gravity = 1.2f;
    int ballColor = RED_COLOR;

    int[] colors = {RED_COLOR, BLUE_COLOR, YELLOW_COLOR, GREEN_COLOR};

    // ----- מכשולים -----
    int obstacleCount = 3;
    float[] obstaclesY = new float[obstacleCount];
    boolean[] passed = new boolean[obstacleCount];

    float obstacleX;
    float obstacleRadius = 320;
    float strokeWidth = 16;
    float angle = 0;

    float obstacleSpacing = 1400;

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
            obstaclesY[i] = ballY - 700 - i * obstacleSpacing;
            passed[i] = false;
        }

        paint.setTextSize(80);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        // פיזיקה
        if (!gameOver) {
            velocity += gravity;
            ballY += velocity;
        }

        // פגיעה למטה בלבד
        if (!gameOver && !exploding) {
            if (ballY + ballRadius >= canvas.getHeight()) {
                explode();
            }
        }

        // הזזת מסך
        float middle = canvas.getHeight() / 2f;
        if (ballY < middle) {
            float move = middle - ballY;
            ballY = middle;

            for (int i = 0; i < obstacleCount; i++) {
                obstaclesY[i] += move;
            }
        }

        // ציור מכשולים
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        for (int i = 0; i < obstacleCount; i++) {

            int[] obsColors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

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

        // ציור הכדור
        if (!exploding) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ballColor);
            canvas.drawCircle(ballX, ballY, ballRadius, paint);
        }

        // פיצוץ
        if (exploding) {
            paint.setColor(ballColor);
            paint.setAlpha(explosionAlpha);
            canvas.drawCircle(ballX, ballY, explosionRadius, paint);

            explosionRadius += 8;
            explosionAlpha -= 12;
        }

        // בדיקת פגיעה במכשולים
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
                    if (touchAngle < 90) obstacleColor = RED_COLOR;
                    else if (touchAngle < 180) obstacleColor = YELLOW_COLOR;
                    else if (touchAngle < 270) obstacleColor = BLUE_COLOR;
                    else obstacleColor = GREEN_COLOR;

                    if (obstacleColor != ballColor) {
                        explode();
                    }
                }

                // ניקוד אחרי מעבר מלא
                if (!passed[i] &&
                        ballY + ballRadius < obstaclesY[i] - obstacleRadius) {

                    score++;
                    passed[i] = true;

                    int newColor;
                    do {
                        newColor = colors[(int) (Math.random() * 4)];
                    } while (newColor == ballColor);

                    ballColor = newColor;
                }

                // מחזור מכשול
                if (obstaclesY[i] - obstacleRadius > canvas.getHeight()) {

                    float minY = obstaclesY[0];
                    for (int k = 1; k < obstacleCount; k++) {
                        if (obstaclesY[k] < minY) minY = obstaclesY[k];
                    }

                    obstaclesY[i] = minY - obstacleSpacing;
                    passed[i] = false;
                }
            }
        }

        // ציור ניקוד
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
        if (event.getAction() == MotionEvent.ACTION_DOWN && !gameOver) {
            velocity = -20;
        }
        return true;
    }
}
