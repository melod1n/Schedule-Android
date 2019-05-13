package ru.stwtforever.schedule.adapter.items;

public abstract class Item {
	
	protected boolean expand = false;
	
	public void setExpanded(boolean expand) {
		this.expand = expand;
	}
	
	public boolean isExpanded() {
		return expand;
	}
}
