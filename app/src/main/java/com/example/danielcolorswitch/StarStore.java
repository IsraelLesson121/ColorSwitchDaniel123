package com.example.danielcolorswitch;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/*
 חנות סקינים:
 מסך זה מאפשר למשתמש לבחור את המראה של הכדור במשחק.
 */
public class StarStore extends AppCompatActivity {

    // הגדרת משתנים לכפתורים (כל כפתור מייצג סקין אחר)
    Button btn1, btn2, btn3, btn4, btn5, btn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_store);

        // קישור הכפתורים לקובץ ה-XML (מציאת הרכיבים הגרפיים)
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);

        // הגדרת מאזינים (Listeners) ללחיצה.
        // בחרת להשתמש ב"ביטוי למדא" (v -> ...) - דרך קצרה ומודרנית לכתוב OnClickListener.
        btn1.setOnClickListener(v -> selectSkin(1));
        btn2.setOnClickListener(v -> selectSkin(2));
        btn3.setOnClickListener(v -> selectSkin(3));
        btn4.setOnClickListener(v -> selectSkin(4));
        btn5.setOnClickListener(v -> selectSkin(5));
        btn6.setOnClickListener(v -> selectSkin(6));
    }

    /**
     * פעולה המופעלת בעת לחיצה על כפתור סקין.
     * @param skinId מספר המזהה של הסקין שנבחר
     */
    private void selectSkin(int skinId) {
        // קריאה לפעולה סטטית במחלקת SkinManager שתשמור את הבחירה
        // אנחנו מעבירים את 'this' (הקשר/Context) כדי שהמנהל יוכל לגשת לזיכרון המכשיר
        SkinManager.saveSelectedSkin(this, skinId);

        // הצגת הודעה קופצת (Toast) למשתמש לאישור הבחירה
        Toast.makeText(this, "Skin Selected!", Toast.LENGTH_SHORT).show();
    }
}