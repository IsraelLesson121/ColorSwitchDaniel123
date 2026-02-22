package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Obstacle {
    void update();
    void draw(Canvas canvas, Paint paint);
    boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor);
    void move(float dy);
    float getY();
    void setY(float newY);
    float getTopY();
}
