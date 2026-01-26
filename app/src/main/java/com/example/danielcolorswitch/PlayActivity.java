package com.example.danielcolorswitch;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // מציג את המשחק Play.java
        setContentView(new Play(this));
    }
}
