package ru.stwtforever.schedule.view;

import android.content.*;
import android.support.annotation.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.util.*;

public class TimePicker extends LinearLayout {
	
	private Context context;
	
	private LinearLayout header;
	private EditText hours, minutes;
	private TextView title;
	
	private LinearLayout buttons;
	private Button positive, negative;
	
	private String text_title, text_positive, text_negative;
	
	private View.OnClickListener positive_click, negative_click;

	private @ColorInt int color_title;
	
	public TimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TimePicker(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		this.context = context;
		
		color_title = ViewUtil.AttrColor(R.attr.colorAccent);
		
		LayoutInflater.from(context).inflate(R.layout.time_picker, this);
		
		header = findViewById(R.id.title_header);
		title = findViewById(R.id.title);
		
		hours = findViewById(R.id.hours);
		minutes = findViewById(R.id.minutes);
		
		buttons = findViewById(R.id.buttons);
		
		positive = findViewById(R.id.ok);
		negative = findViewById(R.id.cancel);
		
		hours.setOnEditorActionListener(new TextView.OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int id, KeyEvent event) {
					if (id == R.id.next || id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_GO) {
						minutes.requestFocus();
						return true;
					}
					return false;
				}
			
		});
		
		hours.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					if (p1.length() == 2) {
						minutes.requestFocus();
					}
				}

				@Override
				public void afterTextChanged(Editable p1) {}
			
		});
		
		invalidateViews();
	}
	
	public void setTitleColor(@ColorInt int color) {
		this.color_title = color;
		invalidateViews();
	}
	
	public @ColorInt int getTitleColor() {
		return color_title;
	}
	
	public void setTitle(CharSequence title) {
		text_title = title.toString();
		invalidateViews();
	}
	
	public void setTitle(int res) {
		text_title = context.getString(res);
		invalidateViews();
	}
	
	public void setPositiveButton(CharSequence text, View.OnClickListener l) {
		this.text_positive = text.toString();
		this.positive_click = l;
		invalidateViews();
	}
	
	public void setPositiveButton(int text, View.OnClickListener l) {
		this.text_positive = context.getString(text);
		this.positive_click = l;
		invalidateViews();
	}
	
	public void setNegativeButton(CharSequence text, View.OnClickListener l) {
		this.text_negative = text.toString();
		this.negative_click = l;
		invalidateViews();
	}

	public void setNegativeButton(int text, View.OnClickListener l) {
		this.text_negative = context.getString(text);
		this.negative_click = l;
		invalidateViews();
	}
	
	public EditText getHoursText() {
		return findViewById(R.id.hours);
	}
	
	public EditText getMinutesText() {
		return findViewById(R.id.minutes);
	}
	
	public int getHours() {
		return Integer.parseInt(hours.getText().toString());
	}
	
	public int getMinutes() {
		return Integer.parseInt(minutes.getText().toString());
	}
	
	public void setHours(int h) {
		hours.setText(Utils.leadingZero(h));
		hours.setSelection(hours.getText().length());
	}
	
	public void setMinutes(int m) {
		minutes.setText(Utils.leadingZero(m));
		minutes.setSelection(minutes.getText().length());
	}
	
	private void invalidateViews() {
		title.setTextColor(color_title);
		
		if (!TextUtils.isEmpty(text_title)) {
			header.setVisibility(View.VISIBLE);
			title.setText(text_title);
		} else {
			header.setVisibility(View.GONE);
			title.setText("");
		}
		
		if (!TextUtils.isEmpty(text_positive)) {
			positive.setVisibility(View.VISIBLE);
			positive.setText(text_positive);
			positive.setOnClickListener(positive_click);
		} else {
			positive.setVisibility(View.GONE);
			positive.setText("");
			positive.setOnClickListener(null);
		}
		
		if (!TextUtils.isEmpty(text_negative)) {
			negative.setVisibility(View.VISIBLE);
			negative.setText(text_negative);
			negative.setOnClickListener(negative_click);
		} else {
			negative.setVisibility(View.GONE);
			negative.setText("");
			negative.setOnClickListener(null);
		}
		
		if (positive.getVisibility() == View.GONE && negative.getVisibility() == View.GONE) {
			buttons.setVisibility(View.GONE);
		} else {
			buttons.setVisibility(View.VISIBLE);
		}
	}
}
