package ru.melod1n.schedule.items;

import java.io.Serializable;
import java.util.ArrayList;

public class DayItem implements Serializable {

    private static int serialVersionUID = 1;

    private long date;
    private int dayOfWeek;
    private int dayOfYear;
    private ArrayList<LessonItem> lessons;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public ArrayList<LessonItem> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<LessonItem> lessons) {
        this.lessons = lessons;
    }
}
