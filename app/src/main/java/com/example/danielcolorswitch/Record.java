package com.example.danielcolorswitch;

/**
 * מחלקת POJO (Plain Old Java Object) המייצגת שיא בודד.
 * מחלקה זו משמשת כ"תבנית" להעברת נתונים בין האפליקציה ל-Firebase.
 */
public class Record {
    private String name; // שם השחקן
    private int score;   // הניקוד שהשיג

    // בנאי רגיל לשימוש בתוך הקוד שלנו (למשל כשיוצרים שיא חדש בסוף משחק)
    public Record(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * בנאי ריק (Default Constructor) - חובה עבור Firebase!
     * כש-Firebase מוריד נתונים, הוא יוצר אובייקט ריק ואז "מזריק" אליו את הנתונים.
     * ללא בנאי זה, האפליקציה תקרוס בזמן קריאת הנתונים מהענן.
     */
    public Record() {
    }

    // ===== Getters & Setters =====
    // חובה לייצר אותם עבור Firebase כדי שיוכל לגשת למשתנים הפרטיים (private)
    // ולבצע המרה אוטומטית מפורמט JSON לאובייקט Java.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}