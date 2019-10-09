package ru.melod1n.schedule.current;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ColorUtil;

public abstract class BaseActivity extends AppCompatActivity {

    private View contentView;

    protected ThemeItem theme;

    @Override
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        theme = ThemeManager.getCurrentTheme();
        setTheme(theme.isDark() ? R.style.AppTheme_Dark : R.style.AppTheme);

        super.onCreate(savedInstanceState);

        int primaryDark = theme.getColorPrimaryDark();

        getWindow().setStatusBarColor(primaryDark);
        getWindow().setNavigationBarColor(primaryDark);

        if (ColorUtil.isLight(primaryDark)) {
            int visibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                getWindow().setNavigationBarColor(ColorUtil.darkenColor(primaryDark));
            }

            getWindow().getDecorView().setSystemUiVisibility(visibility);
        }
    }

    protected void applyBackground() {
        if (contentView != null) {
            contentView.setBackgroundColor(ThemeManager.getCurrentTheme().getColorBackground());
        }
    }
}
