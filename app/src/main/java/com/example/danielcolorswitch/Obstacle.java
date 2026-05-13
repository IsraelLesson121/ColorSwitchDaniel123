package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Obstacle {

    protected float x, y;
    protected float angle = 0;

    protected int RED_COLOR, BLUE_COLOR, YELLOW_COLOR, GREEN_COLOR;

    public Obstacle(float x, float y, int red, int yellow, int blue, int green) {
        this.x = x;
        this.y = y;
        this.RED_COLOR = red;
        this.YELLOW_COLOR = yellow;
        this.BLUE_COLOR = blue;
        this.GREEN_COLOR = green;
    }

    public void update() {
        angle += 3;
        if (angle >= 360) angle = 0;
    }

    public void draw(Canvas canvas, Paint paint) {
        // מימוש ברירת מחדל
    }

    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        return false;
    }

    public void move(float dy) {
        y += dy;
    }

    public float getY() {
        return y;
    }

    public void setY(float newY) {
        y = newY;
    }

    public float getTopY() {
        return y;
    }
}
