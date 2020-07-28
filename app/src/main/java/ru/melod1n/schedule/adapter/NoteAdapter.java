package ru.melod1n.schedule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.current.BaseAdapter;
import ru.melod1n.schedule.current.BaseHolder;
import ru.melod1n.schedule.items.Note;
import ru.melod1n.schedule.util.ColorUtil;

public class NoteAdapter extends BaseAdapter<Note, NoteAdapter.ViewHolder> {

    public NoteAdapter(Context context, ArrayList<Note> items) {
        super(context, items);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        getItem(fromPosition).setPosition(toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onEndMove(int toPosition) {
        //TODO: не сохраняются новые позиции

        //OldCacheStorage.delete(DatabaseHelper.TABLE_NOTES);
        //OldCacheStorage.insert(DatabaseHelper.TABLE_NOTES, getValues());
        //CacheStorage.updateNotesWithDelete(getValues());
        //notifyItemRangeChanged(0, getItemCount(), getItem(toPosition));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = getInflater().inflate(R.layout.fragment_notes_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public boolean onQueryItem(Note item, String lowerQuery) {
        if (item.getTitle().toLowerCase().contains(lowerQuery) || item.getBody().toLowerCase().contains(lowerQuery))
            return true;
        return super.onQueryItem(item, lowerQuery);
    }

    @Override
    public void destroy() {

    }

    class ViewHolder extends BaseHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.card)
        CardView card;

        ViewHolder(View v) {
            super(v);

            ButterKnife.bind(this, v);

            card.setCardElevation(4);
        }

        @Override
        public void bind(int position) {
            Note item = getItem(position);

            int color = item.getColor();

            card.setCardBackgroundColor(color);

            int textColor = ColorUtil.isDark(color) ? Color.WHITE : Color.BLACK;

            this.title.setTextColor(textColor);
            this.text.setTextColor(textColor);

            String title = item.getTitle().length() > 35 ? item.getTitle().substring(0, 35) + "..." : item.getTitle();
            this.title.setText(title);

            String text = item.getBody().length() > 120 ? item.getBody().substring(0, 120) + "..." : item.getBody();
            this.text.setText(text);

            this.text.setVisibility(text.trim().isEmpty() ? View.GONE : View.VISIBLE);
            this.title.setVisibility(title.trim().isEmpty() ? View.GONE : View.VISIBLE);
        }
    }
}
