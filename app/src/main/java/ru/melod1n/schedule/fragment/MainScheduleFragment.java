package ru.melod1n.schedule.fragment;

import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.MainActivity;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.adapter.ScheduleMainAdapter;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.util.Util;
import ru.melod1n.schedule.widget.Toolbar;


public class MainScheduleFragment extends Fragment {

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SearchView searchView;
    private MenuItem searchViewItem;

    private boolean searchViewCollapsed = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        prepareToolbar();

        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        createPagerAdapter();
    }

    private void prepareToolbar() {
        toolbar.setTitle(R.string.nav_schedule);
        toolbar.inflateMenu(R.menu.fragment_main_schedule);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

        searchViewItem = toolbar.getMenu().findItem(R.id.schedule_search);

        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint(getString(R.string.title));

        searchView.setOnCloseListener(() -> {
            searchViewCollapsed = true;
            return false;
        });

        searchView.setOnSearchClickListener(view -> searchViewCollapsed = false);
    }

    public MenuItem getSearchViewItem() {
        return searchViewItem;
    }

    public boolean isSearchViewCollapsed() {
        return searchViewCollapsed;
    }

    private boolean onMenuItemClick(@NonNull MenuItem item) {
        return true;
    }

    private void createPagerAdapter() {
        pager.setAdapter(new ScheduleMainAdapter(getChildFragmentManager()));
        pager.computeScroll();
        pager.setOffscreenPageLimit(5);
        tabs.setupWithViewPager(pager);

        if (AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SELECT_CURRENT_DAY, true))
            pager.setCurrentItem(Util.getNumOfCurrentDay());
    }
}
