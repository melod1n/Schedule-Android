package ru.stwtforever.schedule.fragment;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import org.greenrobot.eventbus.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.*;
import ru.stwtforever.schedule.adapter.items.*;
import ru.stwtforever.schedule.common.*;
import ru.stwtforever.schedule.db.*;
import ru.stwtforever.schedule.util.*;

import android.support.v7.widget.Toolbar;
import ru.stwtforever.schedule.util.ViewUtil;
import android.text.*;

public class NotesFragment extends Fragment implements RecyclerAdapter.OnItemClickListener, Toolbar.OnMenuItemClickListener {

    private RecyclerView list;
	private ItemTouchHelper ith;

	private StaggeredGridLayoutManager manager;
    private SwipeRefreshLayout refresh;
    private FloatingActionButton add;
    private View empty;

    private NoteAdapter adapter;

	private Toolbar tb;
	
	private boolean twoCollumns;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twoCollumns = AppGlobal.preferences.getBoolean("two_collumns", false);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		initViews(view);

		tb.setTitle(R.string.notes);
		tb.setOnMenuItemClickListener(this);

        manager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
		manager.setSpanCount(twoCollumns ? 1 : 2);
		list.setHasFixedSize(true);

		list.setLayoutManager(manager);

		initDragDrop();

		MenuItem item = tb.getMenu().add(R.string.set_one_collumn);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		item.setIcon(twoCollumns ? R.drawable.grid_large : R.drawable.grid);
		item.getIcon().setTint(ThemeManager.isLight() ? Color.BLACK : Color.WHITE);

		ViewUtil.applyToolbarStyles(tb);

		refresh.setProgressBackgroundColorSchemeColor(ThemeManager.getBackground());
		refresh.setColorSchemeColors(ThemeManager.getMain());
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					getNotes();
				}
			});

        add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (adapter != null) showDialog();
				}
			});

		getNotes();
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
								ItemTouchHelper.DOWN | ItemTouchHelper.UP | (manager.getSpanCount() == 2 ? (ItemTouchHelper.START | ItemTouchHelper.END) : 0));
			}

			@Override
			public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
				super.clearView(recyclerView, viewHolder);

				if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
					adapter.onEndMove(dragFrom, dragTo);
				}

				dragFrom = dragTo = -1;
			}
		};

		ith = new ItemTouchHelper(_ithCallback);
		ith.attachToRecyclerView(list);
	}

	private void getNotes() {
		refresh.setRefreshing(true);
		ArrayList<NoteItem> items = CacheStorage.getNotes();
		createAdapter(items);
		checkCount();

		refresh.setRefreshing(false);
	}

	private void showDialog() {
		showDialog(true, 0);
	}

	private void showDialog(final boolean isAdd, int position) {
		Intent i = new Intent();
		i.putExtra("add", isAdd);
		i.putExtra("position", position);

		if (adapter.getItemCount() > 0) {
			i.putExtra("item", adapter.getItem(position));
		}

		getActivity().startActivityForResult(new Intent(getContext(), NoteActivity.class).putExtras(i), 487);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (manager == null) return false;
		
		twoCollumns = manager.getSpanCount() == 2;
		AppGlobal.preferences.edit().putBoolean("two_collumns", twoCollumns).apply();
		
		item.setIcon(twoCollumns ? R.drawable.grid_large : R.drawable.grid);
		item.setTitle(twoCollumns ? R.string.set_two_collumns : R.string.set_one_collumn);

		item.getIcon().setTint(ThemeManager.isLight() ? Color.BLACK : Color.WHITE);

		manager.setSpanCount(twoCollumns ? 1 : 2);
		ith.attachToRecyclerView(null);
		initDragDrop();
		
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String key = data.getStringExtra("action");
		if (TextUtils.isEmpty(key)) return;
		switch (key) {
			case "delete":
				for (int i = 0; i < adapter.getItemCount(); i++) {
					NoteItem item = adapter.getItem(i);

					if (item.getId() == data.getIntExtra("id", 0)) {
						adapter.remove(i);
						adapter.notifyDataSetChanged();

						checkCount();
						break;
					}
				}
				break;
			case "add":
				String title = data.getStringExtra("title");
				String text = data.getStringExtra("text");
				int color = data.getIntExtra("color", 0);

				NoteItem item = new NoteItem();
				item.setColor(color);
				item.setTitle(title);
				item.setText(text);

				adapter.getValues().add(item);
				adapter.notifyDataSetChanged();

				checkCount();

				CacheStorage.insert(DatabaseHelper.TABLE_NOTES, item);
				break;
			case "edit":
				String newTitle = data.getStringExtra("title");
				String newText = data.getStringExtra("text");
				int newColor = data.getIntExtra("color", 0);
				int position = data.getIntExtra("position", 0);

				NoteItem note = adapter.getItem(position);

				note.setColor(newColor);
				note.setTitle(newTitle.trim());
				note.setText(newText.trim());

				adapter.notifyDataSetChanged();

				CacheStorage.update(DatabaseHelper.TABLE_NOTES, note, "id=?", note.getId());
				break;
		}
	}
	
	private void createAdapter(ArrayList<NoteItem> values) {
		if (adapter == null) {
			adapter = new NoteAdapter(getActivity(), values);
			adapter.setOnItemClickListener(this);
			list.setAdapter(adapter);
			return;
		}

		adapter.changeItems(values);
		adapter.notifyDataSetChanged();
	}

    private void initViews(View root) {
		tb = root.findViewById(R.id.toolbar);
        empty = root.findViewById(R.id.no_items_container);
        add = root.findViewById(R.id.fab_add);
        refresh = root.findViewById(R.id.refresh);
        list = root.findViewById(R.id.list);
    }

	private void checkCount() {
		tb.getMenu().getItem(0).setVisible(adapter == null ? false : adapter.getItemCount() > 0);
		empty.setVisibility(adapter == null ? View.VISIBLE : adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
	}

    @Override
    public void onItemClick(View v, final int position) {
		showDialog(false, position);
    }
}
