package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * מחלקת מכשול בצורת פלוס (+)
 * מדגימה שימוש בטריגונומטריה לחישוב קודקודי קצוות (Endpoints).
 */
public class PlusObstacle extends Obstacle {

    float length = 320;      // אורך כל זרוע מהמרכז החוצה
    float strokeWidth = 40;  // עובי הקווים המייצגים את הזרועות

    public PlusObstacle(float x, float y, int red, int yellow, int blue, int green) {
        // קריאה ל-super כדי להשתמש במנגנון המשותף לכל המכשולים (מיקום וצבעים)
        super(x, y, red, yellow, blue, green);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth); // הגדרת עובי הקו לציור הזרוע

        int[] colors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

        // לולאה שמציירת את 4 הזרועות של הפלוס
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);

            // חישוב הזווית הנוכחית של הזרוע (90 מעלות הפרש בין כל אחת)
            // angle הוא משתנה הסיבוב שמתעדכן ב-Update
            float rad = (float) Math.toRadians(angle + i * 90);

            // טריגונומטריה: חישוב נקודת הקצה (xEnd, yEnd) בעזרת רדיוס (length) וזווית
            float xEnd = x + length * (float) Math.cos(rad);
            float yEnd = y + length * (float) Math.sin(rad);

            // ציור קו המתחיל במרכז המכשול (x,y) ומסתיים בקצה שחושב
            canvas.drawLine(x, y, xEnd, yEnd, paint);
        }
    }

    /**
     * לוגיקת זיהוי התנגשות בזרועות הפלוס
     */
    @Override
    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        // 1. בדיקה ראשונית: האם הכדור בכלל נמצא בתוך הטווח המקסימלי של הזרועות?
        float dx = ballX - x;
        float dy = ballY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < length + ballRadius) {

            // 2. מציאת הזווית שבה הכדור נמצא יחסית למרכז המכשול
            double touchAngle = Math.toDegrees(Math.atan2(dy, dx));
            if (touchAngle < 0) touchAngle += 360; // נרמול לטווח חיובי

            // 3. קיזוז זווית הסיבוב של המכשול (angle) כדי לדעת באיזו זרוע המגע מתרחש
            touchAngle = (touchAngle - angle + 360) % 360;

            // 4. הגדרת טווח סבירות (Tolerance) - כיוון שלזרוע יש עובי,
            // המגע לא קורה רק ב-90 מעלות בדיוק, אלא בטווח קטן מסביב.
            int hitColor = -1;
            float tolerance = 15; // טווח המעלות שבו נחשב שנגענו בזרוע

            // 5. בדיקה מול כל אחת מארבע הזרועות (ב-0, 90, 180, 270 מעלות)
            if (Math.abs(touchAngle - 0) < tolerance || Math.abs(touchAngle - 360) < tolerance) hitColor = RED_COLOR;
            else if (Math.abs(touchAngle - 90) < tolerance) hitColor = YELLOW_COLOR;
            else if (Math.abs(touchAngle - 180) < tolerance) hitColor = BLUE_COLOR;
            else if (Math.abs(touchAngle - 270) < tolerance) hitColor = GREEN_COLOR;

            // 6. אם המשתנה hitColor השתנה, סימן שהייתה פגיעה בזרוע מסוימת
            if (hitColor != -1) {
                // בדיקת התאמת צבעים: אם צבע הזרוע שונה מצבע הכדור -> פסילה
                return hitColor != ballColor;
            }
        }

        return false; // אין מגע בזרועות או שהצבעים תואמים
    }

    @Override
    public float getTopY() {
        // מחזיר את הגובה המקסימלי של המכשול (לצורך ניהול זיכרון ו-offset)
        return y - length;
    }
}