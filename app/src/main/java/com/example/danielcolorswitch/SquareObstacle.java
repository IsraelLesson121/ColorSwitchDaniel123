package com.example.danielcolorswitch;

import android.graphics.Canvas;
import android.graphics.Paint;

// יורש מ-Obstacle (עקרון ההורשה)
public class SquareObstacle extends Obstacle {

    // המרחק מהמרכז לכל קודקוד (כמו רדיוס של הריבוע)
    float size = 300;

    public SquareObstacle(float x, float y,
                          int red, int yellow, int blue, int green) {
        // שליחת הפרמטרים למחלקת האב
        super(x, y, red, yellow, blue, green);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(16); // עובי הצלע

        int[] colors = {RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR};

        // לולאה לציור 4 צלעות הריבוע
        for (int i = 0; i < 4; i++) {
            paint.setColor(colors[i]);

            // חישוב זווית הקודקוד הראשון של הצלע (ברדיאנים)
            float rad = (float) Math.toRadians(angle + i * 90);

            // מיקום הקודקוד הראשון (התחלת הקו)
            float x1 = x + size * (float) Math.cos(rad);
            float y1 = y + size * (float) Math.sin(rad);

            // חישוב זווית הקודקוד השני (במרחק 90 מעלות מהראשון)
            float x2 = x + size * (float) Math.cos(rad + Math.PI / 2);
            float y2 = y + size * (float) Math.sin(rad + Math.PI / 2);

            // ציור הקו שמחבר בין שני הקודקודים - זו הצלע
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }

    @Override
    public boolean checkCollision(float ballX, float ballY, float ballRadius, int ballColor) {
        // שלב 1: חישוב מרחק הכדור ממרכז הריבוע
        float dx = ballX - x;
        float dy = ballY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // שלב 2: האם הכדור נמצא באזור הצלעות?
        // (בדיקה אם המרחק דומה ל-size)
        if (distance > size - ballRadius && distance < size + ballRadius) {

            // שלב 3: מציאת זווית המגע בעזרת atan2
            double touchAngle = Math.toDegrees(Math.atan2(dy, dx));
            if (touchAngle < 0) touchAngle += 360;

            // שלב 4: נרמול הזווית לפי סיבוב הריבוע (angle)
            touchAngle = (touchAngle - angle + 360) % 360;

            // שלב 5: קביעת הצבע לפי רבעי המעגל (0-90, 90-180 וכו')
            int obstacleColor;
            if (touchAngle < 90) obstacleColor = RED_COLOR;
            else if (touchAngle < 180) obstacleColor = YELLOW_COLOR;
            else if (touchAngle < 270) obstacleColor = BLUE_COLOR;
            else obstacleColor = GREEN_COLOR;

            // שלב 6: החזרת אמת אם הצבעים שונים (פסילה)
            return obstacleColor != ballColor;
        }
        return false;
    }

    @Override
    public float getTopY() {
        // הנקודה הגבוהה ביותר בריבוע
        return y - size;
    }
}