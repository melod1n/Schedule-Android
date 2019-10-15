package ru.melod1n.schedule.items;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.Serializable;

import ru.melod1n.schedule.util.ArrayUtil;

public class ThemeItem implements Serializable {

    private static int serialVersionUID = 1;

    private String id;
    private String title;
    private String author;
    private int engineVersion;

    private boolean dark;
    private boolean lightStatusBar;
    private boolean lightNavigationBar;
    private boolean md2;

    private boolean selected;

    private int colorSurface;
    private int colorPrimary;
    private int colorPrimaryDark;
    private int colorAccent;
    private int colorBackground;
    private int colorTabsTextNormal;
    private int colorTabsTextActive;
    private int colorTabsIndicator;
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
    private int colorBottomBar;
    private int colorBottomBarIconsNormal;
    private int colorBottomBarIconsActive;
    private int colorHighlight;
    private int fabColor;
    private int fabIconColor;

    public ThemeItem() {
    }

    public ThemeItem(@NonNull JSONObject o) {
        this.id = o.optString("id");
        this.title = o.optString("title");
        this.author = o.optString("author");
        this.engineVersion = o.optInt("version");

        JSONObject ui = o.optJSONObject("ui");
        if (!ArrayUtil.isEmpty(ui)) {
            this.dark = ui.optBoolean("dark");
            this.lightStatusBar = ui.optBoolean("light_status_bar");
            this.lightNavigationBar = ui.optBoolean("light_navigation_bar");
            this.md2 = ui.optBoolean("md2");
        }

        JSONObject colors = o.optJSONObject("colors");
        if (!ArrayUtil.isEmpty(colors)) {
            this.colorSurface = parseColor(colors.optString("surface"));
            this.colorPrimary = parseColor(colors.optString("primary"));
            this.colorPrimaryDark = parseColor(colors.optString("primary_dark"));
            this.colorAccent = parseColor(colors.optString("accent"));
            this.colorBackground = parseColor(colors.optString("background"));
            this.colorTabsTextNormal = parseColor(colors.optString("tabs_text_normal"));
            this.colorTabsTextActive = parseColor(colors.optString("tabs_text_active"));
            this.colorTabsIndicator = parseColor(colors.optString("tabs_indicator"));
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
            this.colorBottomBar = parseColor(colors.optString("bottom_bar"));
            this.colorBottomBarIconsNormal = parseColor(colors.optString("bottom_bar_icons_normal"));
            this.colorBottomBarIconsActive = parseColor(colors.optString("bottom_bar_icons_active"));
            this.colorHighlight = parseColor(colors.optString("highlight"));
            this.fabColor = parseColor(colors.optString("fab_color"));
            this.fabIconColor = parseColor(colors.optString("fab_icon_color"));
        }
    }

    private int parseColor(String color) {
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            switch (color) {
                case "primary":
                    return colorPrimary;
                case "primary_dark":
                    return colorPrimaryDark;
                case "accent":
                    return colorAccent;
                case "surface":
                    return colorSurface;
                case "background":
                    return colorBackground;
                default:
                    return -1;
            }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public boolean isLightStatusBar() {
        return lightStatusBar;
    }

    public void setLightStatusBar(boolean lightStatusBar) {
        this.lightStatusBar = lightStatusBar;
    }

    public boolean isLightNavigationBar() {
        return lightNavigationBar;
    }

    public void setLightNavigationBar(boolean lightNavigationBar) {
        this.lightNavigationBar = lightNavigationBar;
    }

    public boolean isMd2() {
        return md2;
    }

    public void setMd2(boolean md2) {
        this.md2 = md2;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public int getColorTabsTextNormal() {
        return colorTabsTextNormal;
    }

    public void setColorTabsTextNormal(int colorTabsTextNormal) {
        this.colorTabsTextNormal = colorTabsTextNormal;
    }

    public int getColorTabsTextActive() {
        return colorTabsTextActive;
    }

    public void setColorTabsTextActive(int colorTabsTextActive) {
        this.colorTabsTextActive = colorTabsTextActive;
    }

    public int getColorTabsIndicator() {
        return colorTabsIndicator;
    }

    public void setColorTabsIndicator(int colorTabsIndicator) {
        this.colorTabsIndicator = colorTabsIndicator;
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

    public int getColorDrawer() {
        return colorDrawer;
    }

    public void setColorDrawer(int colorDrawer) {
        this.colorDrawer = colorDrawer;
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

    public int getColorBottomBar() {
        return colorBottomBar;
    }

    public void setColorBottomBar(int colorBottomBar) {
        this.colorBottomBar = colorBottomBar;
    }

    public int getColorBottomBarIconsNormal() {
        return colorBottomBarIconsNormal;
    }

    public void setColorBottomBarIconsNormal(int colorBottomBarIconsNormal) {
        this.colorBottomBarIconsNormal = colorBottomBarIconsNormal;
    }

    public int getColorBottomBarIconsActive() {
        return colorBottomBarIconsActive;
    }

    public void setColorBottomBarIconsActive(int colorBottomBarIconsActive) {
        this.colorBottomBarIconsActive = colorBottomBarIconsActive;
    }

    public int getColorHighlight() {
        return colorHighlight;
    }

    public void setColorHighlight(int colorHighlight) {
        this.colorHighlight = colorHighlight;
    }

    public int getFabColor() {
        return fabColor;
    }

    public void setFabColor(int fabColor) {
        this.fabColor = fabColor;
    }

    public int getFabIconColor() {
        return fabIconColor;
    }

    public void setFabIconColor(int fabIconColor) {
        this.fabIconColor = fabIconColor;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ThemeItem)) return false;
        if (obj == this) return true;

        ThemeItem themeItem = (ThemeItem) obj;

        return id.toLowerCase().equals(themeItem.getId().toLowerCase());
    }
}
