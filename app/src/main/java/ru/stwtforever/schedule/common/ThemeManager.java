package ru.stwtforever.schedule.common;
import android.graphics.*;
import android.support.annotation.*;
import org.greenrobot.eventbus.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.fragment.*;
import ru.stwtforever.schedule.util.*;

public class ThemeManager {
	
	private static boolean dark;
	
	private static @IntegerRes int 
	theme, 
	fullscreen_alert_theme,
	bottom_sheet_theme,
	about_theme;
	
	private static @ColorInt int 
	primary, 
	primary_dark, 
	accent, 
	background, 
	main,
	icons,
	icons_selected,
	about_background;
	
	public static final int[] COLORS = new int[] {
		Color.WHITE,
		getColor(R.color.dark_primary),
	};
	
	public static void init() {
		dark = AppGlobal.preferences.getBoolean(SettingsFragment.KEY_DARK_THEME, false);
		
		theme = isDark() ? R.style.AppTheme_Dark : R.style.AppTheme_Light;
		about_theme = isDark() ? R.style.AppTheme_About_Dark : R.style.AppTheme_About_Light;
		fullscreen_alert_theme = isDark() ? R.style.AppTheme_FullScreenDialog_Dark : R.style.AppTheme_FullScreenDialog_Light;
		
		primary = getColor(isDark() ? R.color.dark_primary : R.color.primary);
		primary_dark = getColor(isDark() ? R.color.dark_primary_dark : R.color.primary_dark);
		accent = getColor(isDark() ? R.color.accent : R.color.accent);
		background = getColor(isDark() ? R.color.dark_background : R.color.background);
		about_background = getColor(isDark() ? R.color.about_dark_bg : R.color.about_bg);
		main = getAccent();
		icons = Color.GRAY;
		icons_selected = isLight() ? getAccent() : Color.WHITE;
		bottom_sheet_theme = isLight() ? R.style.BottomSheet_Light : R.style.BottomSheet_Dark;
	}
	
	public static void switchTheme(boolean dark) {
		AppGlobal.preferences.edit().putBoolean(SettingsFragment.KEY_DARK_THEME, dark).apply();
		init();
		EventBus.getDefault().post(new Object[] {"theme_update"});
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
	
	public static int getTheme() {
		return theme;
	}
	
	public static int getAboutTheme() {
		return about_theme;
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
	
	public static int getAboutBackground() {
		return about_background;
	}
	
	private static @ColorInt int getColor(int res) {
		return AppGlobal.context.getResources().getColor(res);
	}
}
