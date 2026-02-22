package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SquareObstacle implements Obstacle {

    float x, y;
    float size = 300; // חצי הצלע
    float angle = 0;

    int RED_COLOR, BLUE_COLOR, YELLOW_COLOR, GREEN_COLOR;

    public SquareObstacle(float x, float y,
                          int red, int yellow, int blue, int green) {
        this.x = x;
        this.y = y;
        RED_COLOR = red;
        YELLOW_COLOR = yellow;
        BLUE_COLOR = blue;
        GREEN_COLOR = green;
    }

    @Override
    public void update() {
        angle += 3;
        if (angle >= 360) angle = 0;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(16);

        int[] colors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);
            float rad = (float) Math.toRadians(angle + i * 90);
            float x1 = x + size * (float) Math.cos(rad);
            float y1 = y + size * (float) Math.sin(rad);
            float x2 = x + size * (float) Math.cos(rad + Math.PI / 2);
            float y2 = y + size * (float) Math.sin(rad + Math.PI / 2);
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }

    @Override
    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        float dx = ballX - x;
        float dy = ballY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // בדיקה לפי צבע, מחולק ל-4 רבעים כמו העיגול
        if (distance > size - ballRadius && distance < size + ballRadius) {
            double touchAngle = Math.toDegrees(Math.atan2(dy, dx));
            if (touchAngle < 0) touchAngle += 360;
            touchAngle = (touchAngle - angle + 360) % 360;

            int obstacleColor;
            if (touchAngle < 90) obstacleColor = RED_COLOR;
            else if (touchAngle < 180) obstacleColor = YELLOW_COLOR;
            else if (touchAngle < 270) obstacleColor = BLUE_COLOR;
            else obstacleColor = GREEN_COLOR;

            return obstacleColor != ballColor;
        }
        return false;
    }

    @Override
    public void move(float dy) {
        y += dy;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float newY) {
        y = newY;
    }

    @Override
    public float getTopY() {
        return y - size;
    }
}