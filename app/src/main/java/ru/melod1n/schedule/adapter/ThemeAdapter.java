package ru.melod1n.schedule.adapter;

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
import androidx.fragment.app.Fragment;
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
import ru.melod1n.schedule.util.ViewUtil;

public class ThemeAdapter extends RecyclerAdapter<ThemeItem, ThemeAdapter.ViewHolder> {


    public ThemeAdapter(Fragment fragment, ArrayList<ThemeItem> values) {
        super(fragment, values);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.theme_engine_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
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

            int colorSurface = Color.parseColor(item.getColorSurface());
            int colorPrimary = Color.parseColor(item.getColorPrimary());
            int colorAccent = Color.parseColor(item.getColorAccent());
            int colorBackground = Color.parseColor(item.getColorBackground());
            int colorControlNormal = Color.parseColor(item.getColorControlNormal());
            int textColorPrimary = Color.parseColor(item.getColorTextPrimary());
            int textColorSecondary = Color.parseColor(item.getColorTextSecondary());
            int textColorPrimaryInverse = Color.parseColor(item.getColorTextPrimaryInverse());
            int textColorSecondaryInverse = Color.parseColor(item.getColorTextSecondaryInverse());

            toolbar.setTitle(item.getName());
            toolbar.setTitleTextColor(textColorPrimary);
            toolbar.setBackgroundColor(colorPrimary);
            toolbar.setOverflowIcon(getDrawable(R.drawable.ic_more_vert));
            ViewUtil.applyToolbarStyles(toolbar, colorControlNormal, item.isDark());

            String textAuthor = String.format("Author: %s", item.getMadeBy());
            String textVersion = String.format(Locale.getDefault(), "Engine version: %d", item.getEngineVersion());

            String text = String.format("%s\n%s", textAuthor, textVersion);

            Spannable textSpan = new SpannableString(text);
            textSpan.setSpan(new ForegroundColorSpan(textColorPrimary), 0, textAuthor.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textSpan.setSpan(new ForegroundColorSpan(textColorSecondary), textAuthor.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(textSpan);

            int[][] switchTrackStates = new int[][]{
                    new int[]{android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };

            int[] switchTrackColors = new int[]{
                    item.alphaColor(colorAccent, 0.5f),
                    item.alphaColor(textColorSecondary, 0.5f)
            };

            int[][] switchThumbStates = new int[][]{
                    new int[]{android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };

            int[] switchThumbColors = new int[]{
                    colorAccent,
                    ColorUtil.isDark(colorBackground) ? ColorUtil.lightenColor(textColorSecondary, 1.2f) : colorBackground
            };

            switchCompat.getThumbDrawable().setTintList(new ColorStateList(switchThumbStates, switchThumbColors));
            switchCompat.getTrackDrawable().setTintList(new ColorStateList(switchTrackStates, switchTrackColors));

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
                    colorAccent,
                    textColorSecondary
            };

            ColorStateList colorStateList = new ColorStateList(bottomNavigationViewStates, bottomNavigationViewColors);
            bottomNavigationView.setItemIconTintList(colorStateList);
            bottomNavigationView.setItemTextColor(colorStateList);
            bottomNavigationView.setBackgroundColor(colorSurface);
        }
    }
}
