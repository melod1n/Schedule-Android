package ru.stwtforever.schedule.adapter;


import android.animation.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.items.*;
import ru.stwtforever.schedule.db.*;
import ru.stwtforever.schedule.fragment.*;
import ru.stwtforever.schedule.common.*;
import android.graphics.*;

public class SubjectAdapter extends RecyclerAdapter<SubjectItem, SubjectAdapter.ViewHolder> {
	
    public SubjectAdapter(DayFragment f, ArrayList<SubjectItem> items) {
        super(f, items);
    }
	
	public void onItemDismiss(int position) {
		getValues().remove(position);
		notifyItemRemoved(position);
	}
	
	public void onItemMove(int fromPosition, int toPosition) {
		notifyItemMoved(fromPosition, toPosition);
	}
	
	public void onEndMove(int fromPosition, int toPosition, int day) {
		CacheStorage.delete(DatabaseHelper.TABLE_SUBJECTS, "day=" + day);
		CacheStorage.insert(DatabaseHelper.TABLE_SUBJECTS, getValues());
		
		notifyItemRangeChanged(0, getItemCount(), getItem(toPosition));
	}
	
    @NonNull
    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup vg, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_day_item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SubjectAdapter.ViewHolder vh, int i) {
		super.onBindViewHolder(vh, i);
        vh.bind(i);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

		private TextView name, cab, homework, cab_title;
		private ImageButton edit;
		private LinearLayout tools;

        ViewHolder(View v) {
            super(v);

			homework = v.findViewById(R.id.homework);
            name = v.findViewById(R.id.name);
            cab = v.findViewById(R.id.cab);
			cab_title = v.findViewById(R.id.cab_title);
			
			tools = v.findViewById(R.id.tools);
			edit = v.findViewById(R.id.edit);
        }

		public void bind(final int position) {
			final SubjectItem item = getItem(position);
			initViews(position, false);
			
			itemView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						tools.setVisibility(item.isExpanded() ? View.GONE : View.VISIBLE);
						item.setExpanded(!item.isExpanded());
						initViews(position, true);
					}
			});
		}
		
		private void initViews(final int position, boolean from_anim) {
			SubjectItem item = getItem(position);

			String num = (position + 1) + ". ";

			name.setText(num + item.getName());
			cab.setText(item.getCab());

			cab_title.setVisibility(TextUtils.isEmpty(item.getCab()) ? View.INVISIBLE : View.VISIBLE);

			homework.setText(TextUtils.isEmpty(item.getHomework()) ? getString(R.string.empty) : item.getHomework());

			if (!from_anim)
				tools.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);

			edit.getDrawable().setTint(ThemeManager.isLight() ? Color.BLACK : Color.WHITE);
			edit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						((DayFragment) fragment).showDialog(position);
					}
				});
		}
    }
}
