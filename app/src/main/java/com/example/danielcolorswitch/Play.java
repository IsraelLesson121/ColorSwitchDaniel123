package com.example.danielcolorswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class Play extends View {

    Paint paint = new Paint();      // ציור כדור ומכשולים
    Paint textPaint = new Paint();  // ציור ניקוד
    Random random = new Random();

    // צבעים
    int RED_COLOR = Color.RED;
    int BLUE_COLOR = Color.BLUE;
    int YELLOW_COLOR = Color.rgb(255, 170, 0);
    int GREEN_COLOR = Color.rgb(0, 150, 0);
    int[] colors = {RED_COLOR, BLUE_COLOR, YELLOW_COLOR, GREEN_COLOR};

    // נתוני כדור
    float ballX, ballY;
    float ballRadius = 30;
    float velocity = 0;
    float gravity = 1.2f;
    int ballColor = RED_COLOR;

    // מכשולים
    Obstacle[] obstacles;
    int obstacleCount = 4;
    float obstacleSpacing = 900;

    boolean gameOver = false;
    boolean exploding = false;
    boolean startPaused = true; // מצב המתנה ללחיצה ראשונה

    int score = 0;

    float explosionRadius = 0;
    int explosionAlpha = 255;

    // בנאי
    public Play(Context context) {
        super(context);

        ballX = getResources().getDisplayMetrics().widthPixels / 2f;
        ballY = getResources().getDisplayMetrics().heightPixels / 2f;

        obstacles = new Obstacle[obstacleCount];

        float currentY = ballY - 700;

        for (int i = 0; i < obstacleCount; i++) {
            obstacles[i] = createRandomObstacle(currentY);
            currentY -= obstacleSpacing;
        }

        // הגדרות טקסט לניקוד
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(80);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
    }

    // יצירת מכשול רנדומלי
    private Obstacle createRandomObstacle(float y) {
        int type = random.nextInt(3);
        if (type == 0)
            return new CircleObstacle(ballX, y, RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR);
        else if (type == 1)
            return new SquareObstacle(ballX, y, RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR);
        else {
            float offset = 180;
            return new PlusObstacle(ballX + offset, y, RED_COLOR, YELLOW_COLOR, BLUE_COLOR, GREEN_COLOR);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK); // רקע

        // ===== פיזיקה של הכדור =====
        if (!gameOver && !startPaused) { // אם לא במשחק נעצר ומחכה ללחיצה ראשונה
            velocity += gravity;
            ballY += velocity;
        }

        // נגיעה ברצפה
        if (!gameOver && ballY + ballRadius >= canvas.getHeight())
            explode();

        float middle = canvas.getHeight() / 2f;
        float move = 0;

        // הזזת המסך אם הכדור עבר את האמצע
        if (ballY < middle) {
            move = middle - ballY;
            ballY = middle;
        }

        // ===== עדכון מכשולים =====
        for (int i = 0; i < obstacleCount; i++) {
            obstacles[i].update();
            obstacles[i].draw(canvas, paint);

            // בדיקת התנגשות
            if (!gameOver && obstacles[i].checkCollision(ballX, ballY, ballRadius, ballColor))
                explode();

            if (move > 0)
                obstacles[i].move(move);

            // אם הכדור עבר את המכשול
            if (!gameOver && ballY + ballRadius < obstacles[i].getTopY()) {
                score++;
                changeBallColor();
                float minY = getMinY();
                obstacles[i] = createRandomObstacle(minY - obstacleSpacing);
            }
        }

        // ציור כדור רגיל
        if (!exploding) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ballColor);
            canvas.drawCircle(ballX, ballY, ballRadius, paint);
        }

        // אנימציית פיצוץ
        if (exploding && explosionAlpha > 0) {
            paint.setColor(ballColor);
            paint.setAlpha(explosionAlpha);
            canvas.drawCircle(ballX, ballY, explosionRadius, paint);
            explosionRadius += 8;
            explosionAlpha -= 12;
        }

        // ציור ניקוד
        canvas.drawText("Score: " + score, 50, 100, textPaint);

        invalidate(); // רענון מתמיד
    }

    // שינוי צבע הכדור
    private void changeBallColor() {
        int newColor;
        do {
            newColor = colors[random.nextInt(colors.length)];
        } while (newColor == ballColor);
        ballColor = newColor;
    }

    // מציאת המכשול העליון ביותר
    private float getMinY() {
        float minY = Float.MAX_VALUE;
        for (int i = 0; i < obstacleCount; i++) {
            if (obstacles[i].getY() < minY)
                minY = obstacles[i].getY();
        }
        return minY;
    }

    // סיום משחק והפעלת פיצוץ
    private void explode() {
        if (gameOver) return;

        gameOver = true;
        exploding = true;
        explosionRadius = ballRadius;
        explosionAlpha = 255;

        // הצגת חלון Game Over אחרי חצי שנייה
        postDelayed(() -> {
            GameOverDialog dialog = new GameOverDialog(getContext());
            dialog.showGameOver(
                    score,
                    this::restartGame,
                    this::exitToHome
            );
        }, 500);
    }

    // אתחול משחק מחדש
    private void restartGame() {
        ballY = getResources().getDisplayMetrics().heightPixels / 2f;
        velocity = 0;
        ballColor = RED_COLOR;
        score = 0;
        gameOver = false;
        exploding = false;
        startPaused = true; // חוזר למצב המתנה ללחיצה ראשונה

        float currentY = ballY - 700;
        for (int i = 0; i < obstacleCount; i++) {
            obstacles[i] = createRandomObstacle(currentY);
            currentY -= obstacleSpacing;
        }
    }

    // חזרה למסך הבית
    private void exitToHome() {
        if (getContext() instanceof android.app.Activity) {
            ((android.app.Activity) getContext()).finish();
        }
    }

    // קפיצה בלחיצה
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (startPaused) {
                // לחיצה ראשונה מתחילה את המשחק
                startPaused = false;
            }

            if (!gameOver) {
                velocity = -20; // קפיצה של הכדור
            }
        }
        return true;
    }
}