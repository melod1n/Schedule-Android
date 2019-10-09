package ru.melod1n.schedule.items;

import android.graphics.Color;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.Serializable;

import ru.melod1n.schedule.util.ArrayUtil;

public class ThemeItem implements Serializable {

    private String key;
    private String name;
    private String madeBy;
    private int engineVersion;

    private boolean dark;
    private boolean selected;

    private int colorSurface;
    private int colorPrimary;
    private int colorPrimaryDark;
    private int colorAccent;
    private int colorBackground;
    private int colorTabsText;
    private int colorControlNormal;
    private int colorTextPrimary;
    private int colorTextSecondary;
    private int colorTextPrimaryInverse;
    private int colorTextSecondaryInverse;
    private int colorDrawer;
    private int colorDrawerTextNormal;
    private int colorDrawerTextActive;
    private int colorDrawerIconNormal;
    private int colorDrawerIconActive;
    private int colorDrawerHeaderBackground;
    private int colorDrawerHeaderTitle;
    private int colorDrawerHeaderSubtitle;

    public ThemeItem() {
    }

    public ThemeItem(@NonNull JSONObject o) {
        this.name = o.optString("name");
        this.madeBy = o.optString("made_by");
        this.engineVersion = o.optInt("version");
        this.key = o.optString("key");

        this.dark = o.optBoolean("dark");

        JSONObject colors = o.optJSONObject("colors");
        if (!ArrayUtil.isEmpty(colors)) {
            this.colorSurface = parseColor(colors.optString("surface"));
            this.colorPrimary = parseColor(colors.optString("primary"));
            this.colorPrimaryDark = parseColor(colors.optString("primary_dark"));
            this.colorAccent = parseColor(colors.optString("accent"));
            this.colorBackground = parseColor(colors.optString("background"));
            this.colorTabsText = parseColor(colors.optString("tabs_text"));
            this.colorControlNormal = parseColor(colors.optString("control_normal"));
            this.colorTextPrimary = parseColor(colors.optString("text_primary"));
            this.colorTextSecondary = parseColor(colors.optString("text_secondary"));
            this.colorTextPrimaryInverse = parseColor(colors.optString("text_primary_inverse"));
            this.colorTextSecondaryInverse = parseColor(colors.optString("text_secondary_inverse"));
            this.colorDrawer = parseColor(colors.optString("drawer"));
            this.colorDrawerTextNormal = parseColor(colors.optString("drawer_text_normal"));
            this.colorDrawerTextActive = parseColor(colors.optString("drawer_text_active"));
            this.colorDrawerIconNormal = parseColor(colors.optString("drawer_icon_normal"));
            this.colorDrawerIconActive = parseColor(colors.optString("drawer_icon_active"));
            this.colorDrawerHeaderBackground = parseColor(colors.optString("drawer_header_background"));
            this.colorDrawerHeaderTitle = parseColor(colors.optString("drawer_header_title"));
            this.colorDrawerHeaderSubtitle = parseColor(colors.optString("drawer_header_subtitle"));
        }
    }

    private int parseColor(String color) {
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            return -1;
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


    public void setColorSurface(String colorSurface) {
        this.colorSurface = parseColor(colorSurface);
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = parseColor(colorPrimary);
    }

    public void setColorPrimaryDark(String colorPrimaryDark) {
        this.colorPrimaryDark = parseColor(colorPrimaryDark);
    }

    public void setColorAccent(String colorAccent) {
        this.colorAccent = parseColor(colorAccent);
    }

    public void setColorBackground(String colorBackground) {
        this.colorBackground = parseColor(colorBackground);
    }

    public void setColorTabsText(String colorTabsText) {
        this.colorTabsText = parseColor(colorTabsText);
    }

    public void setColorControlNormal(String colorControlNormal) {
        this.colorControlNormal = parseColor(colorControlNormal);
    }

    public void setColorTextPrimary(String colorTextPrimary) {
        this.colorTextPrimary = parseColor(colorTextPrimary);
    }

    public void setColorTextSecondary(String colorTextSecondary) {
        this.colorTextSecondary = parseColor(colorTextSecondary);
    }

    public void setColorTextPrimaryInverse(String colorTextPrimaryInverse) {
        this.colorTextPrimaryInverse = parseColor(colorTextPrimaryInverse);
    }

    public void setColorTextSecondaryInverse(String colorTextSecondaryInverse) {
        this.colorTextSecondaryInverse = parseColor(colorTextSecondaryInverse);
    }

    public int getColorSurface() {
        return colorSurface;
    }

    public void setColorSurface(int colorSurface) {
        this.colorSurface = colorSurface;
    }

    public int getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(int colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public int getColorPrimaryDark() {
        return colorPrimaryDark;
    }

    public void setColorPrimaryDark(int colorPrimaryDark) {
        this.colorPrimaryDark = colorPrimaryDark;
    }

    public int getColorAccent() {
        return colorAccent;
    }

    public void setColorAccent(int colorAccent) {
        this.colorAccent = colorAccent;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public int getColorTabsText() {
        return colorTabsText;
    }

    public void setColorTabsText(int colorTabsText) {
        this.colorTabsText = colorTabsText;
    }

    public int getColorControlNormal() {
        return colorControlNormal;
    }

    public void setColorControlNormal(int colorControlNormal) {
        this.colorControlNormal = colorControlNormal;
    }

    public int getColorTextPrimary() {
        return colorTextPrimary;
    }

    public void setColorTextPrimary(int colorTextPrimary) {
        this.colorTextPrimary = colorTextPrimary;
    }

    public int getColorTextSecondary() {
        return colorTextSecondary;
    }

    public void setColorTextSecondary(int colorTextSecondary) {
        this.colorTextSecondary = colorTextSecondary;
    }

    public int getColorTextPrimaryInverse() {
        return colorTextPrimaryInverse;
    }

    public void setColorTextPrimaryInverse(int colorTextPrimaryInverse) {
        this.colorTextPrimaryInverse = colorTextPrimaryInverse;
    }

    public int getColorTextSecondaryInverse() {
        return colorTextSecondaryInverse;
    }

    public void setColorTextSecondaryInverse(int colorTextSecondaryInverse) {
        this.colorTextSecondaryInverse = colorTextSecondaryInverse;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getColorDrawerTextNormal() {
        return colorDrawerTextNormal;
    }

    public void setColorDrawerTextNormal(int colorDrawerTextNormal) {
        this.colorDrawerTextNormal = colorDrawerTextNormal;
    }

    public int getColorDrawerTextActive() {
        return colorDrawerTextActive;
    }

    public void setColorDrawerTextActive(int colorDrawerTextActive) {
        this.colorDrawerTextActive = colorDrawerTextActive;
    }

    public int getColorDrawerIconNormal() {
        return colorDrawerIconNormal;
    }

    public void setColorDrawerIconNormal(int colorDrawerIconNormal) {
        this.colorDrawerIconNormal = colorDrawerIconNormal;
    }

    public int getColorDrawerIconActive() {
        return colorDrawerIconActive;
    }

    public void setColorDrawerIconActive(int colorDrawerIconActive) {
        this.colorDrawerIconActive = colorDrawerIconActive;
    }

    public int getColorDrawerHeaderBackground() {
        return colorDrawerHeaderBackground;
    }

    public void setColorDrawerHeaderBackground(int colorDrawerHeaderBackground) {
        this.colorDrawerHeaderBackground = colorDrawerHeaderBackground;
    }

    public int getColorDrawerHeaderTitle() {
        return colorDrawerHeaderTitle;
    }

    public void setColorDrawerHeaderTitle(int colorDrawerHeaderTitle) {
        this.colorDrawerHeaderTitle = colorDrawerHeaderTitle;
    }

    public int getColorDrawerHeaderSubtitle() {
        return colorDrawerHeaderSubtitle;
    }

    public void setColorDrawerHeaderSubtitle(int colorDrawerHeaderSubtitle) {
        this.colorDrawerHeaderSubtitle = colorDrawerHeaderSubtitle;
    }

    public int getColorDrawer() {
        return colorDrawer;
    }

    public void setColorDrawer(int colorDrawer) {
        this.colorDrawer = colorDrawer;
    }
}
