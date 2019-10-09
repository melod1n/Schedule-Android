package ru.melod1n.schedule.util;

import android.content.Context;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
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

    public static void applyToolbarStyles(@NonNull Toolbar toolbar, int colorControl, Boolean isDark) {
        int color = colorControl == -1 ? (isDark == null ? ThemeManager.isDark() : isDark) ? Color.WHITE : Color.BLACK : colorControl;

        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(color);
        }

        if (toolbar.getOverflowIcon() != null) {
            toolbar.getOverflowIcon().setTint(color);
        }

        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            MenuItem item = toolbar.getMenu().getItem(i);
            if (item.getIcon() != null)
                item.getIcon().setTint(color);
        }
    }

    public static void applyToolbarStyles(@NonNull Toolbar toolbar, int colorControl) {
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(colorControl);
        }

        if (toolbar.getOverflowIcon() != null) {
            toolbar.getOverflowIcon().setTint(colorControl);
        }

        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            MenuItem item = toolbar.getMenu().getItem(i);
            if (item.getIcon() != null)
                item.getIcon().setTint(colorControl);
        }
    }

    public static void applyToolbarStyles(@NonNull Toolbar toolbar) {
        applyToolbarStyles(toolbar, -1, null);
    }

    public static void showKeyboard(@NonNull View v) {
        keyboard.showSoftInput(v, 0);
    }

    public static void hideKeyboard(@NonNull View v) {
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
