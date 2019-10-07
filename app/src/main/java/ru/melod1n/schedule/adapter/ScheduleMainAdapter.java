package ru.melod1n.schedule.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.fragment.ScheduleFragment;

public class ScheduleMainAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 6;

    public ScheduleMainAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        return new ScheduleFragment(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return AppGlobal.context.getResources().getStringArray(R.array.days)[position];
    }

}
