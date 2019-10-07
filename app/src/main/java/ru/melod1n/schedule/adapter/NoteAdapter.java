package ru.melod1n.schedule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.items.NoteItem;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.database.CacheStorage;
import ru.melod1n.schedule.database.DatabaseHelper;
import ru.melod1n.schedule.util.ColorUtil;

public class NoteAdapter extends RecyclerAdapter<NoteItem, NoteAdapter.ViewHolder> {

    public NoteAdapter(Context context, ArrayList<NoteItem> items) {
        super(context, items);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onEndMove(int fromPosition, int toPosition) {
        CacheStorage.delete(DatabaseHelper.TABLE_NOTES);
        CacheStorage.insert(DatabaseHelper.TABLE_NOTES, getValues());

        notifyItemRangeChanged(0, getItemCount(), getItem(toPosition));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.fragment_notes_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.card)
        CardView card;

        final int[] colors = ThemeManager.isDark() ? ThemeManager.COLOR_PALETTE_DARK : ThemeManager.COLOR_PALETTE_LIGHT;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            card.setCardElevation(4);
        }

        void bind(int position) {
            NoteItem item = getItem(position);

            card.setCardBackgroundColor(colors[item.getPosition()]);

            int textColor = ColorUtil.isDark(colors[item.getPosition()]) ? Color.WHITE : Color.BLACK;

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
