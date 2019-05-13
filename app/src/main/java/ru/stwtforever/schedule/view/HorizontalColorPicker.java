package ru.stwtforever.schedule.view;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import de.hdodenhof.circleimageview.*;
import java.util.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.*;
import ru.stwtforever.schedule.util.*;

import ru.stwtforever.schedule.R;

public class HorizontalColorPicker extends RecyclerView {
	
	private Context context;
	
	private int selectedPosition;
	
	private OnChoosedColorListener listener;
	
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
	
	public void setColors(int... colors) {
		ArrayList<Integer> items = new ArrayList<>(colors.length);
		
		for (int color : colors) {
			items.add(color);
		}
		
		createAdapter(items);
	}
	
	public void setColors(ArrayList<Integer> items) {
		createAdapter(items);
	}
	
	private void createAdapter(ArrayList<Integer> items) {
		ArrayList<HorizontalColorPicker.Item> array = new ArrayList<>(items.size());
		
		for (int color : items) {
			array.add(new HorizontalColorPicker.Item(color, false));
		}
		
		Adapter adapter = new Adapter(context, array);
		setAdapter(adapter);
		
		getAdapter().setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {

				@Override
				public void onItemClick(View v, int position) {
					getAdapter().setSelectedPosition(position);
				
					
					if (listener != null)
						listener.onChoosedColor(position, getAdapter().getSelectedColor());
				}
		});
	}
	
	public void setOnChoosedColorListener(OnChoosedColorListener listener) {
		this.listener = listener;
	}
	
	public void setSelectedColor(int color) {
		getAdapter().setSelectedColor(color);
	}
	
	public void setSelectedPosition(int position) {
		getAdapter().setSelectedPosition(position);
	}
	
	public int getSelectedColor() {
		return getAdapter().getSelectedColor();
	}

	@Override
	public LinearLayoutManager getLayoutManager() {
		return ((LinearLayoutManager) super.getLayoutManager());
	}

	@Override
	public Adapter getAdapter() {
		return ((Adapter) super.getAdapter());
	}
	
	class Item {
		private int color;
		private boolean selected;
		
		public Item() {}
		public Item(int color, boolean selected) {
			this.color = color;
			this.selected = selected;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public int getColor() {
			return color;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public boolean isSelected() {
			return selected;
		}
	}
	
	public int getSelectedPosition() {
		return selectedPosition;
	}
	
	class Adapter extends RecyclerAdapter<HorizontalColorPicker.Item, Adapter.ViewHolder> {

		public Adapter(Context context, ArrayList<HorizontalColorPicker.Item> items) {
			super(context, items);
		}

		@Override
		public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = inflater.inflate(R.layout.abc_widget_colorpicker_horizontal_item, parent, false);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
			super.onBindViewHolder(holder, position);
			holder.bind(position);
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

		class ViewHolder extends RecyclerView.ViewHolder {

			CircleImageView circle;
			ImageView selected;

			ViewHolder(View v) {
				super(v);

				circle = v.findViewById(R.id.circle);
				selected = v.findViewById(R.id.selected);
			}

			public void bind(int position) {
				HorizontalColorPicker.Item item = getItem(position);

				circle.setImageDrawable(new ColorDrawable(item.getColor()));
				circle.setBorderWidth(2);
				
				int borderColor = item.getColor() == Color.BLACK ? item.isSelected() ? Color.WHITE : ColorUtil.lightenColor(item.getColor()) : item.getColor() == Color.WHITE ? item.isSelected() ? Color.BLACK : ColorUtil.darkenColor(item.getColor()) : Utils.isDark(item.getColor()) ? Utils.manipulateColor(item.getColor(), 2) : ColorUtil.darkenColor(item.getColor());
				
				circle.setBorderColor(borderColor);
				selected.getDrawable().setTint(borderColor);
				
				selected.setVisibility(item.isSelected() ? View.VISIBLE : View.INVISIBLE);
			}
		}
	}
	
	public interface OnChoosedColorListener {
		void onChoosedColor(int position, @ColorInt int color);
	}
}
