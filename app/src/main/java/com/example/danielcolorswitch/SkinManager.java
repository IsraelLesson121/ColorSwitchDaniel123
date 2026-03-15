package com.example.danielcolorswitch;

import android.content.Context;
import android.content.SharedPreferences;

/*
 מחלקה שמנהלת את הסקינים במשחק
 היא שומרת איזה סקין נבחר ומאפשרת לקרוא אותו במשחק
 */

public class SkinManager {

    private static final String PREF_NAME = "skins_data";
    private static final String KEY_SELECTED_SKIN = "selected_skin";

    // שמירת הסקין שנבחר
    public static void saveSelectedSkin(Context context, int skinId) {

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(KEY_SELECTED_SKIN, skinId);

        editor.apply();
    }

    // החזרת הסקין שנבחר
    public static int getSelectedSkin(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // אם לא נבחר סקין נחזיר סקין 1
        return prefs.getInt(KEY_SELECTED_SKIN, 1);
    }
}