package ru.stwtforever.schedule.fragment;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.*;
import ru.stwtforever.schedule.adapter.items.*;
import ru.stwtforever.schedule.common.*;
import ru.stwtforever.schedule.db.*;
import ru.stwtforever.schedule.util.*;

import android.support.v7.widget.Toolbar;
import ru.stwtforever.schedule.util.ViewUtil;
import ru.stwtforever.schedule.view.*;

public class DayFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {

    private RecyclerView list;
    private SwipeRefreshLayout refresh;
    private View empty;
    private FloatingActionButton add;

    private SubjectAdapter adapter;

    private int day;

	public DayFragment() {}

    public DayFragment(int i) {
		this.day = i;
	}

    @Override
    public void onItemClick(View v, int position) {
		SubjectItem item = adapter.getItem(position);
		item.setExpanded(!item.isExpanded());
		adapter.notifyItemChanged(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		initViews(view);

		LinearLayoutManager manager = new LinearLayoutManager(getActivity());
		manager.setOrientation(OrientationHelper.VERTICAL);

		list.setLayoutManager(manager);
        list.setHasFixedSize(true);

		initDragDrop();

		refresh.setProgressBackgroundColorSchemeColor(ThemeManager.getBackground());
		refresh.setColorSchemeColors(ThemeManager.getMain());
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					getSubjects();
				}
			});

        add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showDialog();
				}
			});

        getSubjects();
	}

	private void initDragDrop() {
		ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
			int dragFrom = -1;
			int dragTo = -1;

			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				final int fromPosition = viewHolder.getAdapterPosition();
				final int toPosition = target.getAdapterPosition();

				if (dragFrom == -1) {
					dragFrom =  fromPosition;
				}
				dragTo = toPosition;

				if (fromPosition < toPosition) {
					for (int i = fromPosition; i < toPosition; i++) {
						Collections.swap(adapter.getValues(), i, i + 1);
					}
				} else {
					for (int i = fromPosition; i > toPosition; i--) {
						Collections.swap(adapter.getValues(), i, i - 1);
					}
				}

				adapter.onItemMove(fromPosition, toPosition);
				return true;
			}

			@Override
			public boolean isLongPressDragEnabled() {
				return true;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}

			@Override
			public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
				return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
								ItemTouchHelper.DOWN | ItemTouchHelper.UP);// | ItemTouchHelper.START | ItemTouchHelper.END);
			}

			@Override
			public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
				super.clearView(recyclerView, viewHolder);

				if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
					adapter.onEndMove(dragFrom, dragTo, day);
				}

				dragFrom = dragTo = -1;
			}
		};

		ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
		ith.attachToRecyclerView(list);
	}

	private void showDialog() {
		showDialog(-1);
	}

	public void showDialog(final int position) {
		FullScreenSubjectDialog dialog = FullScreenSubjectDialog.display(getFragmentManager(), position == -1 ? null : adapter.getItem(position));
		dialog.setOnDoneListener(new FullScreenSubjectDialog.OnDoneListener() {

				@Override
				public void onDone(SubjectItem item) {
					if (position == -1) {
						item.setDay(day);
						adapter.getValues().add(item);
						CacheStorage.insert(DatabaseHelper.TABLE_SUBJECTS, item);
						checkCount();
					} else {
						if (item == null) {
							CacheStorage.delete(DatabaseHelper.TABLE_SUBJECTS, "id = " + adapter.getItem(position).getId());
							adapter.remove(position);
							checkCount();
						} else {
							CacheStorage.update(DatabaseHelper.TABLE_SUBJECTS, item, "id = ?", item.getId());
							item.setExpanded(false);
						}
					}
					
					adapter.notifyDataSetChanged();
				}
			});
	}
	
	private void checkCount() {
		empty.setVisibility(adapter == null ? View.VISIBLE : adapter.getValues().size() == 0 ? View.VISIBLE : View.GONE);
	}

    private void getSubjects() {
		refresh.setRefreshing(true);

		ArrayList<SubjectItem> items = CacheStorage.getSubjects(day);
		createAdapter(items);
		checkCount();

		refresh.setRefreshing(false);
    }

	private void createAdapter(ArrayList<SubjectItem> values) {
		if (adapter == null) {
			adapter = new SubjectAdapter(this, values);
			adapter.setOnItemClickListener(this);
			list.setAdapter(adapter);
			return;
		}

		adapter.changeItems(values);
		adapter.notifyDataSetChanged();
	}

    private void initViews(View root) {
        empty = root.findViewById(R.id.no_items_container);
        list = root.findViewById(R.id.list);
        refresh = root.findViewById(R.id.refresh);
        add = root.findViewById(R.id.fab_add);
    }
}
