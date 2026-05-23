package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * מחלקת מכשול בצורת פלוס (+)
 * מוסבר ברמה של פרויקט בגרות: שימוש בהורשה, טריגונומטריה בסיסית ולוגיקה של זוויות.
 */
public class PlusObstacle extends Obstacle {

    float length = 320;      // אורך כל זרוע של הפלוס
    float strokeWidth = 40;  // עובי הקווים

    public PlusObstacle(float x, float y, int red, int yellow, int blue, int green) {
        // קריאה למחלקת האב Obstacle כדי לאתחל מיקום וצבעים
        super(x, y, red, yellow, blue, green);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        int[] colors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

        // לולאה שמציירת את 4 הזרועות
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);

            // חישוב זווית הזרוע הנוכחית (בהתחשב בסיבוב הכללי - angle)
            // Math.toRadians הופך מעלות לרדיאנים כי המחשב עובד ככה
            float rad = (float) Math.toRadians(angle + i * 90);

            // חישוב נקודת הקצה של הזרוע בעזרת סינוס וקוסינוס
            float xEnd = x + length * (float) Math.cos(rad);
            float yEnd = y + length * (float) Math.sin(rad);

            // ציור קו מהמרכז לנקודת הקצה שחישבנו
            canvas.drawLine(x, y, xEnd, yEnd, paint);
        }
    }

    @Override
    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        // 1. חישוב מרחק אווירי בין הכדור למרכז הפלוס (משפט פיתגורס)
        float dx = ballX - x;
        float dy = ballY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // 2. בדיקה: אם הכדור רחוק מהמרכז יותר מאורך הזרוע, הוא בטוח לא נוגע
        if (distance < length + ballRadius) {

            // 3. מציאת הזווית שבה הכדור נמצא יחסית למרכז (בעזרת פונקציית atan2)
            double touchAngle = Math.toDegrees(Math.atan2(dy, dx));
            if (touchAngle < 0) touchAngle += 360; // נרמול הזווית לטווח חיובי (0-360)

            // 4. נרמול הזווית לפי סיבוב המכשול (כדי לדעת איזה צבע נמצא שם עכשיו)
            touchAngle = (touchAngle - angle + 360) % 360;

            // 5. בדיקה: האם זווית המגע קרובה לאחת הזרועות?
            // "Tolerance" הוא טווח השגיאה (מייצג את עובי הזרוע במעלות)
            int hitColor = -1;
            float tolerance = 15;

            if (Math.abs(touchAngle - 0) < tolerance || Math.abs(touchAngle - 360) < tolerance) hitColor = RED_COLOR;
            else if (Math.abs(touchAngle - 90) < tolerance) hitColor = YELLOW_COLOR;
            else if (Math.abs(touchAngle - 180) < tolerance) hitColor = BLUE_COLOR;
            else if (Math.abs(touchAngle - 270) < tolerance) hitColor = GREEN_COLOR;

            // 6. אם נגענו בזרוע - בודקים אם הצבע שלה שונה מצבע הכדור
            if (hitColor != -1) {
                return hitColor != ballColor;
            }
        }

        return false; // אין התנגשות
    }

    @Override
    public float getTopY() {
        return y - length;
    }
}