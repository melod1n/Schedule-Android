package ru.melod1n.schedule.common;

public class EventInfo {

    public static final String KEY_THEME_UPDATE = "theme_update";
    public static final String KEY_THEME_UPDATE_DAY = "theme_update_day";
    public static final String KEY_THEME_UPDATE_NIGHT = "theme_update_night";

    private String key;
    private Object data;

    public EventInfo(String key, Object data) {
        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
