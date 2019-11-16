package ru.melod1n.schedule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ColorUtil;

public class ThemeAdapter extends RecyclerAdapter<ThemeItem, ThemeAdapter.ViewHolder> {

    public ThemeAdapter(Context context, ArrayList<ThemeItem> values) {
        super(context, values);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.theme_engine_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.theme_engine_title)
        TextView textView;

        @BindView(R.id.theme_engine_card_view)
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            ThemeItem item = getItem(position);

            int colorPrimary = item.getColorPrimary();

            Spanned text;

            if (!StringUtils.isEmpty(item.getAuthor())) {
                text = Html.fromHtml(String.format("<b>%s</b> – %s", item.getTitle(), item.getAuthor()));
            } else {
                text = Html.fromHtml(String.format("<b>%s</b>", item.getTitle()));
            }

            textView.setTextColor(ColorUtil.isLight(colorPrimary) ? Color.BLACK : Color.WHITE);
            textView.setText(text);

            cardView.setCardBackgroundColor(colorPrimary);
            cardView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemView, position);
                }
            });
        }
    }
}
