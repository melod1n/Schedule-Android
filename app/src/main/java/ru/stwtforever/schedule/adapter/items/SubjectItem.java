package ru.stwtforever.schedule.adapter.items;

import androidx.annotation.ColorInt;

import org.json.JSONObject;

public class SubjectItem {

    private int id;
    private String name;
    private String cab;
    private String homework;
    private int day;
    private @ColorInt
    int color = -1;

    public SubjectItem() {
    }

    public SubjectItem(JSONObject o) {
        id = o.optInt("id", 0);
        name = o.optString("name");
        cab = o.optString("cab", "");
        homework = o.optString("homework", "");
        day = o.optInt("day", 0);
    }

    public SubjectItem(int id, String name, String cab, String homework, int day) {
        this.name = name;
        this.cab = cab;
        this.homework = homework;
        this.id = id;
        this.day = day;
    }

    public @ColorInt
    int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCab() {
        return cab;
    }

    public void setCab(String cab) {
        this.cab = cab;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


}

