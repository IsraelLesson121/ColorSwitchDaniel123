package com.example.danielcolorswitch;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameOverDialog {

    Context context;

    // בנאי המקבל את ה-Context כדי שנוכל להציג את הדיאלוג על המסך
    public GameOverDialog(Context context) {
        this.context = context;
    }

    /**
     * פונקציה להצגת דיאלוג סיום המשחק
     * @param score הציון שהשחקן קיבל
     * @param onRestart פעולה לביצוע במידה ולוחצים על "שחק שוב" (Runnable)
     * @param onExit פעולה לביצוע במידה ולוחצים על "יציאה"
     */
    public void showGameOver(int score, Runnable onRestart, Runnable onExit) {

        // שימוש ב-Builder ליצירת הדיאלוג
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // ===== עיצוב כותרת "GAME OVER" צבעונית =====
        String text = "GAME OVER";
        // SpannableString מאפשר לעצב חלקים שונים בתוך אותה מחרוזת טקסט
        SpannableString span = new SpannableString(text);
        int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN};

        // לולאה שעוברת על כל אות וצובעת אותה בצבע הבא במערך (שימוש במודולו % 4)
        for (int i = 0; i < text.length(); i++) {
            span.setSpan(new ForegroundColorSpan(colors[i % 4]), i, i + 1, 0);
        }

        // יצירת רכיב טקסט עבור הכותרת והגדרת עיצוב (גודל ומרכוז)
        TextView title = new TextView(context);  
        title.setText(span);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);

        builder.setCustomTitle(title); // קביעת הכותרת המעוצבת לדיאלוג

        // ===== הגדרת הודעת הדיאלוג והציון =====
        builder.setMessage("Your Score: " + score + "\n\nמה תרצה לעשות?");

        // כפתור חיובי - שחק שוב
        builder.setPositiveButton("PLAY AGAIN", (d, w) -> {
            // שמירת השיא בענן לפני שמתחילים מחדש
            FB.getInstance().setRecord("daniel", score);
            if (onRestart != null) onRestart.run(); // הפעלת הלוגיקה של איפוס המשחק
        });

        // כפתור שלילי - יציאה
        builder.setNegativeButton("EXIT", (d, w) -> {
            // שמירת השיא בענן לפני היציאה
            FB.getInstance().setRecord("daniel", score);
            if (onExit != null) onExit.run(); // הפעלת הלוגיקה של סגירת המסך
        });

        // מניעת סגירת הדיאלוג בלחיצה מחוץ לחלונית (מכריח את המשתמש לבחור)
        builder.setCancelable(false);

        // יצירה והצגה של הדיאלוג
        AlertDialog dialog = builder.create();
        dialog.show();

        // הגדרת רקע כהה לחלונית הדיאלוג (שימוש ב-Hex Color)
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1E1E1E"))
        );

        // עיצוב הודעת הטקסט המרכזית לאחר שהדיאלוג כבר הוצג
        TextView message = dialog.findViewById(android.R.id.message);
        if (message != null) {
            message.setTextColor(Color.WHITE);
            message.setTextSize(20);
            message.setGravity(Gravity.CENTER);
        }

        // ===== התאמת עיצוב הכפתורים (צבע ומיקום) =====
        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);


        // צביעת הכפתורים למראה ברור (ירוק לניצחון/המשך, אדום לעצירה)
        positive.setTextColor(Color.GREEN);
        negative.setTextColor(Color.RED);
    }
}