package ru.melod1n.schedule.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class FloatingActionButton extends com.google.android.material.floatingactionbutton.FloatingActionButton {


    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.floatingActionButtonStyle);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ThemeItem theme = ThemeEngine.getCurrentTheme();

        int colorAccent = theme.getColorAccent();
        int textColorPrimaryInverse = theme.getColorTextPrimaryInverse();

        setSupportBackgroundTintList(ColorStateList.valueOf(colorAccent));

        if (getDrawable() != null) {
            getDrawable().setTint(textColorPrimaryInverse);
        }
    }
}
