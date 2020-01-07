package ru.melod1n.schedule.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.current.BaseAdapter;
import ru.melod1n.schedule.current.BaseHolder;
import ru.melod1n.schedule.items.AboutItem;

public class AboutAdapter extends BaseAdapter<AboutItem, AboutAdapter.ViewHolder> {

    public AboutAdapter(Context context, ArrayList<AboutItem> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = getInflater().inflate(R.layout.activity_about_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void destroy() {

    }

    class ViewHolder extends BaseHolder {

        @BindView(R.id.icon)
        ImageView icon;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.job)
        TextView job;

        @BindView(R.id.card)
        CardView card;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public void bind(final int position) {
            final AboutItem item = getItem(position);

            Picasso.get().load(item.getIcon()).resize(128, 128).config(Bitmap.Config.ARGB_8888).into(icon);

            name.setText(item.getName());
            job.setText(item.getJob());

            card.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
            });
        }
    }
}
