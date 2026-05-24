package com.example.danielcolorswitch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * ה-Adapter משמש כגשר בין רשימת הנתונים (מקור המידע) לבין הרכיב הגרפי (RecyclerView).
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.UserViewHolder> {

    private Context context; // הקשר המערכת (Context) שדרוש לניפוח ה-XML
    private List<Record> recordsList; // רשימת אובייקטים מסוג Record המכילה את נתוני השחקנים

    // בנאי (Constructor) שמקבל את הרשימה ואת ה-Context
    public RecordAdapter(Context context, List<Record> recordsList) {
        this.context = context;
        this.recordsList = recordsList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // שלב 1: ניפוח (Inflate) של תבנית השורה הבודדת (custom_layout.xml)
        // המרת קובץ ה-XML לאובייקט View שה-Java יכול לעבוד איתו
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_layout, null);

        // החזרת ה-ViewHolder שיחזיק את הרכיבים של אותה שורה
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // שלב 2: קישור הנתונים (Data Binding)
        // שליפת אובייקט ה-Record המתאים לפי המיקום (position) ברשימה
        Record record = recordsList.get(position);

        // עדכון טקסט השם מתוך האובייקט לתוך רכיב ה-TextView
        holder.tvName.setText(record.getName());

        // עדכון טקסט הניקוד. שימוש ב- ""+ הופך את ה-int למחרוזת (String) כדי שניתן יהיה להציגו
        holder.tvRecord.setText("" + record.getScore());
    }

    @Override
    public int getItemCount() {
        // החזרת מספר הפריטים הכולל ברשימה - קובע כמה שורות יוצגו ב-RecyclerView
        return recordsList.size();
    }

    /**
     * מחלקה פנימית שתפקידה להחזיק (Hold) את רכיבי ה-UI של שורה בודדת.
     * זה משפר את הביצועים כי המערכת לא צריכה לחפש את ה-ID בכל פעם מחדש (findViewById).
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRecord;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            // קישור המשתנים לרכיבים הגרפיים הנמצאים בתוך ה-XML של השורה
            tvName = itemView.findViewById(R.id.tvName);
            tvRecord = itemView.findViewById(R.id.tvScore);
        }
    }
}