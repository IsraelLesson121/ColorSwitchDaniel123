package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class CircleObstacle extends Obstacle {

    float radius = 320;
    float strokeWidth = 16;
    RectF rect = new RectF();

    public CircleObstacle(float x, float y,
                          int red, int yellow, int blue, int green) {
        super(x, y, red, yellow, blue, green);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        int[] colors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

        rect.set(x - radius, y - radius, x + radius, y + radius);

        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);
            canvas.drawArc(rect, angle + i * 90, 90, false, paint);
        }
    }

    @Override
    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        float dx = ballX - x;
        float dy = ballY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > radius - strokeWidth / 2 - ballRadius &&
                distance < radius + strokeWidth / 2 + ballRadius) {

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
    public float getTopY() {
        return y - radius;
    }
}
