package ru.melod1n.schedule.items;

import java.io.Serializable;

public class AboutItem implements Serializable {

    private Integer icon;
    private String name, job;
    private String link;
    private boolean expanded;

    public AboutItem(Integer icon, String name, String job, String link) {
        this.icon = icon;
        this.name = name;
        this.job = job;
        this.link = link;
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
