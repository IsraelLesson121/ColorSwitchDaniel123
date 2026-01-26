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

    // הכדור
    float ballX;
    float ballY;
    float ballRadius = 30;

    // פיזיקה
    float velocity = 0;
    float gravity = 1.2f;

    // צבעי הכדור
    int[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};
    int ballColor = Color.RED;

    // המכשול – עיגול מסתובב
    float obstacleRadius = 250; // גדול יותר
    float obstacleX;
    float obstacleY;
    float angle = 0;
    float rotationSpeed = 5;

    // דגל לבדיקה שהכדור עבר את המכשול
    boolean crossedObstacle = false;

    public Play(Context context) {
        super(context);

        ballX = getResources().getDisplayMetrics().widthPixels / 2f;
        ballY = getResources().getDisplayMetrics().heightPixels / 2f;

        obstacleX = ballX;
        obstacleY = ballY - 400; // מעל הכדור בתחילת המשחק
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // רקע שחור
        canvas.drawColor(Color.BLACK);

        // ציור המכשול – 4 צבעים
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);

        int[] obsColors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};
        for (int i = 0; i < 4; i++) {
            paint.setColor(obsColors[i]);
            canvas.drawArc(new RectF(obstacleX - obstacleRadius, obstacleY - obstacleRadius,
                            obstacleX + obstacleRadius, obstacleY + obstacleRadius),
                    angle + i * 90, 90, false, paint);
        }

        angle += rotationSpeed;
        if (angle >= 360) angle = 0;

        // ציור הכדור
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ballColor);
        canvas.drawCircle(ballX, ballY, ballRadius, paint);

        // פיזיקה – קפיצה
        velocity += gravity;
        ballY += velocity;

        // גבול תחתון
        if (ballY > canvas.getHeight() - ballRadius) {
            ballY = canvas.getHeight() - ballRadius;
            velocity = 0;
        }

        // בדיקה אם הכדור עבר את המכשול
        if (!crossedObstacle && ballY - ballRadius < obstacleY + obstacleRadius
                && ballY + ballRadius > obstacleY - obstacleRadius) {

            // כאשר הכדור מגיע למרכז המכשול – שינוי צבע
            if (ballY > obstacleY - 10 && ballY < obstacleY + 10) {
                int newColor;
                do {
                    newColor = colors[(int) (Math.random() * colors.length)];
                } while (newColor == ballColor); // לא לשים אותו צבע שוב
                ballColor = newColor;

                crossedObstacle = true; // מונע שינוי צבע שוב עד לעדכון הבא
            }
        }

        // איפוס הדגל אחרי שהכדור עבר את המכשול לחלוטין
        if (ballY - ballRadius > obstacleY + obstacleRadius) {
            crossedObstacle = false;
        }

        invalidate(); // ציור מחדש
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            velocity = -20; // קפיצה
        }
        return true;
    }
}
