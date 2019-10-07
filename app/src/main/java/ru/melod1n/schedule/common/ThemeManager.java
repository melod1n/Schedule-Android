package ru.melod1n.schedule.common;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatDelegate;

import org.greenrobot.eventbus.EventBus;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.fragment.SettingsFragment;

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

    private static boolean dark;
    private static String themeType;
    private static int theme, fullscreen_alert_theme, bottom_sheet_theme;
    private static int primary, primary_dark, accent, background, main, icons, icons_selected;

    static void init() {
        dark = AppGlobal.preferences.getBoolean("isDark", false);
        themeType = AppGlobal.preferences.getString(SettingsFragment.KEY_THEME, "light");
        AppCompatDelegate.setDefaultNightMode(getNightMode());

        theme = R.style.AppTheme;
        fullscreen_alert_theme = R.style.AppTheme_FullScreenDialog;

        primary = getColor(R.color.primary);
        primary_dark = getColor(R.color.primary_dark);
        accent = getColor(R.color.accent);
        background = getColor(R.color.background);
        main = getAccent();
        icons = Color.GRAY;
        icons_selected = isDark() ? Color.WHITE : getAccent();
        bottom_sheet_theme = R.style.BottomSheet;
    }

    public static void switchTheme() {
        init();
        EventBus.getDefault().post(new Object[]{"theme_update"});
    }

    private static int getNightMode() {
        return getNightMode(themeType);
    }

    public static int getNightMode(String themeType) {
        switch (themeType) {
            case "light":
                return AppCompatDelegate.MODE_NIGHT_NO;
            case "dark":
                return AppCompatDelegate.MODE_NIGHT_YES;
            case "system":
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            case "auto_battery":
                return AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
            default:
                return AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;
        }
    }

    public static boolean isDark() {
        return dark;
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

    public static void setDark(boolean dark) {
        ThemeManager.dark = dark;
        AppGlobal.preferences.edit().putBoolean("isDark", dark).apply();
    }
}
