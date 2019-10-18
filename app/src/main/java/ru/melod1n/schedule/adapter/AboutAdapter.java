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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.items.AboutItem;

public class AboutAdapter extends RecyclerAdapter<AboutItem, AboutAdapter.ViewHolder> {

    private int cardColor;

    public AboutAdapter(Context context, ArrayList<AboutItem> items) {
        super(context, items);

        cardColor = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.activity_about_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutAdapter.ViewHolder holder, final int position) {
        updateListeners(holder.itemView, position);
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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

        void bind(final int position) {
            final AboutItem item = getItem(position);

            Picasso.get().load(item.getIcon()).resize(128, 128).config(Bitmap.Config.ARGB_8888).into(icon);

            name.setText(item.getName());
            job.setText(item.getJob());

            card.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            });
        }
    }
}
