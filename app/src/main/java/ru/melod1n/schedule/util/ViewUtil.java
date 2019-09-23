package ru.melod1n.schedule.util;

import android.content.Context;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.common.ThemeManager;


public class ViewUtil {

    private static final InputMethodManager keyboard = (InputMethodManager)
            AppGlobal.context.getSystemService(Context.INPUT_METHOD_SERVICE);

    public static void fadeView(@NonNull View v, long duration) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(duration).start();
    }

    public static void fadeView(View v) {
        fadeView(v, 200);
    }

    public static void applyToolbarStyles(@NonNull Toolbar tb) {
        boolean isDark = ThemeManager.isDark() || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;

        int color = ThemeManager.isDark() ? Color.WHITE : Color.BLACK;

        if (tb.getNavigationIcon() != null) {
            tb.getNavigationIcon().setTint(color);
        }

        if (tb.getOverflowIcon() != null) {
            tb.getOverflowIcon().setTint(color);
        }

        for (int i = 0; i < tb.getMenu().size(); i++) {
            MenuItem item = tb.getMenu().getItem(i);
            if (item.getIcon() != null)
                item.getIcon().setTint(color);
        }
    }

    public static void showKeyboard(@NonNull View v) {
        keyboard.showSoftInput(v, 0);
    }

    public static void hideKeyboard(@NonNull View v) {
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
