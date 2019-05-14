package ru.stwtforever.schedule.view;

import android.content.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.util.*;
import android.support.v7.widget.AppCompatButton;
import ru.stwtforever.schedule.common.*;

public class TimePickerDialog extends BottomSheetDialog {

    private int HOURS = -1, MINUTES = -1, NUM = -1;
    private EditText hours, minutes, num;
    private AppCompatButton apply;
    private boolean showTime = true;

    private OnChoosedNumListener numListener;
    private OnChoosedTimeListener timeListener;

    public TimePickerDialog(@NonNull Context context) {
        this(context, false);
        init();
    }

    public TimePickerDialog(@NonNull Context context, int theme) {
        this(context, false);
        init();
    }

    public TimePickerDialog(@NonNull Context context, boolean showTime) {
        super(context, ThemeManager.getBottomSheetTheme());
        this.showTime = showTime;
        init();
    }
	
	public TimePickerDialog(@NonNull Context context, int theme, boolean showTime) {
		super(context, theme);
		this.showTime = showTime;
		init();
	}

    private void init() {
        View v = getLayoutInflater().inflate(R.layout.abc_time_picker, null, false);
        setContentView(v);
 
        hours = v.findViewById(R.id.abc_hours);
        minutes = v.findViewById(R.id.abc_minutes);
        num = v.findViewById(R.id.abc_num);
		
		hours.setLongClickable(false);
		minutes.setLongClickable(false);
		num.setLongClickable(false);

		int minutesColor = ColorUtil.alphaColor(ThemeManager.getAccent(), 0.85f);
		minutes.setTextColor(minutesColor);
		
		int textColor = ColorUtil.lightenColor(ThemeManager.getAccent());
		
		hours.setTextColor(textColor);
		num.setTextColor(textColor);
		
		int hintTextColor = ColorUtil.alphaColor(ColorUtil.darkenColor(textColor), 0.8f);
		
		minutes.setHintTextColor(hintTextColor);
		hours.setHintTextColor(hintTextColor);
		num.setHintTextColor(hintTextColor);
		
        hours.setFocusableInTouchMode(true);
        minutes.setFocusableInTouchMode(true);
        num.setFocusableInTouchMode(true);

        apply = v.findViewById(R.id.abc_apply);

        LinearLayout numContainer = v.findViewById(R.id.abc_num_container);
        LinearLayout timeContainer = v.findViewById(R.id.abc_time_container);

        hours.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s.length() == 1 && Integer.parseInt(s.toString()) > 2) {
						String str = "0" + s;
						hours.setText(str);
						hours.setSelection(hours.getText().length());
						minutes.requestFocus();
						minutes.setText("");
					} else if (s.length() == 2) minutes.requestFocus();
				}

				@Override
				public void afterTextChanged(Editable s) {}
			});

        minutes.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (s.length() == 1 && Integer.parseInt(s.toString()) > 5) {
						String str = "0" + s;
						minutes.setText(str);
						minutes.setSelection(minutes.getText().length());
						ViewUtil.hideKeyboard(minutes);
					} else if (s.length() == 2) ViewUtil.hideKeyboard(minutes);
				}

				@Override
				public void afterTextChanged(Editable s) {}
			});
			
		num.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}

				@Override
				public void onTextChanged(CharSequence text, int p2, int p3, int p4) {
					if (!text.toString().isEmpty()) {
						int integer = Integer.parseInt(text.toString());
						
						if (integer > 1440) {
							num.setText("1440");
							num.setSelection(num.getText().length());
						}
					}
				}

				@Override
				public void afterTextChanged(Editable p1) {}
			});

        if (showTime) {
            numContainer.setVisibility(View.GONE);
            timeContainer.setVisibility(View.VISIBLE);
        } else {
            numContainer.setVisibility(View.VISIBLE);
            timeContainer.setVisibility(View.GONE);
        }
        setListener();
    }

    private void setListener() {
        apply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if ((showTime && (hours.getText().toString().trim().isEmpty() || minutes.getText().toString().isEmpty())) || (!showTime && num.getText().toString().trim().isEmpty()))
						return;
					dismiss();
					if (showTime) {
						if (timeListener != null)
							timeListener.onChoosedTime(Integer.parseInt(hours.getText().toString()), Integer.parseInt(minutes.getText().toString()));
					} else {
						if (numListener != null)
							numListener.onChoosedNum(Integer.parseInt(num.getText().toString()));
					}
				}
			});
    }

    public void setOnChoosedNumListener(OnChoosedNumListener listener) {
        this.numListener = listener;
        setListener();
    }

    public void setOnChoosedTimeListener(OnChoosedTimeListener listener) {
        this.timeListener = listener;
    }

    public void setHintTime(int hours, int minutes) {
        this.hours.setHint(Util.leadingZero(hours));
        this.minutes.setHint(Util.leadingZero(minutes));
    }

    public void setHintNum(int num) {
        this.num.setHint(String.valueOf(num));
    }

    public void setTime(int hours, int minutes) {
        this.HOURS = hours;
        this.MINUTES = minutes;
        this.NUM = -1;
        this.hours.setText(Util.leadingZero(hours));
        this.minutes.setText(Util.leadingZero(minutes));
        this.hours.setSelection(this.hours.getText().length());
        this.hours.requestFocus();
    }

    public void setNum(int num) {
        this.NUM = num;
        this.HOURS = -1;
        this.MINUTES = -1;
        this.num.setText(String.valueOf(num));
        this.num.setSelection(this.num.getText().length());
    }

    public int getNum() {
        return NUM;
    }

    public String getTime() {
        return HOURS + ":" + MINUTES;
    }

    public int getHours() {
        return HOURS;
    }

    public int getMinutes() {
        return MINUTES;
    }

    public interface OnChoosedNumListener {
        void onChoosedNum(int num);
    }

    public interface OnChoosedTimeListener {
        void onChoosedTime(int hours, int minutes);
    }
}
