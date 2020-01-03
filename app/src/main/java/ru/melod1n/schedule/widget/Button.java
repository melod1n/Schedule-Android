package ru.melod1n.schedule.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.helper.FontHelper;
import ru.melod1n.schedule.items.ThemeItem;

public class Button extends MaterialButton {

    public static final int STYLE_DEFAULT = 0;
    public static final int STYLE_OUTLINE = 1;

    private ThemeItem theme;

    private int style;

    public Button(Context context) {
        this(context, null);
    }

    public Button(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialButtonStyle);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Button, 0, 0);

        if (a.hasValue(R.styleable.Button_style)) {
            this.style = a.getInt(R.styleable.Button_style, 0);
        }

        if (theme == null) theme = ThemeEngine.getCurrentTheme();
        init();

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = (ThemeItem) info.getData();
            init();
        }
    }

    private void init() {
        int colorAccent = theme.getColorAccent();
        int textColorPrimaryInverse = theme.getColorTextPrimaryInverse();
        int textColorSecondary = theme.getColorTextSecondary();

        if (theme.isMd2()) {
            FontHelper.applyFont(this, FontHelper.Font.PS_MEDIUM);
            setText(StringUtils.capitalize(getText().toString().toLowerCase()));
        } else {
            FontHelper.applyFont(this, FontHelper.Font.ROBOTO_REGULAR);
            setText(getText().toString().toUpperCase());
        }

        if (style == STYLE_DEFAULT) {
            setTextColor(textColorPrimaryInverse);
            setBackgroundTintList(ColorStateList.valueOf(colorAccent));
        } else if (style == STYLE_OUTLINE) {
            setTextColor(colorAccent);
            setStrokeColor(ColorStateList.valueOf(theme.alphaColor(textColorSecondary, 0.3f)));
            setRippleColor(ColorStateList.valueOf(colorAccent));
            setBackgroundTintList(ColorStateList.valueOf(0));
        }
    }
}
