package ru.stwtforever.schedule.util;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.inputmethod.*;
import ru.stwtforever.schedule.common.*;


public class ViewUtil {
	
	private static final InputMethodManager keyboard = (InputMethodManager)
	AppGlobal.context.getSystemService(Context.INPUT_METHOD_SERVICE);

    public static void fadeView(View v, long duration) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(duration).start();
    }

    public static void fadeView(View v) {
        fadeView(v, 200);
    }
	
	public static void applyWindowStyles(Window w) {
		applyWindowStyles(w, ThemeManager.getPrimary());
	}

	public static void applyWindowStyles(Window w, int color) {
		int sb_color = color;
		int light_sb = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		int light_nb = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
		
		boolean light = Utils.isLight(color);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			w.getDecorView().setSystemUiVisibility(light ? light_sb : 0);
			w.setStatusBarColor(sb_color);
			w.setNavigationBarColor(light ? ColorUtil.darkenColor(sb_color) : sb_color);
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			w.getDecorView().setSystemUiVisibility(light ? (light_sb | light_nb) : 0);
			w.setStatusBarColor(sb_color);
			w.setNavigationBarColor(sb_color);
			return;
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			w.getDecorView().setSystemUiVisibility(0);
			w.setStatusBarColor(light ? ColorUtil.darkenColor(sb_color) : sb_color);
			w.setNavigationBarColor(w.getStatusBarColor());
			return;
		}
	}

	public static void setStyles(Activity w) {
		setStyles(w, false, ThemeManager.getPrimaryDark());
	}
	
	public static void setStyles(Activity a, boolean withTheme) {
		setStyles(a, withTheme, ThemeManager.getPrimaryDark());
	}

	public static void setStyles(Activity a, boolean withTheme, int c) {
		Window w = a.getWindow();

		int sb_color = c;
		int light_sb = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		int light_nb = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;

		if (withTheme) {
			a.setTheme(ThemeManager.getCurrentTheme());
		}

		if (!Utils.isLight(sb_color)) {
			w.getDecorView().setSystemUiVisibility(0);
			w.setStatusBarColor(sb_color);
			w.setNavigationBarColor(sb_color);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
				w.getDecorView().setSystemUiVisibility(light_sb);
				w.setStatusBarColor(sb_color);
				w.setNavigationBarColor(ColorUtil.darkenColor(sb_color));
				return;
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				w.getDecorView().setSystemUiVisibility(light_sb | light_nb);
				w.setStatusBarColor(sb_color);
				w.setNavigationBarColor(sb_color);
				return;
			}

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
				w.getDecorView().setSystemUiVisibility(0);
				w.setStatusBarColor(ColorUtil.darkenColor(sb_color));
				w.setNavigationBarColor(ColorUtil.darkenColor(sb_color));
				return;
			}
		}
	}
	
	public static void applyToolbarStyles(Toolbar tb) {
		tb.setTitleTextColor(Utils.isLight(ThemeManager.getPrimary()) ? Color.BLACK : Color.WHITE);
		
		if (tb.getNavigationIcon() != null) {
			tb.getNavigationIcon().setTint(ThemeManager.isLight() ? Color.BLACK : Color.WHITE);
		}
		
		if (tb.getOverflowIcon() != null) {
			tb.getOverflowIcon().setTint(ThemeManager.isLight() ? Color.BLACK : Color.WHITE);
		}
	}
	
	
	public static int AttrColor(@AttrRes int id) {
        int[] attr = new int[] {id};
        TypedArray a = AppGlobal.context.obtainStyledAttributes(attr);
        int value = a.getDimensionPixelSize(0, 0);
        a.recycle();
        return value;
    }
	
	public static void showKeyboard(View v) {
		keyboard.showSoftInput(v, 0);
	}
	
	public static void hideKeyboard(View v) {
		keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
    static @ColorInt
    int getColor(int resId) {
        return AppGlobal.context.getResources().getColor(resId);
    }
}
