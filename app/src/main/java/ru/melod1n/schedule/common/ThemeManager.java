package ru.melod1n.schedule.common;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatDelegate;

import org.greenrobot.eventbus.EventBus;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.fragment.SettingsFragment;

public class ThemeManager {

    public static final int[] COLORS = new int[]{
            Color.WHITE,
            getColor(R.color.dark_primary),
    };

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

    private static boolean dark;
    private static int theme, fullscreen_alert_theme, bottom_sheet_theme;
    private static int primary, primary_dark, accent, background, main, icons, icons_selected;

    static void init() {
        dark = AppGlobal.preferences.getBoolean(SettingsFragment.KEY_DARK_THEME, false);

        AppCompatDelegate.setDefaultNightMode(dark ? AppGlobal.preferences.getBoolean(SettingsFragment.KEY_AUTO_DARK_THEME, false) ? AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM : AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        theme = R.style.AppTheme;
        fullscreen_alert_theme = R.style.AppTheme_FullScreenDialog;

        primary = getColor(isDark() ? R.color.dark_primary : R.color.primary);
        primary_dark = getColor(isDark() ? R.color.dark_primary_dark : R.color.primary_dark);
        accent = getColor(isDark() ? R.color.accent : R.color.accent);
        background = getColor(isDark() ? R.color.dark_background : R.color.background);
        main = getAccent();
        icons = Color.GRAY;
        icons_selected = isLight() ? getAccent() : Color.WHITE;
        bottom_sheet_theme = R.style.BottomSheet;
    }

    public static void switchTheme(boolean dark) {
        AppGlobal.preferences.edit().putBoolean(SettingsFragment.KEY_DARK_THEME, dark).apply();
        init();
        EventBus.getDefault().post(new Object[]{"theme_update"});
    }

    public static boolean isDark() {
        return dark;
    }

    public static boolean isLight() {
        return !dark;
    }

    public static int getFullScreenDialogTheme() {
        return fullscreen_alert_theme;
    }

    public static int getBottomSheetTheme() {
        return bottom_sheet_theme;
    }

    public static int getIcons() {
        return icons;
    }

    public static int getIconsSelected() {
        return icons_selected;
    }

    public static int getMain() {
        return main;
    }

    public static int getCurrentTheme() {
        return theme;
    }

    public static int getPrimary() {
        return primary;
    }

    public static int getPrimaryDark() {
        return primary_dark;
    }

    public static int getAccent() {
        return accent;
    }

    public static int getBackground() {
        return background;
    }

    private static int getColor(int res) {
        return AppGlobal.context.getColor(res);
    }
}
