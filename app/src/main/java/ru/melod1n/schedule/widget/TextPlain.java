package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.TintTypedArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ArrayUtil;
import ru.melod1n.schedule.util.Keys;

public class TextPlain extends AppCompatTextView {

    private ThemeItem theme;

    private String colorDef;

    public TextPlain(Context context) {
        this(context, null);
    }

    public TextPlain(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public TextPlain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.TextPlain, defStyleAttr, 0);

        colorDef = a.getString(R.styleable.TextPlain_colorDef);
        if (colorDef == null || colorDef.isEmpty()) {
            colorDef = "secondary";
        }

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        theme = ThemeEngine.getCurrentTheme();

        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(@NonNull Object[] data) {
        String key = (String) data[0];
        if (Keys.THEME_UPDATE.equals(key)) {
            theme = (ThemeItem) data[1];
            init();
            requestLayout();
        }
    }

    private void init() {
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

    private int getColor(@NonNull String def) {
        switch (def) {
            case "primary":
                return theme.getColorTextPrimary();
            default:
            case "secondary":
                return theme.getColorTextSecondary();
            case "accent":
                return theme.getColorAccent();
        }
    }
}
