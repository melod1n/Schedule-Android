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
import android.graphics.drawable.*;
import ru.stwtforever.schedule.util.*;

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
    public void onBindViewHolder(SubjectAdapter.ViewHolder holder, int position) {
		if (position > 99) return;
		super.onBindViewHolder(holder, position);
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

		private TextView name, cab, cab_title, num;
		
        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            cab = v.findViewById(R.id.cab);
			cab_title = v.findViewById(R.id.cab_title);
			num = v.findViewById(R.id.num);
			
			GradientDrawable background = new GradientDrawable();
			background.setCornerRadius(200);
			background.setColor(ThemeManager.getAccent());
			
			num.setBackground(background);
        }

		public void bind(final int position) {
			SubjectItem item = getItem(position);
			
			num.getBackground().setTint(item.getColor() == 0 ? ThemeManager.getAccent() : item.getColor());
			
			num.setTextColor(Utils.isLight(item.getColor()) ? Color.BLACK : Color.WHITE);
			
			name.setText(item.getName());
			cab.setText(item.getCab());
			
			num.setText(String.valueOf(position + 1));

			cab_title.setVisibility(TextUtils.isEmpty(item.getCab()) ? View.INVISIBLE : View.VISIBLE);
		}
    }
}
