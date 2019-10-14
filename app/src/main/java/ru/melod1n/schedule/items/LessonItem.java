package ru.melod1n.schedule.items;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class LessonItem implements Serializable {

    private static int serialVersionUID = 1;

    private int order;
    private LessonType lessonType;
    private String lessonStringType;
    private String lessonTypeCustom;
    private SubjectItem subject;
    private TeacherItem teacher;
    private LocationItem classRoom;
    private ArrayList<ParticipantItem> participants = new ArrayList<>();

    public LessonItem() {
    }

    public LessonItem(JSONObject o) {
//        id = o.optInt("id");
//        name = o.optString("name");
//        cab = o.optString("cab", "");
//        homework = o.optString("homework", "");
//        day = o.optInt("day");
//        position = o.optInt("position");
    }



//    public String getStartHours() {
//        if (start == -1) return null;
//        return Util.leadingZero((int) Math.floor(getStart() / 60));
//    }
//
//    public String getEndHours() {
//        if (end == -1) return null;
//        return Util.leadingZero((int) Math.floor(getEnd() / 60));
//    }
//
//    public String getStartMinutes() {
//        if (start == -1) return null;
//        int hours_start = (int) Math.floor(getStart() / 60);
//        return Util.leadingZero(((int) Math.floor(getStart() - hours_start * 60)));
//    }
//
//    public String getEndMinutes() {
//        if (end == -1) return null;
//        int hours_end = (int) Math.floor(getEnd() / 60);
//        return Util.leadingZero(((int) Math.floor(getEnd() - hours_end * 60)));
//    }
//
//    public String getStartFull() {
//        int hours_start = (int) Math.floor(getStart() / 60);
//        int minutes_start = (int) Math.floor(getStart() - hours_start * 60);
//
//        return Util.leadingZero(hours_start) + ":" + Util.leadingZero(minutes_start);
//    }
//
//    public String getEndFull() {
//        int hours_end = (int) Math.floor(getEnd() / 60);
//        int minutes_end = (int) Math.floor(getEnd() - hours_end * 60);
//
//        return Util.leadingZero(hours_end) + ":" + Util.leadingZero(minutes_end);
//    }

    public String getLessonStringType() {
        return lessonStringType;
    }

    public void setLessonStringType(String lessonStringType) {
        this.lessonStringType = lessonStringType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public String getLessonTypeCustom() {
        return lessonTypeCustom;
    }

    public void setLessonTypeCustom(String lessonTypeCustom) {
        this.lessonTypeCustom = lessonTypeCustom;
    }

    public SubjectItem getSubject() {
        return subject;
    }

    public void setSubject(SubjectItem subject) {
        this.subject = subject;
    }

    public TeacherItem getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherItem teacher) {
        this.teacher = teacher;
    }

    public LocationItem getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(LocationItem classRoom) {
        this.classRoom = classRoom;
    }

    public ArrayList<ParticipantItem> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<ParticipantItem> participants) {
        this.participants = participants;
    }

    public enum LessonType {
        DEFAULT, LECTION, PRACTICE, LABWORK, COURSES, CUSTOM
    }

    public static int getLessonType(LessonType type) {
        if (type == null) return -1;

        switch (type) {
            case DEFAULT:
                return 0; //for schools
            case LECTION:
                return 1;
            case PRACTICE:
                return 2;
            case LABWORK:
                return 3;
            case COURSES:
                return 4;
            case CUSTOM:
                return 100;
        }

        return -1;
    }

    public static LessonType getLessonType(int type) {
        switch (type) {
            case 0:
                return LessonType.DEFAULT;
            case 1:
                return LessonType.LECTION;
            case 2:
                return LessonType.PRACTICE;
            case 3:
                return LessonType.LABWORK;
            case 4:
                return LessonType.COURSES;
            case 100:
                return LessonType.CUSTOM;
        }

        return null;
    }
}

