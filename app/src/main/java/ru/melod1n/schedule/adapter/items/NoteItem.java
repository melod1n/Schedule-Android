package ru.melod1n.schedule.adapter.items;

import org.json.JSONObject;

import java.io.Serializable;

public class NoteItem implements Serializable {

    public int id;
    public String title, text;

    public int position = -1;

    public NoteItem() {
    }

    public NoteItem(int id, String title, String text, int position) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.position = position;
    }

    public NoteItem(JSONObject o) {
        id = o.optInt("id", 0);
        title = o.optString("title", "");
        text = o.optString("text", "");
        position = o.optInt("position");
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
