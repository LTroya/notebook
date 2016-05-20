package com.ltroya.notebook;

import android.graphics.Color;
import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public class Note {
    private String title, message;
    private long noteId, dateCreateMilli;
    private Category category;

    public enum Category {PERSONAL, TECHNICAL, QUOTE, FINANCE}

    public Note(String title, String message, Category category) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = 0;
        this.dateCreateMilli = 0;
    }

    public Note(String title, String message, Category category, long noteId, long dateCreateMilli) {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = noteId;
        this.dateCreateMilli = dateCreateMilli;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

    public long getId() {
        return this.noteId;
    }

    public long getDateCreateMilli() {
        return this.dateCreateMilli;
    }

    public Category getCategory() {
        return this.category;
    }

    // Android App Development: Easy and Quick Programming torrent
    public TextDrawable getIconResource() {
        return categoryToDrawable(this.category);
    }


    public static TextDrawable categoryToDrawable(Category category) {
        String color;
        String letter = "" + category.toString().charAt(0);

        switch (category) {
            case PERSONAL:
                color = "#F44336";
                break;
            case TECHNICAL:
                color = "#3F51B5";
                break;
            case QUOTE:
                color = "#009688";
                break;
            case FINANCE:
                color = "#607D8B";
                break;
            default:
                color = "#FFEB3B";
        }

        return TextDrawable.builder().buildRound(letter, Color.parseColor(color));
    }
}
