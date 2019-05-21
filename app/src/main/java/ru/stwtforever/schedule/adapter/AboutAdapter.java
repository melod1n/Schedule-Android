package ru.stwtforever.schedule.adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.adapter.items.AboutItem;
import ru.stwtforever.schedule.common.ThemeManager;
import ru.stwtforever.schedule.util.ColorUtil;

public class AboutAdapter extends RecyclerAdapter<AboutItem, AboutAdapter.ViewHolder> {

    private @ColorInt
    int cardColor;

    public AboutAdapter(Context context, ArrayList<AboutItem> items) {
        super(context, items);

        cardColor = ColorUtil.lightenColor(ThemeManager.getAboutBackground(), 1.4f);
    }

    @NonNull
    @Override
    public AboutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.activity_about_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutAdapter.ViewHolder holder, final int position) {
        updateListeners(holder.itemView, position);
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;

        TextView name;
        TextView job;

        CardView card;

        ViewHolder(View v) {
            super(v);

            card = v.findViewById(R.id.card);

            icon = v.findViewById(R.id.icon);

            name = v.findViewById(R.id.name);
            job = v.findViewById(R.id.job);
        }

        void bind(final int position) {
            final AboutItem item = getItem(position);
            initViews(position);

            card.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int x = (card.getLeft() + card.getRight()) / 2;
                    int y = (card.getTop() + card.getBottom()) / 2;

                    int startRadius = 0;//(int) Math.hypot(v.getWidth() / 5, v.getHeight() / 5);
                    int endRadius = Math.max(card.getWidth(), card.getHeight());

                    Animator anim = ViewAnimationUtils.createCircularReveal(card, x, y, startRadius, endRadius);

                    card.setCardBackgroundColor(item.isExpanded() ? item.getSelectedColor() : cardColor);
                    anim.setDuration(300);
                    anim.start();

                    card.setVisibility(View.VISIBLE);

                    item.setExpanded(!item.isExpanded());
                    initViews(position);
                }
            });
        }

        private void initViews(int position) {
            final AboutItem item = getItem(position);

            icon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View p1) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            if (item.isExpanded()) {
                boolean dark = ColorUtil.isDark(item.getSelectedColor());

                card.setCardBackgroundColor(item.getSelectedColor());

                Picasso.get().load(item.getSelectedIcon()).into(icon);
                icon.setClickable(true);

                name.setText(item.getTitle());
                job.setText(item.getLink());

                name.setTextColor(dark ? Color.WHITE : Color.DKGRAY);
                job.setTextColor(dark ? Color.LTGRAY : Color.GRAY);
            } else {
                boolean dark = ColorUtil.isDark(cardColor);

                if (item.getIcon() == null) {
                    icon.setImageDrawable(null);
                    icon.setVisibility(View.GONE);
                } else {
                    Picasso.get().load(item.getIcon()).resize(128, 128).config(Bitmap.Config.ARGB_8888).into(icon);
                    icon.setVisibility(View.VISIBLE);
                }

                icon.setClickable(false);

                name.setText(item.getName());
                job.setText(item.getJob());

                name.setTextColor(dark ? Color.WHITE : Color.DKGRAY);
                job.setTextColor(dark ? Color.LTGRAY : Color.GRAY);

                card.setCardBackgroundColor(cardColor);
            }
        }
    }
}
