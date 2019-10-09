package ru.melod1n.schedule.items;

import android.graphics.Color;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.Serializable;

import ru.melod1n.schedule.util.ArrayUtil;

public class ThemeItem implements Serializable {

    private String name;
    private String madeBy;
    private int engineVersion;

    private boolean dark;
    private boolean selected;

    private String colorSurface;
    private String colorPrimary;
    private String colorPrimaryDark;
    private String colorAccent;
    private String colorBackground;
    private String colorTabsText;
    private String colorControlNormal;
    private String colorTextPrimary;
    private String colorTextSecondary;
    private String colorTextPrimaryInverse;
    private String colorTextSecondaryInverse;

    public ThemeItem() {
    }

    public ThemeItem(@NonNull JSONObject o) {
        this.name = o.optString("name");
        this.madeBy = o.optString("made_by");
        this.engineVersion = o.optInt("version");

        this.dark = o.optBoolean("dark");

        JSONObject colors = o.optJSONObject("colors");
        if (!ArrayUtil.isEmpty(colors)) {
            this.colorSurface = colors.optString("surface");
            this.colorPrimary = colors.optString("primary");
            this.colorPrimaryDark = colors.optString("primary_dark");
            this.colorAccent = colors.optString("accent");
            this.colorBackground = colors.optString("background");
            this.colorTabsText = colors.optString("tabs_text");
            this.colorControlNormal = colors.optString("control_normal");
            this.colorTextPrimary = colors.optString("text_primary");
            this.colorTextSecondary = colors.optString("text_secondary");
            this.colorTextPrimaryInverse = colors.optString("text_primary_inverse");
            this.colorTextSecondaryInverse = colors.optString("text_secondary_inverse");
        }
    }

    public int alphaColor(int color, float alphaFactor) {
        int alpha = Color.alpha(color);

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb((int) (alpha * alphaFactor), red, green, blue);
    }

    public int alphaColor(String color, float alphaFactor) {
        return alphaColor(Color.parseColor(color), alphaFactor);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(String madeBy) {
        this.madeBy = madeBy;
    }

    public int getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(int engineVersion) {
        this.engineVersion = engineVersion;
    }

    public boolean isDark() {
        return dark;
    }

    public void setDark(boolean dark) {
        this.dark = dark;
    }

    public String getColorSurface() {
        return colorSurface;
    }

    public void setColorSurface(String colorSurface) {
        this.colorSurface = colorSurface;
    }

    public String getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public String getColorPrimaryDark() {
        return colorPrimaryDark;
    }

    public void setColorPrimaryDark(String colorPrimaryDark) {
        this.colorPrimaryDark = colorPrimaryDark;
    }

    public String getColorAccent() {
        return colorAccent;
    }

    public void setColorAccent(String colorAccent) {
        this.colorAccent = colorAccent;
    }

    public String getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(String colorBackground) {
        this.colorBackground = colorBackground;
    }

    public String getColorTabsText() {
        return colorTabsText;
    }

    public void setColorTabsText(String colorTabsText) {
        this.colorTabsText = colorTabsText;
    }

    public String getColorControlNormal() {
        return colorControlNormal;
    }

    public void setColorControlNormal(String colorControlNormal) {
        this.colorControlNormal = colorControlNormal;
    }

    public String getColorTextPrimary() {
        return colorTextPrimary;
    }

    public void setColorTextPrimary(String colorTextPrimary) {
        this.colorTextPrimary = colorTextPrimary;
    }

    public String getColorTextSecondary() {
        return colorTextSecondary;
    }

    public void setColorTextSecondary(String colorTextSecondary) {
        this.colorTextSecondary = colorTextSecondary;
    }

    public String getColorTextPrimaryInverse() {
        return colorTextPrimaryInverse;
    }

    public void setColorTextPrimaryInverse(String colorTextPrimaryInverse) {
        this.colorTextPrimaryInverse = colorTextPrimaryInverse;
    }

    public String getColorTextSecondaryInverse() {
        return colorTextSecondaryInverse;
    }

    public void setColorTextSecondaryInverse(String colorTextSecondaryInverse) {
        this.colorTextSecondaryInverse = colorTextSecondaryInverse;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
