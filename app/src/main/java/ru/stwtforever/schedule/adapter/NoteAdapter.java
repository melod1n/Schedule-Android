package ru.stwtforever.schedule.adapter;

import android.content.*;
import android.graphics.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.items.*;
import ru.stwtforever.schedule.db.*;
import ru.stwtforever.schedule.util.*;

public class NoteAdapter extends RecyclerAdapter<NoteItem, NoteAdapter.ViewHolder> {
	
    public NoteAdapter(Context context, ArrayList<NoteItem> items) {
        super(context, items);
    }
	
	public void onItemDismiss(int position) {
		getValues().remove(position);
		notifyItemRemoved(position);
	}

	public void onItemMove(int fromPosition, int toPosition) {
		notifyItemMoved(fromPosition, toPosition);
	}
	
	public void onEndMove(int fromPosition, int toPosition) {
		CacheStorage.delete(DatabaseHelper.TABLE_NOTES);
		CacheStorage.insert(DatabaseHelper.TABLE_NOTES, getValues());

		notifyItemRangeChanged(0, getItemCount(), getItem(toPosition));
	}
	
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View v = inflater.inflate(R.layout.fragment_notes_item, viewGroup, false);
		return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
		holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
		
		TextView title, text;
		
		CardView card;

        ViewHolder(View v) {
            super(v);
			
			card = v.findViewById(R.id.card);
			card.setCardElevation(4);
			
			title = v.findViewById(R.id.title);
			text = v.findViewById(R.id.text);
        }
		
		public void bind(int position) {
			NoteItem item = getItem(position);
			
			card.setCardBackgroundColor(item.getColor());
			
			int textColor = ColorUtil.isDark(item.getColor()) ? Color.WHITE : Color.BLACK;

			title.setTextColor(textColor);
			text.setTextColor(textColor);
			
			String title_ = item.getTitle().length() > 35 ? item.getTitle().substring(0, 35) + "..." : item.getTitle();
			title.setText(title_);
			
			String text_ = item.getText().length() > 120 ? item.getText().substring(0, 120) + "..." : item.getText();
			text.setText(text_);
			
			text.setVisibility(text_.trim().isEmpty() ? View.GONE : View.VISIBLE);
			title.setVisibility(title_.trim().isEmpty() ? View.GONE : View.VISIBLE);
		}
	}
}
