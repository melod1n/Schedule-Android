package ru.melod1n.schedule.items;

import java.io.Serializable;

public class HomeworkItem implements Serializable {

    private String text;
    private String title;
    private String date;
    private int color;

    public HomeworkItem(String text) {
        this.text = text;
    }

    public HomeworkItem(String text, String title, String date, int color) {
        this.text = text;
        this.title = title;
        this.date = date;
        this.color = color;
    }

    public HomeworkItem() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
