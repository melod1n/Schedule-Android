package ru.stwtforever.schedule.adapter.items;

import ru.stwtforever.schedule.util.*;
import ru.stwtforever.schedule.helper.*;
import org.json.*;

public class BellItem {
	
    public int start, end, id, day;
	
	public BellItem() {}

    public BellItem(int start, int end, int id, int day) {
        this.end = end;
        this.start = start;
        this.id = id;
		this.day = day;
    }
	
	public BellItem (JSONObject o) {
		id = o.optInt("id", 0);
		start = o.optInt("start", 0);
		end = o.optInt("end", 0);
		day = o.optInt("day", 0);
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartHours() {
		if (start == -1) return null;
        return Utils.leadingZero((int) Math.floor(getStart() / 60));
    }

    public String getEndHours() {
		if (end == -1) return null;
        return Utils.leadingZero((int) Math.floor(getEnd() / 60));
    }

    public String getStartMinutes() {
		if (start == -1) return null;
        int hours_start = (int) Math.floor(getStart() / 60);
        return Utils.leadingZero(((int) Math.floor(getStart() - hours_start * 60)));
    }

    public String getEndMinutes() {
		if (end == -1) return null;
        int hours_end = (int) Math.floor(getEnd() / 60);
        return Utils.leadingZero(((int) Math.floor(getEnd() - hours_end * 60)));
    }

    public String getStartFull() {
        int hours_start = (int) Math.floor(getStart() / 60);
        int minutes_start = (int) Math.floor(getStart() - hours_start * 60);

        return Utils.leadingZero(hours_start) + ":" + Utils.leadingZero(minutes_start);
    }

    public String getEndFull() {
        int hours_end = (int) Math.floor(getEnd() / 60);
        int minutes_end = (int) Math.floor(getEnd() - hours_end * 60);

        return Utils.leadingZero(hours_end) + ":" + Utils.leadingZero(minutes_end);
    }
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public int getDay() {
		return day;
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
	
	public void setEnd(String end) {
		setEnd(TimeHelper.getStart(end));
	}

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
