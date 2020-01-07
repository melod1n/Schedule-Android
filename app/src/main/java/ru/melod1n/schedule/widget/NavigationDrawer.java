package ru.melod1n.schedule.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;

public class NavigationDrawer extends NavigationView {

    private ThemeItem theme;

    public NavigationDrawer(@NonNull Context context) {
        this(context, null);
    }

    public NavigationDrawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.navigationViewStyle);
    }

    public NavigationDrawer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        theme = ThemeEngine.getCurrentTheme();
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

    private void init() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_checked}
        };

        int[] textColors = new int[]{
                theme.getColorDrawerTextActive(),
                theme.getColorDrawerTextNormal()
        };

        int[] iconColors = new int[]{
                theme.getColorDrawerIconActive(),
                theme.getColorDrawerIconNormal()
        };

        setBackgroundColor(theme.getColorDrawer());
        setItemTextColor(new ColorStateList(states, textColors));
        setItemIconTintList(new ColorStateList(states, iconColors));

        View header = getHeaderView(0);
        header.setBackgroundColor(theme.getColorDrawerHeaderBackground());

        TextView title = header.findViewById(R.id.drawer_header_title);
        TextView subtitle = header.findViewById(R.id.drawer_header_subtitle);

        title.setTextColor(theme.getColorDrawerHeaderTitle());
        subtitle.setTextColor(theme.getColorDrawerHeaderSubtitle());
    }
}
