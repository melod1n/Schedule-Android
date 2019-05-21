package ru.stwtforever.schedule.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.common.AppGlobal;
import ru.stwtforever.schedule.fragment.DayFragment;

public class DayFragmentAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 6;

    public DayFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        return new DayFragment(position);
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
