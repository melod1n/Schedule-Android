package ru.stwtforever.schedule.adapter.items;
import android.support.annotation.*;
import org.json.*;

public class SubjectItem extends Item {
	
    private int id;
    private String name;
    private String cab;
    private String homework;
	private int day;
	private @ColorInt int color = -1;
    
	public SubjectItem() {}
	
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

	public void setColor(int color) {
		this.color = color;
	}

	public @ColorInt int getColor() {
		return color;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCab(String cab) {
		this.cab = cab;
	}

	public String getCab() {
		return cab;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}

	public String getHomework() {
		return homework;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}
	
	
}

