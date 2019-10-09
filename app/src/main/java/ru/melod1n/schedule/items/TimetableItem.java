package ru.melod1n.schedule.items;

import java.io.Serializable;
import java.util.ArrayList;

public class TimetableItem implements Serializable {

    private ArrayList<BellItem> bells;
    private String title;

    public ArrayList<BellItem> getBells() {
        return bells;
    }

    public void setBells(ArrayList<BellItem> bells) {
        this.bells = bells;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
