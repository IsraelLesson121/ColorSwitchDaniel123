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

    public GameOverDialog(Context context) {
        this.context = context;
    }

    public void showGameOver(int score, Runnable onRestart, Runnable onExit) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // ===== GAME OVER צבעוני =====
        String text = "GAME OVER";
        SpannableString span = new SpannableString(text);
        int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN};

        for (int i = 0; i < text.length(); i++) {
            span.setSpan(new ForegroundColorSpan(colors[i % 4]), i, i + 1, 0);
        }

        TextView title = new TextView(context);
        title.setText(span);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);

        builder.setCustomTitle(title);

        // ===== טקסט אמצעי =====
        builder.setMessage("Your Score: " + score + "\n\nמה תרצה לעשות?");

        builder.setPositiveButton("PLAY AGAIN", (d, w) -> {
            FB.getInstance().setRecord("daniel", score);
            if (onRestart != null) onRestart.run();
        });

        builder.setNegativeButton("EXIT", (d, w) -> {
            FB.getInstance().setRecord("daniel", score);
            if (onExit != null) onExit.run();
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        // רקע כהה
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1E1E1E"))
        );

        // מירכוז הטקסט
        TextView message = dialog.findViewById(android.R.id.message);
        if (message != null) {
            message.setTextColor(Color.WHITE);
            message.setTextSize(20);
            message.setGravity(Gravity.CENTER);
        }

        // ===== מירכוז כפתורים =====
        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout parent = (LinearLayout) positive.getParent();
        parent.setGravity(Gravity.CENTER);

        positive.setTextColor(Color.GREEN);
        negative.setTextColor(Color.RED);
    }
}
