package com.example.danielcolorswitch;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;

public class Play extends View { // הגדרת המחלקה כיורשת מ-View כדי לצייר גרפיקה אישית

    // --- אתחול משתנים וכלי ציור ---
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // יצירת "מכחול" עם דגל שמחליק קצוות (אנטי-אליאסינג)
    Random random = new Random(); // אובייקט ליצירת מספרים אקראיים (למכשולים וצבעים)

    // מערך צבעים קבוע למשחק
    int[] colors = {Color.RED, Color.BLUE, Color.rgb(255,170,0), Color.rgb(0,150,0)};

    // משתני הכדור (השחקן)
    float ballX, ballY, ballRadius = 30; // מיקום X,Y ורדיוס הכדור
    float velocity = 0, gravity = 1.2f; // מהירות נוכחית וכוח משיכה קבוע (פיזיקה)
    int ballColor = colors[0]; // צבע הכדור ההתחלתי
    int selectedSkin; // משתנה שיכיל את מספר הסקין שנבחר בחנות

    // ניהול המכשולים
    Obstacle[] obstacles = new Obstacle[4]; // מערך שמכיל 4 אובייקטים של מכשולים (פולימורפיזם)
    float obstacleSpacing = 900; // המרחק האנכי בין מכשול למכשול
    int score = 0; // מונה נקודות
    boolean gameOver = false, startPaused = true; // דגלים למצב המשחק (עצור/נגמר)

    // משתני אנימציית הפיצוץ
    boolean exploding = false; // דגל האם יש פיצוץ כרגע
    float explosionRadius = 0; // רדיוס העיגול של הפיצוץ שגדל
    int explosionAlpha = 255; // רמת השקיפות של הפיצוץ (הולכת ונעלמת)

    public Play(Context context) {
        super(context); // קריאה לבנאי של View
        selectedSkin = SkinManager.getSelectedSkin(context); // שליפת הסקין השמור מהזיכרון (SharedPreferences)

        // חישוב מרכז המסך לפי מימדי המכשיר
        ballX = getResources().getDisplayMetrics().widthPixels / 2f;
        ballY = getResources().getDisplayMetrics().heightPixels / 2f;

        initObstacles(); // קריאה לפעולת יצירת המכשולים הראשונית
    }

    private void initObstacles() {
        float currentY = ballY - 700; // התחלת הצבת המכשולים מעל הכדור
        for(int i = 0; i < obstacles.length; i++){
            obstacles[i] = createRandomObstacle(currentY); // יצירת מכשול אקראי במיקום Y
            currentY -= obstacleSpacing; // קידום המיקום למעלה עבור המכשול הבא
        }
    }

    private Obstacle createRandomObstacle(float y){
        int type = random.nextInt(3); // הגרלת מספר בין 0 ל-2 לקביעת סוג המכשול
        if(type == 0) return new CircleObstacle(ballX, y, colors[0], colors[2], colors[1], colors[3]); // יצירת עיגול
        if(type == 1) return new SquareObstacle(ballX, y, colors[0], colors[2], colors[1], colors[3]); // יצירת ריבוע
        return new PlusObstacle(ballX + 180, y, colors[0], colors[2], colors[1], colors[3]); // יצירת פלוס
    }

    @Override
    protected void onDraw(Canvas canvas) { // הפעולה המרכזית שמציירת כל פריים (Frame)
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK); // צביעת כל הרקע בשחור

        // עדכון פיזיקה - נפילת הכדור
        if(!gameOver && !startPaused){
            velocity += gravity; // המהירות גדלה בגלל כוח המשיכה
            ballY += velocity;   // המיקום משתנה לפי המהירות
        }

        // מנגנון גלילה (Scrolling) - הכדור נשאר במרכז והעולם זז למטה
        float middle = canvas.getHeight() / 2f;
        if(ballY < middle){
            float offset = middle - ballY; // חישוב ההפרש בין הכדור למרכז
            ballY = middle; // קיבוע הכדור למרכז המסך
            for(Obstacle obs : obstacles) obs.move(offset); // הזזת כל המכשולים למטה באותו הפרש
        }

        // לולאה לניהול המכשולים
        for(int i = 0; i < obstacles.length; i++){
            obstacles[i].update(); // עדכון זווית הסיבוב של המכשול
            obstacles[i].draw(canvas, paint); // ציור המכשול על הקנבס

            // בדיקת התנגשות (Collision)
            if(!gameOver && obstacles[i].checkCollision(ballX, ballY, ballRadius, ballColor)) triggerGameOver();

            // בדיקת מעבר מכשול - ניקוד ומיחזור
            if (!gameOver && ballY < obstacles[i].getTopY()) {
                score++; // העלאת הניקוד
                changeBallColor(); // החלפת צבע הכדור

                // מציאת המכשול הגבוה ביותר כדי למקם את החדש מעליו
                float minY = ballY;
                for (int j = 0; j < obstacles.length; j++) {
                    if (obstacles[j].getY() < minY) {
                        minY = obstacles[j].getY();
                    }
                }

                // מיחזור: המכשול שעברנו (i) עובר למיקום חדש מעל כולם
                obstacles[i] = createRandomObstacle(minY - obstacleSpacing);
            }
        }

        // ציור הכדור
        paint.setColor(ballColor); // הגדרת צבע המכחול לצבע הכדור
        paint.setStyle(Paint.Style.FILL); // הגדרת ציור מלא (לא רק קווי מתאר)
        drawActiveSkin(canvas); // קריאה לפונקציה שמציירת את הצורה שנבחרה (סקין)

        // ציור אנימציית הפיצוץ
        if(exploding) renderExplosion(canvas);

        // ציור הניקוד על המסך
        paint.setColor(Color.WHITE); // צבע לבן לטקסט
        paint.setTextSize(70); // גודל הטקסט
        canvas.drawText("Score: " + score, 50, 100, paint); // כתיבת הניקוד במיקום קבוע

        // בדיקה אם השחקן נפל מחוץ למסך למטה
        if(!gameOver && ballY > canvas.getHeight()) triggerGameOver();

        invalidate(); // פקודה קריטית: אומרת למערכת לצייר מחדש מיד (יוצרת לולאה)
    }

    private void drawActiveSkin(Canvas canvas){
        switch(selectedSkin){ // בדיקה איזה מספר סקין נבחר
            case 1: canvas.drawCircle(ballX, ballY, ballRadius, paint); break; // ציור עיגול
            case 2: canvas.drawRect(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius, paint); break; // ציור ריבוע
            case 3: // ציור משולש בעזרת נתיב (Path)
                Path p = new Path();
                p.moveTo(ballX, ballY-ballRadius);
                p.lineTo(ballX-ballRadius, ballY+ballRadius);
                p.lineTo(ballX+ballRadius, ballY+ballRadius);
                p.close();
                canvas.drawPath(p, paint);
                break;
            case 4: // ציור יהלום
                Path d = new Path();
                d.moveTo(ballX, ballY-ballRadius);
                d.lineTo(ballX+ballRadius, ballY);
                d.lineTo(ballX, ballY+ballRadius);
                d.lineTo(ballX-ballRadius, ballY);
                d.close();
                canvas.drawPath(d, paint);
                break;
            case 5: // ציור איקס (X) בעזרת שני קווים
                paint.setStrokeWidth(10);
                canvas.drawLine(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius, paint);
                canvas.drawLine(ballX+ballRadius, ballY-ballRadius, ballX-ballRadius, ballY+ballRadius, paint);
                break;
            case 6: // ציור פלוס (+) בעזרת שני מלבנים צרים
                canvas.drawRect(ballX-5, ballY-ballRadius, ballX+5, ballY+ballRadius, paint);
                canvas.drawRect(ballX-ballRadius, ballY-5, ballX+ballRadius, ballY+5, paint);
                break;
        }
    }

    private void renderExplosion(Canvas canvas) {
        paint.setAlpha(explosionAlpha); // הגדרת שקיפות המכחול
        canvas.drawCircle(ballX, ballY, explosionRadius, paint); // ציור עיגול הפיצוץ
        explosionRadius += 15; // הגדלת הרדיוס בכל פריים
        explosionAlpha -= 25; // הפחתת השקיפות בכל פריים (נעלם)
        if(explosionAlpha <= 0) { exploding = false; paint.setAlpha(255); } // סיום האנימציה
    }

    private void changeBallColor(){
        int next;
        do { next = colors[random.nextInt(colors.length)]; }
        while(next == ballColor); // הגרלת צבע חדש עד שהוא יהיה שונה מהצבע הנוכחי
        ballColor = next;
    }

    private void triggerGameOver(){
        if(gameOver) return; // מניעת הרצה כפולה של סיום המשחק
        gameOver = true; // עדכון דגל סיום משחק
        exploding = true; // הפעלת אנימציית הפיצוץ
        explosionRadius = ballRadius;

        postDelayed(() -> { // השהיית הצגת הדיאלוג בחצי שנייה (UX)
            new GameOverDialog(getContext()).showGameOver(score, this::restartGame, this::exitToHome);
        }, 500);
    }

    public void restartGame(){ // איפוס כל המשתנים למצב התחלתי
        ballY = getResources().getDisplayMetrics().heightPixels/2f;
        velocity = 0; ballColor = colors[0]; score = 0;
        gameOver = false; startPaused = true; exploding = false;
        initObstacles(); // יצירת מכשולים חדשים
    }

    private void exitToHome(){ // סגירת ה-Activity וחזרה למסך הראשי
        if(getContext() instanceof android.app.Activity) ((android.app.Activity)getContext()).finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){ // זיהוי נגיעות במסך
        if(event.getAction() == MotionEvent.ACTION_DOWN){ // ברגע הלחיצה
            if(startPaused) startPaused = false; // אם המשחק היה בהמתנה - להתחיל
            if(!gameOver) velocity = -20; // מתן מהירות שלילית (כלפי מעלה) ליצירת קפיצה
        }
        return true;
    }
}