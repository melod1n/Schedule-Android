package ru.melod1n.schedule.common;

import android.graphics.Color;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.fragment.SettingsFragment;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ColorUtil;
import ru.melod1n.schedule.util.Util;

public class ThemeEngine {

    public static final int ENGINE_VERSION = 4;

    public static final int[] COLOR_PALETTE_LIGHT = new int[]{
            Color.WHITE,
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

    private static final String DEFAULT_THEME = "teal";
    private static final String DEFAULT_THEME_DARK = "teal_dark";
    private static final String THEMES_FILE_NAME = "stock_themes.json";

    public static List<ThemeItem> themes = new ArrayList<>();

    private static List<ThemeItem> stockThemes = new ArrayList<>();
    private static List<ThemeItem> addedThemes = new ArrayList<>();

    private static int colorMain;

    private static boolean autoTheme;

    private static String selectedThemeKey;
    private static String dayThemeKey;
    private static String nightThemeKey;

    private static ThemeItem currentTheme, dayTheme, nightTheme;

    static void init() {
        initStockThemes();
        initAddedThemes();

        ThemeEngine.selectedThemeKey = AppGlobal.preferences.getString(SettingsFragment.KEY_THEME, DEFAULT_THEME);
        ThemeEngine.dayThemeKey = AppGlobal.preferences.getString(SettingsFragment.KEY_DAY_TIME_THEME, DEFAULT_THEME);
        ThemeEngine.nightThemeKey = AppGlobal.preferences.getString(SettingsFragment.KEY_NIGHT_TIME_THEME, DEFAULT_THEME_DARK);

        assert selectedThemeKey != null;
        assert dayThemeKey != null;
        assert nightThemeKey != null;

        initThemes();

        autoTheme = AppGlobal.preferences.getBoolean(SettingsFragment.KEY_AUTO_SWITCH_THEME, false);

        if (currentTheme == null) {
            selectedThemeKey = DEFAULT_THEME;
            AppGlobal.preferences.edit().putString(SettingsFragment.KEY_THEME, selectedThemeKey).apply();
            TaskManager.execute(() -> currentTheme = CacheStorage.getThemeById(DEFAULT_THEME));
        }

        if (dayTheme == null) {
            dayThemeKey = DEFAULT_THEME;
            AppGlobal.preferences.edit().putString(SettingsFragment.KEY_DAY_TIME_THEME, dayThemeKey).apply();
            TaskManager.execute(() -> dayTheme = CacheStorage.getThemeById(DEFAULT_THEME));
        }

        if (nightTheme == null) {
            nightThemeKey = DEFAULT_THEME_DARK;
            AppGlobal.preferences.edit().putString(SettingsFragment.KEY_NIGHT_TIME_THEME, nightThemeKey).apply();
            TaskManager.execute(() -> nightTheme = CacheStorage.getThemeById(DEFAULT_THEME_DARK));
        }

        colorMain = ColorUtil.isLight(currentTheme.getColorPrimary()) ? Color.BLACK : Color.WHITE;
    }

    private static void initThemes() {
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
    }

    private static void initStockThemes() {
        try {
            JSONArray o = new JSONArray(Util.readFileFromAssets(THEMES_FILE_NAME));

            for (int i = 0; i < o.length(); i++) {
                ThemeItem item = new ThemeItem(o.optJSONObject(i));
                themes.add(item);
                stockThemes.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initAddedThemes() {
        TaskManager.execute(() -> {
            addedThemes = CacheStorage.getThemes();

            themes.addAll(addedThemes);
        });
    }

    public static String getSelectedThemeKey() {
        return selectedThemeKey;
    }

    public static ThemeItem getCurrentTheme() {
        return currentTheme;
    }

    public static void setCurrentTheme(String id) {
        AppGlobal.preferences.edit().putString(SettingsFragment.KEY_THEME, id).apply();
        selectedThemeKey = id;

        initThemes();

        Engine.sendEvent(new EventInfo<>(EventInfo.KEY_THEME_UPDATE, currentTheme), true);
    }

    public static ThemeItem getDayTheme() {
        return dayTheme;
    }

    public static void setDayTheme(String id) {
        AppGlobal.preferences.edit().putString(SettingsFragment.KEY_DAY_TIME_THEME, id).apply();
        dayThemeKey = id;

        if ((TimeManager.isMorning() || TimeManager.isAfternoon()) && isAutoTheme()) {
            setCurrentTheme(id);
            return;
        }

        initThemes();

        Engine.sendEvent(new EventInfo<>(EventInfo.KEY_THEME_UPDATE_DAY, dayTheme), true);
    }

    public static ThemeItem getNightTheme() {
        return nightTheme;
    }

    public static void setNightTheme(String id) {
        AppGlobal.preferences.edit().putString(SettingsFragment.KEY_NIGHT_TIME_THEME, id).apply();
        nightThemeKey = id;

        if ((TimeManager.isNight() || TimeManager.isEvening()) && isAutoTheme()) {
            setCurrentTheme(id);
            return;
        }

        initThemes();

        Engine.sendEvent(new EventInfo<>(EventInfo.KEY_THEME_UPDATE_NIGHT, nightTheme));
    }

    public static boolean isAutoTheme() {
        return autoTheme;
    }

    public static void setAutoTheme(boolean autoTheme) {
        ThemeEngine.autoTheme = autoTheme;

    }

    public static boolean isDark() {
        return currentTheme.isDark();
    }

    public static boolean isThemeCompatible(@NonNull ThemeItem item) {
        return item.getEngineVersion() == ENGINE_VERSION;
    }

    public static int getColorMain() {
        return colorMain;
    }
}
