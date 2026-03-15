package com.example.danielcolorswitch;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
 חנות סקינים
 כאשר השחקן לוחץ על סקין – הוא נשמר כסקין הפעיל במשחק
 */

public class StarStore extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5, btn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_store);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);

        btn1.setOnClickListener(v -> selectSkin(1));
        btn2.setOnClickListener(v -> selectSkin(2));
        btn3.setOnClickListener(v -> selectSkin(3));
        btn4.setOnClickListener(v -> selectSkin(4));
        btn5.setOnClickListener(v -> selectSkin(5));
        btn6.setOnClickListener(v -> selectSkin(6));
    }

    // פעולה ששומרת את הסקין שנבחר
    private void selectSkin(int skinId) {

        SkinManager.saveSelectedSkin(this, skinId);

        Toast.makeText(this, "Skin Selected!", Toast.LENGTH_SHORT).show();
    }
}