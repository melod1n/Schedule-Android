package ru.stwtforever.schedule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.adapter.items.NoteItem;
import ru.stwtforever.schedule.db.CacheStorage;
import ru.stwtforever.schedule.db.DatabaseHelper;
import ru.stwtforever.schedule.util.ColorUtil;

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

        TextView title, text;

        CardView card;

        ViewHolder(View v) {
            super(v);

            card = v.findViewById(R.id.card);
            card.setCardElevation(4);

            title = v.findViewById(R.id.title);
            text = v.findViewById(R.id.text);
        }

        void bind(int position) {
            NoteItem item = getItem(position);

            card.setCardBackgroundColor(item.getColor());

            int textColor = ColorUtil.isDark(item.getColor()) ? Color.WHITE : Color.BLACK;

            title.setTextColor(textColor);
            text.setTextColor(textColor);

            if (!TextUtils.isEmpty(item.getTitle())) {
                String title_ = item.getTitle().length() > 35 ? item.getTitle().substring(0, 35) + "..." : item.getTitle();
                title.setText(title_);
                title.setVisibility(title_.trim().isEmpty() ? View.GONE : View.VISIBLE);
            }

            String text_ = item.getText().length() > 120 ? item.getText().substring(0, 120) + "..." : item.getText();
            text.setText(text_);

            text.setVisibility(text_.trim().isEmpty() ? View.GONE : View.VISIBLE);

        }
    }
}
