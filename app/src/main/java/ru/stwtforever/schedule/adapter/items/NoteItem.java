package ru.stwtforever.schedule.adapter.items;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import org.json.JSONObject;

import java.io.Serializable;

public class NoteItem implements Serializable {

    public int id;
    public String title, text;

    public @ColorInt
    int color;

    public NoteItem() {
    }

    public NoteItem(int id, String title, String text, @ColorInt int color) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.color = color;
    }

    public NoteItem(JSONObject o) {
        id = o.optInt("id", 0);
        title = o.optString("title", "");
        text = o.optString("text", "");
        color = o.optInt("color", Color.WHITE);
    }

    public @ColorInt
    int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
