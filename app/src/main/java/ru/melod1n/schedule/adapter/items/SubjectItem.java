package ru.melod1n.schedule.adapter.items;

import org.json.JSONObject;

import ru.melod1n.schedule.helper.TimeHelper;
import ru.melod1n.schedule.util.Util;

public class SubjectItem {

    private int id;
    private String name;
    private String cab;
    private String homework;
    private int day;
    private int position = -1;
    private int start;
    private int end;

    public SubjectItem() {
    }

    public SubjectItem(JSONObject o) {
        id = o.optInt("id");
        name = o.optString("name");
        cab = o.optString("cab", "");
        homework = o.optString("homework", "");
        day = o.optInt("day");
        position = o.optInt("position");
    }

    public SubjectItem(int id, String name, String cab, String homework, int day) {
        this.name = name;
        this.cab = cab;
        this.homework = homework;
        this.id = id;
        this.day = day;
    }

    public String getStartHours() {
        if (start == -1) return null;
        return Util.leadingZero((int) Math.floor(getStart() / 60));
    }

    public String getEndHours() {
        if (end == -1) return null;
        return Util.leadingZero((int) Math.floor(getEnd() / 60));
    }

    public String getStartMinutes() {
        if (start == -1) return null;
        int hours_start = (int) Math.floor(getStart() / 60);
        return Util.leadingZero(((int) Math.floor(getStart() - hours_start * 60)));
    }

    public String getEndMinutes() {
        if (end == -1) return null;
        int hours_end = (int) Math.floor(getEnd() / 60);
        return Util.leadingZero(((int) Math.floor(getEnd() - hours_end * 60)));
    }

    public String getStartFull() {
        int hours_start = (int) Math.floor(getStart() / 60);
        int minutes_start = (int) Math.floor(getStart() - hours_start * 60);

        return Util.leadingZero(hours_start) + ":" + Util.leadingZero(minutes_start);
    }

    public String getEndFull() {
        int hours_end = (int) Math.floor(getEnd() / 60);
        int minutes_end = (int) Math.floor(getEnd() - hours_end * 60);

        return Util.leadingZero(hours_end) + ":" + Util.leadingZero(minutes_end);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setStart(String start) {
        setStart(TimeHelper.getStart(start));
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(String end) {
        setEnd(TimeHelper.getStart(end));
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

