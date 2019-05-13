package ru.stwtforever.schedule.adapter;
import android.content.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.items.*;
import ru.stwtforever.schedule.util.*;
import ru.stwtforever.schedule.common.*;

public class TimetableAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<TimetableItem> items;
	private LayoutInflater inflater;
	
	private OnGroupLongClickListener longClick;
	private OnGroupClickListener click;
	
	public TimetableAdapter(Context context, ArrayList<TimetableItem> items) {
		this.context = context;
		this.items = items;
		this.inflater = LayoutInflater.from(context);
	}
	
	public void setItems(ArrayList<TimetableItem> items) {
		this.items = items;
	}
	
	public ArrayList<TimetableItem> getGroups() {
		return items;
	}
	
	public ArrayList<BellItem> getChilds(int groupPosition) {
		return items.get(groupPosition).getBells();
	}
	
	@Override
	public int getGroupCount() {
		return 6;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return items.get(groupPosition).getBells().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return items.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return items.get(groupPosition).getBells().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_timetable_item_header, parent, false);
		}
		
		TextView text = convertView.findViewById(R.id.title);
		text.setText(Utils.getStringDay(groupPosition));
		
		if (click != null)
			convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						click.onGroupClick(v, groupPosition);
					}
				});
				
		if (longClick != null) {
			convertView.setOnLongClickListener(new View.OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						longClick.onGroupLongClick(v, groupPosition);
						return click != null;
					}
				});
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) 
			convertView = inflater.inflate(R.layout.fragment_timetable_item, parent, false);
		
		BellItem item = (BellItem) getChild(groupPosition, childPosition);

		String id_ = String.valueOf(childPosition + 1) + ".";

		TextView id = convertView.findViewById(R.id.id);
		TextView bell = convertView.findViewById(R.id.bell);
		
		id.setText(id_);
		bell.setText(Html.fromHtml(context.getString(R.string.bell, item.getStartFull(), item.getEndFull())));
			
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void setOnGroupLongClickListener(OnGroupLongClickListener listener) {
		this.longClick = listener;
	}
	
	public void setOnGroupClickListener(OnGroupClickListener listener) {
		this.click = listener;
	}
	
	public interface OnGroupLongClickListener {
		void onGroupLongClick(View v, int position);
	}
	
	public interface OnGroupClickListener {
		void onGroupClick(View v, int position);
	}
}
