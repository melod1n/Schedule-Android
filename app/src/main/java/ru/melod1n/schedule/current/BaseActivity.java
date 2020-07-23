package ru.melod1n.schedule.current;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ViewUtil;

public abstract class BaseActivity extends AppCompatActivity {

    private View contentView;

    protected ThemeItem theme;
    protected boolean created;

    @Override
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        created = true;
    }

    protected void init() {
        if (theme == null) theme = ThemeEngine.getCurrentTheme();

        setTheme(theme.isDark() ? R.style.AppTheme_Dark : R.style.AppTheme);

        ViewUtil.applyWindowStyles(getWindow());

        if (!EventBus.getDefault().isRegistered(this))
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

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        created = false;
        super.onDestroy();
    }

    protected void applyBackground() {
        if (contentView != null) {
            contentView.setBackgroundColor(ThemeEngine.getCurrentTheme().getColorBackground());
        }
    }
}
