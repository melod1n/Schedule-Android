package ru.stwtforever.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.adapter.DayFragmentAdapter;
import ru.stwtforever.schedule.common.AppGlobal;
import ru.stwtforever.schedule.util.Util;
import ru.stwtforever.schedule.util.ViewUtil;


public class DayTabFragment extends Fragment {

    private ViewPager pager;
    private TabLayout tabs;
    private Toolbar tb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initViews(view);

        tb.setTitle(R.string.schedule);

        ViewUtil.applyToolbarStyles(tb);

        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        createPagerAdapter();
    }

    private void createPagerAdapter() {
        pager.setAdapter(new DayFragmentAdapter(getChildFragmentManager()));
        pager.computeScroll();
        pager.setOffscreenPageLimit(5);
        tabs.setupWithViewPager(pager);

        if (AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SELECT_CURRENT_DAY, true))
            pager.setCurrentItem(Util.getNumOfCurrentDay());
    }

    private void initViews(View root) {
        tabs = root.findViewById(R.id.tabs);
        pager = root.findViewById(R.id.pager);
        tb = root.findViewById(R.id.toolbar);
    }
}
