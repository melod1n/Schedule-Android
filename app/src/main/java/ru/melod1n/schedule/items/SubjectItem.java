package ru.melod1n.schedule.items;

import java.io.Serializable;

public class SubjectItem  implements Serializable {

    private int id;
    private int colorPosition;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColorPosition() {
        return colorPosition;
    }

    public void setColorPosition(int colorPosition) {
        this.colorPosition = colorPosition;
    }
}
