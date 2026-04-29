package com.example.danielcolorswitch;
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

    public static ArrayList<Record> records;
    FB fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView titleText = findViewById(R.id.titleText);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        Button btnInstruction = findViewById(R.id.btnInstruction);
        Button btnStore = findViewById(R.id.btnStore);
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnBestScore = findViewById(R.id.btnBestScore);

        records = new ArrayList<>();
        fb = FB.getInstance();

        btnBestScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NameActivity.class);
                startActivity(intent);
            }
        });


        btnInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InstructionActivity.class);
                startActivity(intent);
            }
        });
        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StarStore.class);
                startActivity(intent);
            }


        });
        String text = "COLOR SWITCH";
        SpannableString spannableString = new SpannableString(text);

        // מערך צבעים: אדום, כחול, ורוד, ירוק, צהוב
        int[] colors = {
                Color.RED,
                Color.BLUE,
                Color.MAGENTA,
                Color.GREEN,
                Color.YELLOW
        };

        Random random = new Random();

        // צביעה של כל אות בצבע אקראי
        for (int i = 0; i < text.length(); i++) {
            int color = colors[random.nextInt(colors.length)];
            spannableString.setSpan(
                    new ForegroundColorSpan(color),
                    i,
                    i + 1,

                          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        titleText.setText(spannableString);
    }

}