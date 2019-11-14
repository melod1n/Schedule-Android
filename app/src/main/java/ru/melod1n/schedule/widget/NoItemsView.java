package ru.melod1n.schedule.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.common.TimeManager;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.Keys;

public class NoItemsView extends AppCompatTextView {

    private ThemeItem theme;

    public NoItemsView(Context context) {
        this(context, null);
    }

    public NoItemsView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public NoItemsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        theme = ThemeEngine.getCurrentTheme();
        init();

        initIcon(TimeManager.getCurrentHour());
        TimeManager.addOnHourChangeListener(this::initIcon);
    }

    private void initIcon(int currentHour) {
        String subtitle = "";

        if (currentHour > 6 && currentHour < 12) { //morning
            subtitle += "пичка утра";
        } else if (currentHour > 12 && currentHour < 17) { //afternoon
            subtitle += "пикча дня";
        } else if (currentHour > 17 && currentHour < 23) { //evening
            subtitle += "пикча вечера";
        } else { //night
            subtitle += "пикча ночи";
        }

        if (getText() == null || getText().toString().isEmpty()) return;

        String text = getContext().getString(R.string.no_items_title) + "\n(" + subtitle + ")";

        NoItemsView.this.post(() -> setText(text));
    }

    private void init() {
        int textColor = theme.getColorTextSecondary();
        setTextColor(textColor);

        Drawable icon = getContext().getDrawable(R.drawable.ic_no_items);
        if (icon == null) return;
        icon.setTint(textColor);

        setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(Object[] data) {
        String key = (String) data[0];
        if (Keys.THEME_UPDATE.equals(key)) {
            theme = (ThemeItem) data[1];
            init();
        }
    }
}
