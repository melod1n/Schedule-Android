package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputEditText;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class TextArea extends TextInputEditText {

    private Drawable cursorDrawable;

    public TextArea(Context context) {
        this(context, null);
    }

    public TextArea(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public TextArea(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ThemeItem themeItem = ThemeEngine.getCurrentTheme();

        cursorDrawable = getContext().getDrawable(R.drawable.edit_text_cursor);

        int colorAccent = themeItem.getColorAccent();

    }

}
