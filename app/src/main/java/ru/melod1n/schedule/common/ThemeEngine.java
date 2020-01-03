package ru.melod1n.schedule.common;

import android.graphics.Color;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.util.ArrayList;

import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.fragment.SettingsFragment;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ColorUtil;
import ru.melod1n.schedule.util.Util;

public class ThemeEngine {

    public static final int ENGINE_VERSION = 3;

    public static final int[] COLOR_PALETTE_LIGHT = new int[]{
            Color.LTGRAY,
            0xffEF9A9A,
            0xffF48FB1,
            0xffCE93D8,
            0xff9FA8DA,
            0xff90CAF9,
            0xff81D4FA,
            0xff80DEEA,
            0xff80CBC4,
            0xffA5D6A7,
            0xffC5E1A5,
            0xffE6EE9C,
            0xffFFF59D,
            0xffFFE082,
            0xffFFCC80,
            0xffFFAB91
    };

    public static final int[] COLOR_PALETTE_DARK = new int[]{
            Color.DKGRAY,
            0xffB71C1C,
            0xff880E4F,
            0xff4A148C,
            0xff283593,
            0xff1565C0,
            0xff0277BD,
            0xff00838F,
            0xff00695C,
            0xff2E7D32,
            0xff558B2F,
            0xff9E9D24,
            0xffF9A825,
            0xffFF8F00,
            0xffEF6C00,
            0xffD84315
    };

    private static final String DEFAULT_THEME = "teal_md2";
    private static final String DEFAULT_THEME_DARK = "teal_md2_dark";
    private static final String THEMES_FILE_NAME = "stock_themes.json";

    private static int colorMain;
    private static boolean autoTheme;

    private static String selectedThemeKey;

    private static ThemeItem currentTheme, dayTheme, nightTheme;

    static void init() {
        String selectedThemeKey = Engine.getPrefString(SettingsFragment.KEY_THEME, DEFAULT_THEME);

        ThemeEngine.selectedThemeKey = selectedThemeKey;

        String dayThemeKey = Engine.getPrefString(SettingsFragment.KEY_DAY_TIME_THEME, DEFAULT_THEME);
        String nightThemeKey = Engine.getPrefString(SettingsFragment.KEY_NIGHT_TIME_THEME, DEFAULT_THEME_DARK);

        autoTheme = Engine.getPrefBool(SettingsFragment.KEY_AUTO_SWITCH_THEME, false);

        ArrayList<ThemeItem> themes = CacheStorage.getThemes();
        insertStockThemes(themes);

        for (ThemeItem theme : themes) {
            if (selectedThemeKey.toLowerCase().equals(theme.getId().toLowerCase())) {
                currentTheme = theme;
            }

            if (dayThemeKey.toLowerCase().equals(theme.getId().toLowerCase())) {
                dayTheme = theme;
            }

            if (nightThemeKey.toLowerCase().equals(theme.getId().toLowerCase())) {
                nightTheme = theme;
            }
        }

        if (currentTheme == null) {
            insertStockThemes(null);
            currentTheme = CacheStorage.getThemes(DEFAULT_THEME).get(0);
        }

        if (dayTheme == null) {
            insertStockThemes(null);
            dayTheme = CacheStorage.getThemes(DEFAULT_THEME).get(0);
        }

        if (nightTheme == null) {
            insertStockThemes(null);
            nightTheme = CacheStorage.getThemes(DEFAULT_THEME_DARK).get(0);
        }

        colorMain = ColorUtil.isLight(currentTheme.getColorPrimary()) ? Color.BLACK : Color.WHITE;
    }

    public static void insertStockThemes(ArrayList<ThemeItem> themes) {
        if (themes == null) themes = new ArrayList<>();
        try {
            JSONArray o = new JSONArray(Util.readFileFromAssets(THEMES_FILE_NAME));
            for (int i = 0; i < o.length(); i++) {
                themes.add(new ThemeItem(o.optJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CacheStorage.insert(DatabaseHelper.TABLE_THEMES, themes);
    }

    public static String getSelectedThemeKey() {
        return selectedThemeKey;
    }

    public static ThemeItem getCurrentTheme() {
        return currentTheme;
    }

    public static ThemeItem getDayTheme() {
        return dayTheme;
    }

    public static ThemeItem getNightTheme() {
        return nightTheme;
    }

    public static void setCurrentTheme(String id) {
        Engine.editPreferences(SettingsFragment.KEY_THEME, id);
        init();
        Engine.sendEvent(new EventInfo(EventInfo.KEY_THEME_UPDATE, currentTheme), true);
    }

    public static void setDayTheme(String id) {
        Engine.editPreferences(SettingsFragment.KEY_DAY_TIME_THEME, id);

        if ((TimeManager.isMorning() || TimeManager.isAfternoon()) && isAutoTheme()) {
            setCurrentTheme(id);
            return;
        }

        init();
        Engine.sendEvent(new EventInfo(EventInfo.KEY_THEME_UPDATE_DAY, dayTheme), true);
    }

    public static void setNightTheme(String id) {
        Engine.editPreferences(SettingsFragment.KEY_NIGHT_TIME_THEME, id);

        if ((TimeManager.isNight() || TimeManager.isEvening()) && isAutoTheme()) {
            setCurrentTheme(id);
            return;
        }

        init();
        Engine.sendEvent(new EventInfo(EventInfo.KEY_THEME_UPDATE_NIGHT, nightTheme));
    }

//    public static void setSelectedThemeKey(String selectedThemeKey) {
//        ThemeEngine.selectedThemeKey = selectedThemeKey;
//        Engine.editPreferences(SettingsFragment.KEY_THEME, selectedThemeKey);
//    }

    public static boolean isAutoTheme() {
        return autoTheme;
    }

    public static void setAutoTheme(boolean autoTheme) {
        ThemeEngine.autoTheme = autoTheme;

    }

    public static boolean isDark() {
        return currentTheme.isDark();
    }

    public static boolean isThemeValid(@NonNull ThemeItem item) {
        return item.getEngineVersion() == ENGINE_VERSION;
    }

    public static int getColorMain() {
        return colorMain;
    }
}
