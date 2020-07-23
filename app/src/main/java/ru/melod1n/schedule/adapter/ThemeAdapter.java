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

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.current.BaseAdapter;
import ru.melod1n.schedule.current.BaseHolder;
import ru.melod1n.schedule.items.ThemeItem;
import ru.melod1n.schedule.util.ColorUtil;

public class ThemeAdapter extends BaseAdapter<ThemeItem, ThemeAdapter.ViewHolder> {

    public ThemeAdapter(Context context, ArrayList<ThemeItem> values) {
        super(context, values);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.theme_engine_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull @NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public void destroy() {

    }

    class ViewHolder extends BaseHolder {

        @BindView(R.id.theme_engine_title)
        TextView textView;

        @BindView(R.id.theme_engine_card_view)
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(int position) {
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
                if (getOnItemClickListener() != null) {
                    getOnItemClickListener().onItemClick(itemView, position);
                }
            });
        }
    }
}
