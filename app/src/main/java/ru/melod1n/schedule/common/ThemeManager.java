package ru.melod1n.schedule.common;

import android.graphics.Color;

import org.json.JSONArray;

import java.util.ArrayList;

import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Util;

public class ThemeManager {

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

    private static final String DEFAULT_THEME = "stock_light";

    private static volatile ThemeItem currentTheme;

    static void init() {
        String themeKey = AppGlobal.preferences.getString("theme", DEFAULT_THEME);

        ArrayList<ThemeItem> themes = CacheStorage.getThemes();
        insertStockThemes(themes);

        for (ThemeItem theme : themes) {
            if (themeKey.toLowerCase().equals(theme.getKey().toLowerCase())) {
                currentTheme = theme;
                break;
            }
        }

        if (currentTheme == null) {
            currentTheme = CacheStorage.getThemes(DEFAULT_THEME).get(0);
        }
    }

    public static void insertStockThemes(ArrayList<ThemeItem> themes) {
        try {
            JSONArray o = new JSONArray(Util.readFileFromAssets("stock_themes"));
            for (int i = 0; i < o.length(); i++) {
                themes.add(new ThemeItem(o.optJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CacheStorage.insert(DatabaseHelper.TABLE_THEMES, themes);
    }

    public static ThemeItem getCurrentTheme() {
        return currentTheme;
    }

    public static void setCurrentTheme(String key) {
        AppGlobal.preferences.edit().putString("theme", key).apply();
        init();
    }

    public static boolean isDark() {
        return currentTheme.isDark();
    }
}
