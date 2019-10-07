package ru.melod1n.schedule.items;

import java.io.Serializable;

public class HomeworkItem implements Serializable {

    public String text, title, date;
    public int color;

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
}
