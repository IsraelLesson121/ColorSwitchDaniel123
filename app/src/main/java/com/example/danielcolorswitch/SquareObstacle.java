package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * מחלקה המייצגת מכשול מסוג ריבוע.
 * מדגימה את עקרון ה-Polymorphism (רב-צורתיות) - דורסת שיטות ממחלקת האב Obstacle.
 */
public class SquareObstacle extends Obstacle {

    // המרחק ממרכז הריבוע לקודקודים (דומה לרדיוס במעגל)
    float size = 300;

    public SquareObstacle(float x, float y,
                          int red, int yellow, int blue, int green) {
        // שימוש ב-super כדי להעביר את הנתונים לבנאי של מחלקת האב
        super(x, y, red, yellow, blue, green);
    }

    /**
     * שיטת הציור - משתמשת בנוסחאות טריגונומטריות כדי לחשב את מיקום קודקודי הריבוע.
     */
    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE); // ציור קווי מתאר בלבד
        paint.setStrokeWidth(16); // עובי הצלע

        int[] colors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

        // לולאה לציור 4 צלעות הריבוע בנפרד
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);

            // המרת הזווית הנוכחית של המכשול לרדיאנים (נדרש עבור פונקציות sin/cos)
            // angle הוא משתנה במחלקת האב שמתעדכן כל הזמן כדי ליצור סיבוב
            float rad = (float) Math.toRadians(angle + i * 90);

            // חישוב נקודת ההתחלה של הצלע (x1, y1) בעזרת קוסינוס וסינוס
            float x1 = x + size * (float) Math.cos(rad);
            float y1 = y + size * (float) Math.sin(rad);

            // חישוב נקודת הסיום של הצלע (x2, y2) - תמיד במרחק 90 מעלות (PI/2 רדיאנים) מההתחלה
            float x2 = x + size * (float) Math.cos(rad + Math.PI / 2);
            float y2 = y + size * (float) Math.sin(rad + Math.PI / 2);

            // חיבור הנקודות בקו - ציור צלע אחת בצבע הנכון
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }

    /**
     * לוגיקת זיהוי התנגשות (Collision Detection)
     * מחשבת האם הכדור נוגע בצלע והאם הצבעים תואמים.
     */
    @Override
    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        // 1. חישוב המרחק האוקלידי בין מרכז הכדור למרכז הריבוע
        float dx = ballX - x;
        float dy = ballY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // 2. בדיקה האם הכדור נמצא בטווח המגע של צלעות הריבוע (כולל עובי הכדור)
        if (distance > size - ballRadius && distance < size + ballRadius) {

            // 3. שימוש ב-atan2 למציאת הזווית המדויקת של נקודת המגע (ביחס למרכז הריבוע)
            double touchAngle = Math.toDegrees(Math.atan2(dy, dx));
            if (touchAngle < 0) touchAngle += 360; // נרמול לטווח 0-360

            // 4. נרמול הזווית ביחס לסיבוב המכשול (angle) כדי לדעת באיזו צלע פגענו כרגע
            touchAngle = (touchAngle - angle + 360) % 360;

            // 5. קביעת צבע הצלע שנפגעה לפי רבעי הזווית (כל צלע תופסת 90 מעלות)
            int obstacleColor;
            if (touchAngle < 90) obstacleColor = RED_COLOR;
            else if (touchAngle < 180) obstacleColor = YELLOW_COLOR;
            else if (touchAngle < 270) obstacleColor = BLUE_COLOR;
            else obstacleColor = GREEN_COLOR;

            // 6. השוואת צבעים: אם צבע המכשול בנקודת המגע שונה מצבע הכדור -> פסילה (true)
            return obstacleColor != ballColor;
        }
        return false; // אין מגע או שהצבעים תואמים
    }

    @Override
    public float getTopY() {
        // מחזיר את הנקודה הכי גבוהה של המכשול (משמש לניהול ה-offset והשלבים)
        return y - size;
    }
}