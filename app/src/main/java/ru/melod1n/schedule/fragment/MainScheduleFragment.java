package ru.melod1n.schedule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
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


public class MainScheduleFragment extends Fragment {

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.nav_schedule);

//        toolbar.inflateMenu(R.menu.activity_main);
//        toolbar.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.settings) {
//                if (getActivity() == null) return false;
//
//                ((MainActivity) getActivity()).replaceFragment(new SettingsFragment());
//                return true;
//            }
//
//            return false;
//        });

        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        DrawerLayout drawerLayout = ((MainActivity) getActivity()).getDrawerLayout();

        ActionBarDrawerToggle toggle = ((MainActivity) getActivity()).initToggle(toolbar, view);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        createPagerAdapter();
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
