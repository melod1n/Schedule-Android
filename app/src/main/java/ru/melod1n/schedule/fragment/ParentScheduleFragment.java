package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.activity.MainActivity;
import ru.melod1n.schedule.adapter.ScheduleParentAdapter;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.common.Engine;
import ru.melod1n.schedule.common.EventInfo;
import ru.melod1n.schedule.util.Util;
import ru.melod1n.schedule.widget.Toolbar;


public class ParentScheduleFragment extends Fragment {

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MenuItem searchViewItem;

    private boolean searchViewCollapsed = true;

    private int currentPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_parent_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        prepareToolbar();

        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        if (getActivity() == null) return;

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        createPagerAdapter();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            ((MainActivity) requireActivity()).prepareScreenSwipe(currentPosition);
        }
        super.onHiddenChanged(hidden);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceive(@NotNull Object[] data) {
        String key = (String) data[0];

        switch (key) {
            case SettingsFragment.KEY_SHOW_DATE:
                setToolbarSubtitle((Boolean) data[1]);
                break;
            case EventInfo.KEY_THEME_UPDATE:
                ((MainActivity) requireActivity()).prepareDrawerHeader();
                break;
        }
    }

    private void prepareToolbar() {
        toolbar.setTitle(R.string.nav_schedule);
        toolbar.inflateMenu(R.menu.fragment_main_schedule);

        searchViewItem = toolbar.getMenu().findItem(R.id.schedule_search);

        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint(getString(R.string.title));

        searchView.setOnCloseListener(() -> {
            searchViewCollapsed = true;
            return false;
        });

        searchView.setOnSearchClickListener(view -> searchViewCollapsed = false);
        setToolbarSubtitle(null);
    }

    private void setToolbarSubtitle(Boolean bool) {
        boolean b = bool == null ? AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SHOW_DATE, false) : bool;

        String subtitle = b ? Engine.getCurrentDate() : Engine.getInterim();

        SpannableString string = new SpannableString(subtitle);
        string.setSpan(new AbsoluteSizeSpan(Util.px(14)), 0, subtitle.length(), 0);

        toolbar.setSubtitle(string);
    }

    public MenuItem getSearchViewItem() {
        return searchViewItem;
    }

    public boolean isSearchViewCollapsed() {
        return searchViewCollapsed;
    }

    private void createPagerAdapter() {
        pager.setAdapter(new ScheduleParentAdapter(requireContext(), getChildFragmentManager()));
        pager.setOffscreenPageLimit(5);
        tabs.setupWithViewPager(pager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                ((MainActivity) requireActivity()).prepareScreenSwipe(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SELECT_CURRENT_DAY, false))
            pager.setCurrentItem(Util.getNumOfCurrentDay());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
