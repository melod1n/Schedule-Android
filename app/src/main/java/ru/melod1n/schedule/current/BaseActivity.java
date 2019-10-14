package ru.melod1n.schedule.current;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ViewUtil;

public abstract class BaseActivity extends AppCompatActivity {

    private View contentView;

    protected ThemeItem theme;

    @Override
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        theme = ThemeEngine.getCurrentTheme();
        setTheme(theme.isDark() ? R.style.AppTheme_Dark : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        ViewUtil.applyWindowStyles(getWindow());
    }

    protected void applyBackground() {
        if (contentView != null) {
            contentView.setBackgroundColor(ThemeEngine.getCurrentTheme().getColorBackground());
        }
    }
}
