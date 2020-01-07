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
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.BaseAdapter;
import ru.melod1n.schedule.current.BaseHolder;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.items.NoteItem;
import ru.melod1n.schedule.util.ColorUtil;

public class NoteAdapter extends BaseAdapter<NoteItem, NoteAdapter.ViewHolder> {

    public NoteAdapter(Context context, ArrayList<NoteItem> items) {
        super(context, items);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onEndMove(int toPosition) {
        CacheStorage.delete(DatabaseHelper.TABLE_NOTES);
        CacheStorage.insert(DatabaseHelper.TABLE_NOTES, getValues());

        notifyItemRangeChanged(0, getItemCount(), getItem(toPosition));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = getInflater().inflate(R.layout.fragment_notes_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public boolean onQueryItem(NoteItem item, String lowerQuery) {
        if (item.getTitle().toLowerCase().contains(lowerQuery) || item.getText().toLowerCase().contains(lowerQuery))
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

        final int[] colors = ThemeEngine.isDark() ? ThemeEngine.COLOR_PALETTE_DARK : ThemeEngine.COLOR_PALETTE_LIGHT;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            card.setCardElevation(4);
        }

        @Override
        public void bind(int position) {
            NoteItem item = getItem(position);

            card.setCardBackgroundColor(colors[item.getPosition()]);

            int textColor = ColorUtil.isDark(colors[item.getPosition()]) ? Color.WHITE : Color.BLACK;

            this.title.setTextColor(textColor);
            this.text.setTextColor(textColor);

            String title = item.getTitle().length() > 35 ? item.getTitle().substring(0, 35) + "..." : item.getTitle();
            this.title.setText(title);

            String text = item.getText().length() > 120 ? item.getText().substring(0, 120) + "..." : item.getText();
            this.text.setText(text);

            this.text.setVisibility(text.trim().isEmpty() ? View.GONE : View.VISIBLE);
            this.title.setVisibility(title.trim().isEmpty() ? View.GONE : View.VISIBLE);
        }
    }
}
