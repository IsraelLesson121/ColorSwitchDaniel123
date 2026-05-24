package com.example.danielcolorswitch;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecordsActivity extends AppCompatActivity {

    // הגדרת האדאפטר כמשתנה מחלקה כדי שנוכל לגשת אליו במידת הצורך
    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // EdgeToEdge מאפשר לתצוגה "להימרח" על כל המסך (כולל מתחת לשורת הסטטוס)
        EdgeToEdge.enable(this);

        // קישור הקלאס לקובץ העיצוב XML של מסך השיאים
        setContentView(R.layout.activity_records);

        // הגדרת מאזין שמחשב את שולי המערכת (כמו המגרעת/Notch) כדי שהטקסט לא יתחבא תחתיהם
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // קריאה לפעולת האתחול של הרכיבים
        initialization();
    }

    /**
     * פעולה המרכזת את כל אתחול רכיבי ה-UI והחיבור לאדאפטר
     */
    private void initialization() {
        // 1. מציאת ה-RecyclerView מתוך קובץ ה-XML
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // 2. הגדרת LayoutManager - קובע איך הרשימה תסודר.
        // LinearLayoutManager מסדר את הפריטים בזה אחר זה (אנכית כברירת מחדל)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. יצירת מופע (Instance) חדש של האדאפטר.
        // אנחנו שולחים לו את ה-Context ואת רשימת השיאים שנמצאת ב-MainActivity
        adapter = new RecordAdapter(this, MainActivity.records);

        // 4. חיבור האדאפטר ל-RecyclerView - כאן מתבצע הקישור הסופי
        recyclerView.setAdapter(adapter);
    }
}