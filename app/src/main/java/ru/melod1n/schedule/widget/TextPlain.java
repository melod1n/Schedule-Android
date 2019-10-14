package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.TintTypedArray;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ArrayUtil;

public class TextPlain extends AppCompatTextView {

    private ThemeItem item;

    public TextPlain(Context context) {
        this(context, null);
    }

    public TextPlain(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public TextPlain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        item = ThemeEngine.getCurrentTheme();

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.TextPlain, defStyleAttr, 0);

        String colorDef = a.getString(R.styleable.TextPlain_colorDef);
        if (colorDef == null || colorDef.isEmpty()) {
            colorDef = "secondary";
        }

        int textColor = getColor(colorDef);
        setTextColor(textColor);

        Drawable[] drawables = getCompoundDrawables();
        if (!ArrayUtil.isEmpty(drawables)) {
            for (Drawable drawable : drawables) {
                if (drawable != null)
                    drawable.setTint(textColor);
            }
        }
    }

    private int getColor(String def) {
        switch (def) {
            case "primary":
                return item.getColorTextPrimary();
            default:
            case "secondary":
                return item.getColorTextSecondary();
            case "accent":
                return item.getColorAccent();
        }
    }
}
