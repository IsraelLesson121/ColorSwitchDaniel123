package com.example.danielcolorswitch;
// ייבוא ספריות דרושות (שימוש ב-Intent למעבר מסכים, Widgets לכפתורים וטקסט, ו-Graphics לצבעים)
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // רשימה סטטית של שיאים שתהיה נגישה מכל מקום באפליקציה
    public static ArrayList<Record> records;
    // אובייקט לניהול מסד הנתונים Firebase
    FB fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // קישור הקוד לקובץ העיצוב (XML)
        setContentView(R.layout.activity_main);

        // מציאת הרכיבים הגרפיים לפי ה-ID שלהם ב-XML
        TextView titleText = findViewById(R.id.titleText);
        Button btnInstruction = findViewById(R.id.btnInstruction);
        Button btnStore = findViewById(R.id.btnStore);
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnBestScore = findViewById(R.id.btnBestScore);

        // הגדרת רקע שחור למסך כולו דרך הקוד
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        // אתחול הרשימה והתחברות ל-Firebase (שימוש ב-Singleton)
        records = new ArrayList<>();
        fb = FB.getInstance();

        // --- הגדרת מאזינים (Listeners) לכפתורים למעבר בין מסכים ---

        // כפתור טבלת שיאים
        btnBestScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent משמש למעבר בין ה-Activity הנוכחית ל-RecordsActivity
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });

        // כפתור התחלת משחק (מעבר למסך הזנת שם)
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NameActivity.class);
                startActivity(intent);
            }
        });

        // כפתור הוראות
        btnInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InstructionActivity.class);
                startActivity(intent);
            }
        });

        // כפתור חנות סקינים
        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StarStore.class);
                startActivity(intent);
            }
        });

        // --- עיצוב כותרת המשחק בצבעים אקראיים (Spannable String) ---

        String text = "COLOR SWITCH";
        // SpannableString מאפשר לעצב חלקים שונים בתוך אותה מחרוזת טקסט
        SpannableString spannableString = new SpannableString(text);

        // מערך צבעים אפשריים לאותיות
        int[] colors = { Color.RED, Color.BLUE, Color.MAGENTA, Color.GREEN, Color.YELLOW };
        Random random = new Random();

        // לולאה שעוברת אות-אות וצובעת אותה בצבע אקראי מהמערך
        for (int i = 0; i < text.length(); i++) {
            int color = colors[random.nextInt(colors.length)];
            // הגדרת ה"Span" (העיצוב): סוג העיצוב (צבע), אינדקס התחלה, אינדקס סיום, ודגל הצמדה
            spannableString.setSpan(
                    new ForegroundColorSpan(color),
                    i,
                    i + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        // הצגת הטקסט המעוצב בתוך ה-TextView
        titleText.setText(spannableString);
    }
}