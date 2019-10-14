package ru.melod1n.schedule.items;

import java.io.Serializable;

public class LocationItem implements Serializable {

    private static int serialVersionUID = 1;

    private int id;
    private String title;
    private String building;

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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
