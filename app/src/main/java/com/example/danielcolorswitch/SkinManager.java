package com.example.danielcolorswitch;

import android.content.Context;
import android.content.SharedPreferences;

/*
 מחלקה שמנהלת את הסקינים במשחק.
 תפקידה: לשמור את בחירת המשתמש בזיכרון הקבוע של הטלפון.
 */
public class SkinManager {

    // שם ה"תיקייה" שבה יישמרו הנתונים בתוך הטלפון
    private static final String PREF_NAME = "skins_data";
    // ה"מפתח" (Key) שדרכו נזהה את הערך של הסקין שנבחר
    private static final String KEY_SELECTED_SKIN = "selected_skin";

    /**
     * פעולה סטטית לשמירת הסקין שנבחר.
     * @param skinId המספר המזהה של הסקין (למשל 1, 2, 3...)
     */
    public static void saveSelectedSkin(Context context, int skinId) {
        // יצירת אובייקט SharedPreferences המאפשר גישה לקובץ הנתונים
        // MODE_PRIVATE אומר שרק האפליקציה שלנו יכולה לקרוא את המידע הזה
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // יצירת אובייקט Editor כדי לבצע שינויים (כתיבה) בקובץ
        SharedPreferences.Editor editor = prefs.edit();

        // הכנסת הנתון: "תחת המפתח KEY_SELECTED_SKIN, תשמור את המספר skinId"
        editor.putInt(KEY_SELECTED_SKIN, skinId);

        // שמירה סופית של השינויים (apply עובד ברקע ולא תוקע את האפליקציה)
        editor.apply();
    }

    /**
     * פעולה סטטית להחזרת הסקין שנבחר מהזיכרון.
     * @return מחזיר את ה-ID של הסקין השמור
     */
    public static int getSelectedSkin(Context context) {
        // גישה לקובץ הנתונים
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // קריאת הנתון: אם המפתח לא קיים (למשל פעם ראשונה שפותחים את המשחק), נחזיר 1 כברירת מחדל
        return prefs.getInt(KEY_SELECTED_SKIN, 1);
    }
}