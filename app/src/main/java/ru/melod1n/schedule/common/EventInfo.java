package ru.melod1n.schedule.common;

public class EventInfo<T> {

    public static final String KEY_THEME_UPDATE = "theme_update";
    public static final String KEY_THEME_UPDATE_DAY = "theme_update_day";
    public static final String KEY_THEME_UPDATE_NIGHT = "theme_update_night";
    public static final String KEY_USER_NAME_UPDATE = "user_name_update";

    private String key;
    private T data;

    public EventInfo(String key, T data) {
        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
