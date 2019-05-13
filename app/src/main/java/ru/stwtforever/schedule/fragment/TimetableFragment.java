package ru.stwtforever.schedule.fragment;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import org.greenrobot.eventbus.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.*;
import ru.stwtforever.schedule.adapter.items.*;
import ru.stwtforever.schedule.common.*;
import ru.stwtforever.schedule.db.*;
import ru.stwtforever.schedule.helper.*;
import ru.stwtforever.schedule.util.*;
import ru.stwtforever.schedule.view.*;

import android.support.v7.widget.Toolbar;

public class TimetableFragment extends Fragment implements Toolbar.OnMenuItemClickListener, TimetableAdapter.OnGroupLongClickListener, TimetableAdapter.OnGroupClickListener {

    private ExpandableListView list;
	private Toolbar tb;
	private SwipeRefreshLayout refresh;

    private TimetableAdapter adapter;
	
	private BellItem item;
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onReceive(Object[] data) {
		if (ArrayUtil.isEmpty(data)) return;

		String key = (String) data[0];
		if (key.equals("bells_update")) {
			getTimetable();
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		initViews(view);
		EventBus.getDefault().register(this);

		tb.setTitle(R.string.timetable);
		tb.getMenu().add(R.string.recreate_all_bells);
		tb.setOnMenuItemClickListener(this);

		refresh.setProgressBackgroundColorSchemeColor(ThemeManager.getBackground());
		refresh.setColorSchemeColors(ThemeManager.getMain());
		refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

				@Override
				public void onRefresh() {
					getTimetable();
				}
			});

        getTimetable();
		
		list.setIndicatorBoundsRelative(10, 0);
		list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView view, View p2, int groupPosition, int childPosition, long p5) {
					showAlert(groupPosition, childPosition);
					return true;
				}
			});
	}

	private void getTimetable() {
		refresh.setRefreshing(true);

		String[] days = getResources().getStringArray(R.array.days);
		ArrayList<TimetableItem> items = new ArrayList<>(6);

		for (int i = 0; i < 6; i++) {
			TimetableItem item = new TimetableItem();
			ArrayList<BellItem> bells = CacheStorage.getBells(i);
			item.setBells(bells);
			item.setTitle(days[i]);
			items.add(item);
		}

		createAdapter(items);
		refresh.setRefreshing(false);
	}

	private void createAdapter(ArrayList<TimetableItem> items) {
		if (adapter == null) {
			adapter = new TimetableAdapter(getActivity(), items);
			adapter.setOnGroupClickListener(this);
			adapter.setOnGroupLongClickListener(this);
			list.setAdapter(adapter);

			expandCurrentDay();
			return;
		}

		adapter.setItems(items);
		adapter.notifyDataSetChanged();

		expandCurrentDay();
	}

	private void expandCurrentDay() {
		if (!AppGlobal.preferences.getBoolean(SettingsFragment.KEY_EXPAND_CURRENT_DAY, true)) return;
		int currentDay = Utils.getNumOfCurrentDay();

		for (int i = 0; i < 6; i++)
			if (i != currentDay)
				list.collapseGroup(i);
			else
				list.expandGroup(currentDay, true);
				
		list.setSelectedGroup(currentDay);
	}
	
	@Override
	public void onGroupClick(View v, int position) {
		if (list.isGroupExpanded(position)) {
			list.collapseGroup(position);
		} else {
			list.expandGroup(position, true);
		}
	}

	@Override
	public void onGroupLongClick(View v, int position) {
		showGroupDialog(position);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.getTitle().toString().equals(getString(R.string.recreate_all_bells))) {
			showConfirmRecreateDialog();
		}
		return true;
	}

	private void showAlert(final int groupPosition, final int childPosition) {
		String[] items = new String[] {getString(R.string.edit), getString(R.string.delete)};

		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setItems(items, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int i) {
					if (i == 0) {
						showEditDialog(groupPosition, childPosition);
					} else if (i == 1) {
						showConfirmationDeleteDialog(groupPosition, childPosition);
					}
				}
			});

		adb.create().show();
	}

	private void showGroupDialog(final int position) {
		String[] items = new String[] {getString(R.string.add), getString(R.string.delete), getString(R.string.recreate_bells)};

		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setItems(items, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int i) {
					if (i == 0) showAddDialog(position, false);
					else if (i == 1) showDeleteGroupDialog(position);
					else if (i == 2) showConfirmRecreateDialog(position);
				}
			});

		adb.create().show();
	}

	private void showDeleteGroupDialog(final int position) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(R.string.warning);
		adb.setMessage(R.string.confirm_delete_text);
		adb.setNegativeButton(android.R.string.no, null);
		adb.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int i) {
					CacheStorage.delete(DatabaseHelper.TABLE_BELLS, "day = " + position);
					adapter.getChilds(position).clear();
					adapter.notifyDataSetChanged();
				}
			});
		adb.create().show();
	}
	
	private void showAddDialog(final int groupPosition, final boolean isEnd) {
		final TimePickerDialog p = new TimePickerDialog(getContext(), true);
		p.setHintTime(0, 0);

		p.setOnChoosedTimeListener(new TimePickerDialog.OnChoosedTimeListener() {

				@Override
				public void onChoosedTime(int hours, int minutes) {
					String hour_ = String.valueOf(hours);
					String minute_ = String.valueOf(minutes);
					
					if (isEnd) {
						int hour = Integer.parseInt(hour_);
						int minute = Integer.parseInt(minute_);

						item.setDay(groupPosition);
						item.setEnd(hour * 60 + minute);

						adapter.getChilds(groupPosition).add(item);
						
						CacheStorage.insert(DatabaseHelper.TABLE_BELLS, item);
						
						adapter.notifyDataSetChanged();
						list.expandGroup(groupPosition, true);
						item = null;
					} else {
						int hour = Integer.parseInt(hour_);
						int minute = Integer.parseInt(minute_);

						item = new BellItem();
						item.setStart(hour * 60 + minute);

						showAddDialog(groupPosition, true);
					}
				}
			});
		p.show();
	}

	private void showEditDialog(int groupPosition, int childPosition) {
		showEditDialog(groupPosition, childPosition, false);
	}

	private void showEditDialog(final int groupPosition, final int childPosition, final boolean isEnd) {
		final BellItem item = adapter.getGroups().get(groupPosition).getBells().get(childPosition);

		TimePickerDialog dialog = new TimePickerDialog(getContext(), true);

		String hours, minutes;
		if (isEnd) {
			hours = item.getEndHours();
			minutes = item.getEndMinutes();
		} else {
			hours = item.getStartHours();
			minutes = item.getStartMinutes();
		}
		
		int h = Integer.parseInt(hours), m = Integer.parseInt(minutes);

		dialog.setTime(h, m);
		dialog.setHintTime(h, m);
		dialog.setOnChoosedTimeListener(new TimePickerDialog.OnChoosedTimeListener() {

				@Override
				public void onChoosedTime(int hours, int minutes) {
					if (isEnd) {
						String end = hours + ":" + minutes;
						item.setEnd(end);

						adapter.notifyDataSetChanged();
					} else {
						String start = hours + ":" + minutes;
						item.setStart(start);

						adapter.notifyDataSetChanged();
						showEditDialog(groupPosition, childPosition, true);
					}

					CacheStorage.update(DatabaseHelper.TABLE_BELLS, item, "id = ?", item.id);
				}
			});

		dialog.show();
	}

	private void showConfirmationDeleteDialog(final int groupPosition, final int childPosition) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(R.string.warning);
		adb.setMessage(R.string.confirm_delete_text);
		adb.setNegativeButton(R.string.no, null);
		adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int i) {
					BellItem bell = adapter.getGroups().get(groupPosition).getBells().get(childPosition);

					CacheStorage.delete(DatabaseHelper.TABLE_BELLS, "id = " + bell.id);
					adapter.getGroups().get(groupPosition).getBells().remove(childPosition);
					adapter.notifyDataSetChanged();
				}
			});

		adb.create().show();
	}

	private void showConfirmRecreateDialog(final int day) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(R.string.warning);
		adb.setMessage(getString(R.string.confirm_recreate_bells_on_day, Utils.getStringDay(day)));
		adb.setNegativeButton(R.string.no, null);
		adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2) {
					CacheStorage.delete(DatabaseHelper.TABLE_BELLS, DatabaseHelper.DAY + " = " + day);

					TimeHelper.load(day);

					adapter.getGroups().get(day).setBells(CacheStorage.getBells(day));
					adapter.notifyDataSetChanged();
				}
			});
		adb.create().show();
	}

	private void showConfirmRecreateDialog() {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(R.string.warning);
		adb.setMessage(R.string.confirm_recreate_bells);
		adb.setNegativeButton(R.string.no, null);
		adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2) {
					CacheStorage.delete(DatabaseHelper.TABLE_BELLS);
					TimeHelper.load();

					getTimetable();
				}
			});
		adb.create().show();
	}

    private void initViews(View v) {
		tb = v.findViewById(R.id.toolbar);
		refresh = v.findViewById(R.id.refresh);
        list = v.findViewById(R.id.list);
    }
}
