package ru.stwtforever.schedule.fragment;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v7.widget.*;
import android.view.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.*;
import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.common.*;
import ru.stwtforever.schedule.util.ViewUtil;
import ru.stwtforever.schedule.util.*;


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
