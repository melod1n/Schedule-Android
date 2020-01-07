package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.TintTypedArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ArrayUtil;

public class TextPlain extends AppCompatTextView {

    private ThemeItem theme;

    private int colorDef;

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

        if (a.hasValue(R.styleable.TextPlain_colorDef)) {
            colorDef = a.getInt(R.styleable.TextPlain_colorDef, 1);
        } else {
            colorDef = 1;
        }

        theme = ThemeEngine.getCurrentTheme();

        init();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo<ThemeItem> info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = info.getData();
            init();
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

    private int getColor(int def) {
        switch (def) {
            case 0:
                return theme.getColorTextPrimary();
            default:
            case 1:
                return theme.getColorTextSecondary();
            case 2:
                return theme.getColorAccent();
        }
    }
}
