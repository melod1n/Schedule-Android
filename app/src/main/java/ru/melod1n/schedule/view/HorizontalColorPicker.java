package ru.melod1n.schedule.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.current.BaseAdapter;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.BaseHolder;

public class HorizontalColorPicker extends RecyclerView {

    private Context context;

    private int selectedPosition;

    private OnChoosedColorListener listener;

    private ArrayList<Integer> colors;

    public HorizontalColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public HorizontalColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalColorPicker(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(HORIZONTAL);

        setHasFixedSize(true);
        setLayoutManager(manager);
    }

    public void setColors(@NonNull int... colors) {
        ArrayList<Integer> items = new ArrayList<>(colors.length);

        this.colors = items;

        for (int color : colors) {
            items.add(color);
        }

        createAdapter(items);
    }

    public void setColors(ArrayList<Integer> items) {
        this.colors = items;
        createAdapter(items);
    }

    public ArrayList<Integer> getColors() {
        return colors;
    }

    private void createAdapter(@NonNull ArrayList<Integer> items) {
        ArrayList<HorizontalColorPicker.Item> array = new ArrayList<>(items.size());

        for (int color : items) {
            array.add(new HorizontalColorPicker.Item(color, false));
        }

        Adapter adapter = new Adapter(context, array);
        setAdapter(adapter);

        getAdapter().setOnItemClickListener((v, position) -> {
            getAdapter().setSelectedPosition(position);


            if (listener != null)
                listener.onChoosedColor(position, getAdapter().getSelectedColor());
        });
    }

    public void setOnChoosedColorListener(OnChoosedColorListener listener) {
        this.listener = listener;
    }

    public int getSelectedColor() {
        return getAdapter().getSelectedColor();
    }

    public void setSelectedColor(int color) {
        getAdapter().setSelectedColor(color);
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return ((LinearLayoutManager) super.getLayoutManager());
    }

    @Override
    public Adapter getAdapter() {
        return ((Adapter) super.getAdapter());
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        getAdapter().setSelectedPosition(position);
    }

    public interface OnChoosedColorListener {
        void onChoosedColor(int position, @ColorInt int color);
    }

    class Item {
        private int color;
        private boolean selected;

        public Item() {
        }

        public Item(int color, boolean selected) {
            this.color = color;
            this.selected = selected;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    class Adapter extends BaseAdapter<Item, Adapter.ViewHolder> {

        public Adapter(Context context, ArrayList<HorizontalColorPicker.Item> items) {
            super(context, items);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getInflater().inflate(R.layout.abc_widget_colorpicker_horizontal_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.bind(position);
        }

        @Override
        public void destroy() {

        }

        public int getSelectedColor() {
            return getItem(selectedPosition).getColor();
        }

        public void setSelectedColor(int color) {
            for (int i = 0; i < getValues().size(); i++) {
                HorizontalColorPicker.Item item = getValues().get(i);
                item.setSelected(item.getColor() == color);
                notifyItemChanged(i, item);
                if (item.isSelected())
                    selectedPosition = i;
            }

            smoothScrollToPosition(selectedPosition);
        }

        public void setSelectedPosition(int position) {
            for (int i = 0; i < getValues().size(); i++) {
                HorizontalColorPicker.Item item = getValues().get(i);
                item.setSelected(i == position);
                notifyItemChanged(i, item);

                if (item.isSelected())
                    selectedPosition = position;
            }

            smoothScrollToPosition(selectedPosition);
        }

        class ViewHolder extends BaseHolder {

            CircleImageView circle;
            ImageView selected;

            ViewHolder(View v) {
                super(v);

                circle = v.findViewById(R.id.circle);
                selected = v.findViewById(R.id.selected);
            }

            @Override
            public void bind(int position) {
                HorizontalColorPicker.Item item = getItem(position);

                circle.setImageDrawable(new ColorDrawable(item.getColor()));
                circle.setBorderWidth(2);

                int borderColor = ThemeEngine.isDark() ? Color.WHITE : 0xff121212;

                circle.setBorderColor(borderColor);
                selected.getDrawable().setTint(borderColor);

                selected.setVisibility(item.isSelected() ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }
}
