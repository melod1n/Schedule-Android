package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.DayFragmentAdapter;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.util.Util;
import ru.melod1n.schedule.util.ViewUtil;


public class ParentSubjectsFragment extends Fragment {

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.toolbar)
    Toolbar tb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

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
}
