package ru.melod1n.schedule.util;

import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;


public class ViewUtil {

    public static void fadeView(@NonNull View v, long duration) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(duration).start();
    }

    public static void fadeView(View v) {
        fadeView(v, 200);
    }

    public static void applyWindowStyles(Window window) {
        applyWindowStyles(window, ThemeEngine.getCurrentTheme().getColorPrimaryDark());
    }

    public static void applyWindowStyles(Window window, @ColorInt int primaryDark) {
        ThemeItem theme = ThemeEngine.getCurrentTheme();

        window.setStatusBarColor(primaryDark);
        window.setNavigationBarColor(primaryDark);

        int visibility = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (theme.isLightStatusBar()) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }

            if (theme.isLightNavigationBar()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                } else {
                    window.setNavigationBarColor(ColorUtil.darkenColor(primaryDark));
                }
            }
        }

        window.getDecorView().setSystemUiVisibility(visibility);
    }
}
