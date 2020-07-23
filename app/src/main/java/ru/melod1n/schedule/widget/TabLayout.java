package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class TabLayout extends com.google.android.material.tabs.TabLayout {

    private ThemeItem theme;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tabStyle);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo<ThemeItem> info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = info.getData();
            init();
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        init();
    }

    private void init() {
        if (theme == null) theme = ThemeEngine.getCurrentTheme();
        setBackgroundColor(theme.getColorPrimary());
        setTabTextColors(theme.getColorTabsTextNormal(), theme.getColorTabsTextActive());
        setSelectedTabIndicatorColor(theme.getColorAccent());
    }

//    void changeFontInViewGroup(ViewGroup viewGroup) {
//        for (int i = 0; i < viewGroup.getChildCount(); i++) {
//            View child = viewGroup.getChildAt(i);
//            if (TextView.class.isAssignableFrom(child.getClass())) {
//                TextView text = (TextView) child;
//                if (theme.isMd2()) {
//                    FontHelper.applyFont(this, FontHelper.Font.PS_MEDIUM);
//                    text.setText(StringUtils.capitalize(text.getText().toString().toLowerCase()));
//                } else {
//                    FontHelper.applyFont(this, FontHelper.Font.ROBOTO_REGULAR);
//                    text.setText(text.getText().toString().toUpperCase());
//                }
//            } else if (ViewGroup.class.isAssignableFrom(child.getClass())) {
//                changeFontInViewGroup((ViewGroup) viewGroup.getChildAt(i));
//            }
//        }
//    }
}
