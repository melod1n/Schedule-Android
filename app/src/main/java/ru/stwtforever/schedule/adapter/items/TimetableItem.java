package ru.stwtforever.schedule.adapter.items;

import java.util.*;

public class TimetableItem {
	
	private ArrayList<BellItem> bells;
	private String title;

	public void setBells(ArrayList<BellItem> bells) {
		this.bells = bells;
	}

	public ArrayList<BellItem> getBells() {
		return bells;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}}
