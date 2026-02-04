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
    int obstacleCount = 3;              // כמה מכשולים במסך
    float[] obstaclesY = new float[obstacleCount];
    float obstacleX;
    float obstacleRadius = 250;
    float strokeWidth = 20;
    float angle = 0;

    // ----- משחק -----
    boolean gameOver = false;
    boolean exploding = false;

    // פיצוץ
    float explosionRadius = 0;
    int explosionAlpha = 255;

    public Play(Context context) {
        super(context);

        ballX = getResources().getDisplayMetrics().widthPixels / 2;
        ballY = getResources().getDisplayMetrics().heightPixels / 2;

        obstacleX = ballX;

        // מיקום התחלתי של מכשולים
        for (int i = 0; i < obstacleCount; i++) {
            obstaclesY[i] = ballY - 500 - i * 800;
        }
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

        // ----- המסך עולה -----
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

                // ----- אם עברנו את המכשול – מוחקים ומוסיפים חדש -----
                if (obstaclesY[i] - obstacleRadius > canvas.getHeight()) {

                    // מוצאים את המכשול הכי גבוה
                    float minY = obstaclesY[0];
                    for (int k = 1; k < obstacleCount; k++) {
                        if (obstaclesY[k] < minY) minY = obstaclesY[k];
                    }

                    obstaclesY[i] = minY - 800;

                    // שינוי צבע אחרי מעבר
                    int newColor;
                    do {
                        newColor = colors[(int) (Math.random() * 4)];
                    } while (newColor == ballColor);
                    ballColor = newColor;
                }
            }
        }

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
