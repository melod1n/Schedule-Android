package ru.melod1n.schedule.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.card.MaterialCardView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class CardView extends MaterialCardView {

    private ThemeItem theme;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialCardViewStyle);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        if (theme == null) theme = ThemeEngine.getCurrentTheme();

        init();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void init() {
        setCardBackgroundColor(theme.getColorSurface());
        setUseCompatPadding(true);
        setRadius(16);

        if (theme.isMd2()) {
            setStrokeColor(theme.getColorHighlight());
            setStrokeWidth(2);
            setCardElevation(0);
        } else {
            setStrokeWidth(0);
            setCardElevation(12);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(EventInfo<ThemeItem> info) {
        String key = info.getKey();
        if (EventInfo.KEY_THEME_UPDATE.equals(key)) {
            theme = info.getData();
            init();
        }
    }
}
