package ru.stwtforever.schedule.adapter.items;

import android.graphics.drawable.*;

public class AboutItem extends Item {
	
	public Integer icon;
	public int selected_icon, selected_color;
	public String name, job;
	public String link, title;
	
	public AboutItem() {}
	
	public AboutItem(Integer icon, String name, String job, int selected_icon, int selected_color, String title, String link) {
		this.icon = icon;
		this.name = name;
		this.job = job;
		this.selected_color = selected_color;
		this.selected_icon = selected_icon;
		this.link = link;
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	
	public void setSelectedIcon(int selected_icon) {
		this.selected_icon = selected_icon;
	}

	public int getSelectedIcon() {
		return selected_icon;
	}

	public void setSelectedColor(int selected_color) {
		this.selected_color = selected_color;
	}

	public int getSelectedColor() {
		return selected_color;
	}

	public void setIcon(Integer icon) {
		this.icon = icon;
	}

	public int getIcon() {
		return icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getJob() {
		return job;
	}
}
