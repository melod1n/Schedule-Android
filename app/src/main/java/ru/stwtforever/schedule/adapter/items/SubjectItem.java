package ru.stwtforever.schedule.adapter.items;
import org.json.*;
import ru.stwtforever.schedule.common.*;

public class SubjectItem extends Item {
	
    public int id;
    public String name;
    public String cab;
    public String homework;
	public int day;
    
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

