package ru.stwtforever.schedule.adapter.items;

public class AboutItem {

    private Integer icon;
    private int selectedIcon, selectedColor;
    private String name, job;
    private String link, title;
    private boolean expanded;

    public AboutItem(Integer icon, String name, String job, int selectedIcon, int selectedColor, String title, String link) {
        this.icon = icon;
        this.name = name;
        this.job = job;
        this.selectedColor = selectedColor;
        this.selectedIcon = selectedIcon;
        this.link = link;
        this.title = title;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selected_icon) {
        this.selectedIcon = selected_icon;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selected_color) {
        this.selectedColor = selected_color;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
