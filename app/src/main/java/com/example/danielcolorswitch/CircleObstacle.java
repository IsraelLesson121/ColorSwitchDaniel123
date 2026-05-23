package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

// המחלקה יורשת מ-Obstacle (מימוש עקרון ההורשה)
public class CircleObstacle extends Obstacle {

    // מאפייני המעגל
    float radius = 320;        // רדיוס המעגל הגדול
    float strokeWidth = 16;    // עובי הקו של המעגל

    // אובייקט עזר המגדיר את הריבוע שבו המעגל כלוא (דרוש לפקודת הציור drawArc)
    RectF rect = new RectF();

    // בנאי - מקבל מיקום וצבעים ומעביר אותם למחלקת האב (super)
    public CircleObstacle(float x, float y,
                          int red, int yellow, int blue, int green) {
        super(x, y, red, yellow, blue, green);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        // הגדרת סגנון הציור כ"קו חוץ" (ולא מילוי) בעובי שהגדרנו
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        // מערך המרכז את ארבעת צבעי המשחק
        int[] colors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

        // הגדרת גבולות המעגל לפי המרכז (x,y) והרדיוס
        rect.set(x - radius, y - radius, x + radius, y + radius);

        // לולאה המציירת 4 קשתות - כל קשת היא רבע מעגל (90 מעלות)
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]); // בחירת הצבע הנוכחי מהמערך

            // ציור הקשת: (הריבוע התוחם, זווית התחלה, כמה מעלות לצייר, האם לסגור למרכז, כלי הציור)
            // מוסיפים 'angle' כדי שהמעגל יסתובב
            canvas.drawArc(rect, angle + i * 90, 90, false, paint);
        }
    }

    @Override
    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        // שלב 1: חישוב המרחק בין מרכז הכדור למרכז המכשול (משפט פיתגורס)
        float dx = ballX - x;
        float dy = ballY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // שלב 2: בדיקה אם המרחק מתאים לרדיוס של המעגל (האם הכדור נוגע בטבעת?)
        // אנחנו בודקים אם המרחק הוא בטווח שבין "קצת פחות מהרדיוס" ל"קצת יותר מהרדיוס"
        if (distance > radius - strokeWidth / 2 - ballRadius &&
                distance < radius + strokeWidth / 2 + ballRadius) {

            // שלב 3: חישוב הזווית שבה הכדור נוגע במעגל (בעזרת פונקציית atan2)
            double touchAngle = Math.toDegrees(Math.atan2(dy, dx));

            // נרמול הזווית לטווח של 0-360
            if (touchAngle < 0) touchAngle += 360;

            // התחשבות בסיבוב של המכשול (angle) כדי לדעת איזה צבע נמצא שם כרגע
            touchAngle = (touchAngle - angle + 360) % 360;

            // שלב 4: קביעת צבע המכשול בנקודת המגע לפי הזווית
            int obstacleColor;
            if (touchAngle < 90) obstacleColor = RED_COLOR;
            else if (touchAngle < 180) obstacleColor = YELLOW_COLOR;
            else if (touchAngle < 270) obstacleColor = BLUE_COLOR;
            else obstacleColor = GREEN_COLOR;

            // שלב 5: החזרת תוצאת ההתנגשות - אם הצבעים שונים, יש התנגשות (נפסלים)
            return obstacleColor != ballColor;
        }

        // אם המרחק לא בטווח הטבעת, אין התנגשות
        return false;
    }

    @Override
    public float getTopY() {
        // מחזיר את הנקודה הכי גבוהה של המכשול (לצורך חישוב מעבר מכשול וניקוד)
        return y - radius;
    }
}