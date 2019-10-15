package ru.melod1n.schedule.widget;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.melod1n.schedule.util.Keys;

public class DrawerToggle extends ActionBarDrawerToggle {

    private ru.melod1n.schedule.widget.Toolbar toolbar;

    public DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        this.toolbar = (ru.melod1n.schedule.widget.Toolbar) toolbar;
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(@NonNull Object[] data) {
        String key = (String) data[0];
        if (Keys.THEME_UPDATE.equals(key)) {
            init();
        }
    }

    private void init() {
        getDrawerArrowDrawable().setColor(toolbar.getTitleColor());
    }
}
