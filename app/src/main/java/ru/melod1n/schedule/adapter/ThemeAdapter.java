package ru.melod1n.schedule.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Locale;

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.theme_engine_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.theme_engine_card_view)
        MaterialCardView cardView;

        @BindView(R.id.theme_engine_toolbar)
        Toolbar toolbar;

        @BindView(R.id.theme_engine_text)
        AppCompatTextView textView;

        @BindView(R.id.theme_engine_switch)
        SwitchCompat switchCompat;

        @BindView(R.id.theme_engine_radio_button)
        AppCompatRadioButton radioButton;

        @BindView(R.id.theme_engine_checkbox)
        AppCompatCheckBox checkBox;

        @BindView(R.id.theme_engine_simple_button)
        MaterialButton simpleButton;

        @BindView(R.id.theme_engine_outline_button)
        MaterialButton outlineButton;

        @BindView(R.id.theme_engine_bottom_navigation_view)
        BottomNavigationView bottomNavigationView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            ThemeItem item = getItem(position);

            int colorSurface = item.getColorSurface();
            int colorPrimary = item.getColorPrimary();
            int colorAccent = item.getColorAccent();
            int colorBackground = item.getColorBackground();
            //int colorControlNormal = item.getColorControlNormal();
            int textColorPrimary = item.getColorTextPrimary();
            int textColorSecondary = item.getColorTextSecondary();
            int textColorPrimaryInverse = item.getColorTextPrimaryInverse();
            //int textColorSecondaryInverse = item.getColorTextSecondaryInverse();

            toolbar.setTitle(item.getTitle());
            toolbar.setTitleTextColor(ColorUtil.isLight(colorPrimary) ? Color.BLACK : Color.WHITE);
            toolbar.setBackgroundColor(colorPrimary);

            String textAuthor = String.format("Author: %s", item.getAuthor());
            String textVersion = String.format(Locale.getDefault(), "Engine version: %d", item.getEngineVersion());

            String text = String.format("%s\n%s", textAuthor, textVersion);

            Spannable textSpan = new SpannableString(text);
            textSpan.setSpan(new ForegroundColorSpan(textColorPrimary), 0, textAuthor.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textSpan.setSpan(new ForegroundColorSpan(textColorSecondary), textAuthor.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(textSpan);
            textView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemView, position);
                }
            });

            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };

            int[] switchTrackColors = new int[]{
                    item.alphaColor(colorAccent, 0.5f),
                    item.alphaColor(textColorSecondary, 0.5f)
            };

            int[] switchThumbColors = new int[]{
                    colorAccent,
                    ColorUtil.isDark(colorBackground) ? ColorUtil.lightenColor(textColorSecondary, 1.2f) : colorBackground
            };

            switchCompat.getThumbDrawable().setTintList(new ColorStateList(states, switchThumbColors));
            switchCompat.getTrackDrawable().setTintList(new ColorStateList(states, switchTrackColors));

            radioButton.setSupportButtonTintList(ColorStateList.valueOf(colorAccent));

            int[][] checkBoxStates = new int[][]{
                    new int[]{android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };

            int[] checkBoxColors = new int[]{
                    colorAccent,
                    item.alphaColor(textColorSecondary, 0.7f)
            };

            checkBox.setSupportButtonTintList(new ColorStateList(checkBoxStates, checkBoxColors));

            simpleButton.setTextColor(textColorPrimaryInverse);
            simpleButton.setBackgroundTintList(ColorStateList.valueOf(colorAccent));

            outlineButton.setTextColor(colorAccent);
            outlineButton.setStrokeColor(ColorStateList.valueOf(item.alphaColor(textColorSecondary, 0.3f)));
            outlineButton.setRippleColor(ColorStateList.valueOf(colorAccent));
            outlineButton.setBackgroundTintList(ColorStateList.valueOf(0));

            cardView.setCardBackgroundColor(colorBackground);

            int[][] bottomNavigationViewStates = new int[][]{
                    new int[]{android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };

            int[] bottomNavigationViewColors = new int[]{
                    item.getColorBottomBarIconsActive(),
                    item.getColorBottomBarIconsNormal()
            };

            ColorStateList colorStateList = new ColorStateList(bottomNavigationViewStates, bottomNavigationViewColors);
            bottomNavigationView.setItemIconTintList(colorStateList);
            bottomNavigationView.setItemTextColor(colorStateList);
            bottomNavigationView.setBackgroundColor(item.getColorBottomBar());
        }
    }
}
