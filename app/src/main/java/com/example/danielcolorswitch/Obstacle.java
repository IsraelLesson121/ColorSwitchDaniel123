package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Obstacle {

    // משתני מיקום וזווית שמשותפים לכל המכשולים (עיגול, ריבוע וכו')
    // protected אומר שתתי-הממחלקות (הבנים) יוכלו להשתמש במשתנים האלה ישירות
    protected float x, y;
    protected float angle = 0; // זווית הסיבוב הנוכחית של המכשול

    // משתני הצבעים שישמשו לציור המכשול
    protected int RED_COLOR, BLUE_COLOR, YELLOW_COLOR, GREEN_COLOR;

    // בנאי (Constructor) - מקבל את המיקום ההתחלתי ואת הצבעים מהמערכת
    public Obstacle(float x, float y, int red, int yellow, int blue, int green) {
        this.x = x;
        this.y = y;
        this.RED_COLOR = red;
        this.YELLOW_COLOR = yellow;
        this.BLUE_COLOR = blue;
        this.GREEN_COLOR = green;
    }

    // פונקציית העדכון - גורמת למכשול להסתובב
    // כל פעם שקוראים לה, הזווית גדלה ב-3 מעלות
    public void update() {
        angle += 3;
        if (angle >= 360) angle = 0; // איפוס הזווית אחרי סיבוב מלא
    }

    // פונקציית הציור - כאן היא ריקה כי לכל מכשול (בן) יש צורה שונה
    // הבנים ידרסו (Override) את הפונקציה הזו עם הציור הספציפי שלהם
    public void draw(Canvas canvas, Paint paint) {
    }

    // בדיקת התנגשות - ברירת המחדל היא false
    // כל מכשול בן יממש חישוב מתמטי שונה בתוך הפונקציה הזו
    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        return false;
    }

    // הזזת המכשול על ציר ה-Y (משמש לאפקט הגלילה כשהכדור עולה)
    public void move(float dy) {
        y += dy;
    }

    // Getters ו-Setters לניהול המיקום של המכשול
    public float getY() {
        return y;
    }

    public void setY(float newY) {
        y = newY;
    }

    // מחזיר את הנקודה העליונה ביותר של המכשול (חשוב לחישוב הניקוד)
    public float getTopY() {
        return y;
    }
}