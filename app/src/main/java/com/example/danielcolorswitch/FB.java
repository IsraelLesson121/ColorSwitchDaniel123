package com.example.danielcolorswitch;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FB {
    // תבנית Singleton: משתנה סטטי ששומר את המופע היחיד של המחלקה בזיכרון
    private static FB instance;

    FirebaseDatabase database; // אובייקט המייצג את מסד הנתונים של Firebase

    // בנאי פרטי (Private Constructor): מונע יצירה של אובייקטים נוספים מחוץ למחלקה
    private FB() {
        database = FirebaseDatabase.getInstance(); // התחברות למסד הנתונים של הפרויקט

        // יצירת שאילתה (Query): מסדרת את המכשולים לפי שדה ה-score ומגבילה ל-15 האחרונים
        Query myQuery = database.getReference("records").orderByChild("score").limitToLast(15);

        // הוספת מאזין (Listener): פונקציה שפועלת בכל פעם שיש שינוי בנתונים בענן
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // ניקוי הרשימה המקומית ב-MainActivity לפני הוספת הנתונים המעודכנים
                MainActivity.records.clear();

                // מעבר בלולאה על כל הילדים (השיאים) שחזרו מהענן
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    // המרת הנתון הגולמי מהענן לאובייקט מסוג Record (Deserilaization)
                    Record currentRecord = userSnapshot.getValue(Record.class);
                    // הוספה לראש הרשימה (אינדקס 0) כדי שהשיא הגבוה ביותר יופיע ראשון
                    MainActivity.records.add(0, currentRecord);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // טיפול במקרה של שגיאה בתקשורת עם השרת
            }
        });
    }

    // פונקציית הגישה של ה-Singleton: מבטיחה שרק אובייקט אחד כזה יהיה קיים (חוסך משאבים)
    public static FB getInstance() {
        if (null == instance) {
            instance = new FB();
        }
        return instance;
    }

    /**
     * פונקציה לשמירת שיא חדש בענן
     */
    public void setRecord(String name, int record)
    {
        // יצירת נתיב חדש תחת "records" עם מזהה ייחודי (push) כדי למנוע דריסת נתונים
        DatabaseReference myRef = database.getReference("records").push();

        // יצירת אובייקט Record חדש עם השם והניקוד שהתקבלו
        Record rec = new Record(name, record);

        // שליחת האובייקט לענן (הפיכתו לפורמט JSON באופן אוטומטי)
        myRef.setValue(rec);
    }
}