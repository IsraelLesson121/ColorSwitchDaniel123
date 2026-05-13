package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

public class PlusObstacle extends Obstacle {

    float length = 320;      // אורך זרוע
    float strokeWidth = 40;  // עובי קו

    public PlusObstacle(float x, float y,
                        int red, int yellow, int blue, int green) {
        super(x, y, red, yellow, blue, green);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        int[] colors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);
            float rad = (float) Math.toRadians(angle + i * 90);
            float x2 = x + length * (float) Math.cos(rad);
            float y2 = y + length * (float) Math.sin(rad);
            canvas.drawLine(x, y, x2, y2, paint);
        }
    }

    @Override
    public boolean checkCollision(float ballX, float ballY,
                                  float ballRadius, int ballColor) {
        for (int i = 0; i < 4; i++) {
            float rad = (float) Math.toRadians(angle + i * 90);
            float x1 = x;
            float y1 = y;
            float x2 = x + length * (float) Math.cos(rad);
            float y2 = y + length * (float) Math.sin(rad);

            float A = ballX - x1;
            float B = ballY - y1;
            float C = x2 - x1;
            float D = y2 - y1;

            float dot = A * C + B * D;
            float lenSq = C * C + D * D;
            float param = dot / lenSq;

            float closestX, closestY;
            if (param < 0) {
                closestX = x1;
                closestY = y1;
            } else if (param > 1) {
                closestX = x2;
                closestY = y2;
            } else {
                closestX = x1 + param * C;
                closestY = y1 + param * D;
            }

            float dx = ballX - closestX;
            float dy = ballY - closestY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= ballRadius + strokeWidth / 2) {
                int obstacleColor;
                if (i == 0) obstacleColor = RED_COLOR;
                else if (i == 1) obstacleColor = YELLOW_COLOR;
                else if (i == 2) obstacleColor = BLUE_COLOR;
                else obstacleColor = GREEN_COLOR;

                return obstacleColor != ballColor;
            }
        }
        return false;
    }

    @Override
    public float getTopY() {
        return y - length;
    }
}
