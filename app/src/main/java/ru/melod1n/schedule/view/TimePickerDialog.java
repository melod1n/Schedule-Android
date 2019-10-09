package ru.melod1n.schedule.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.util.Util;
import ru.melod1n.schedule.util.ViewUtil;

public class TimePickerDialog extends BottomSheetDialog {

    private int hours = -1;
    private int minutes = -1;
    private int number = -1;

    private static CharSequence title = null;

    private boolean showTime;
    private boolean moveLeft;

    @BindView(R.id.abc_apply)
    FloatingActionButton buttonApply;

    @BindView(R.id.abc_title)
    TextView textViewTitle;

    @BindView(R.id.abc_hours)
    EditText inputHours;

    @BindView(R.id.abc_minutes)
    EditText inputMinutes;

    @BindView(R.id.abc_num)
    EditText inputNumber;

    @BindView(R.id.abc_num_container)
    LinearLayout numContainer;

    @BindView(R.id.abc_time_container)
    LinearLayout timeContainer;

    @BindView(R.id.abc_space)
    Space space;

    private OnChoosedNumListener numListener;
    private OnChoosedTimeListener timeListener;

    public TimePickerDialog(@NonNull Context context) {
        this(context, false);
        init();
    }

    public TimePickerDialog(@NonNull Context context, boolean showTime) {
        super(context);
        this.showTime = showTime;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        if (w != null) {
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private void init() {
        @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.abc_time_picker, null, false);
        setContentView(v);
        ButterKnife.bind(this, v);

        if (showTime) {
            inputHours.requestFocus();
        } else {
            inputNumber.requestFocus();
        }

        inputHours.setLongClickable(false);
        inputMinutes.setLongClickable(false);
        inputNumber.setLongClickable(false);

        inputHours.setFocusableInTouchMode(true);
        inputMinutes.setFocusableInTouchMode(true);
        inputNumber.setFocusableInTouchMode(true);

        inputHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && Integer.parseInt(s.toString()) > 2) {
                    String str = "0" + s;
                    inputHours.setText(str);
                    inputHours.setSelection(inputHours.getText().length());
                    inputMinutes.requestFocus();
                    inputMinutes.setText("");
                } else if (s.length() == 2) {
                    inputMinutes.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputHours.setOnEditorActionListener((p1, action, event) -> {
            if (action == EditorInfo.IME_ACTION_DONE) {
                inputMinutes.requestFocus();
                inputMinutes.setSelection(inputMinutes.getText().length());
                return true;
            }
            return false;
        });

        inputMinutes.setOnKeyListener((p1, key, event) -> {
            if (key == KeyEvent.KEYCODE_DEL) {
                if (inputMinutes.getSelectionStart() == 0 && inputMinutes.getText().length() == 0 && !moveLeft) {
                    moveLeft = true;
                    return true;
                } else if (inputMinutes.getSelectionStart() == 0 && moveLeft) {
                    inputHours.requestFocus();
                    return true;
                }
            } else if (key == KeyEvent.KEYCODE_ENTER) {
                buttonApply.performClick();
                return true;
            }
            return false;
        });

        inputNumber.setOnKeyListener((view, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_ENTER) {
                buttonApply.performClick();
                return true;
            }

            return false;
        });

        inputMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                moveLeft = s.length() == 1;
                if (s.length() == 1 && Integer.parseInt(s.toString()) > 5) {
                    String str = "0" + s;
                    inputMinutes.setText(str);
                    inputMinutes.setSelection(inputMinutes.getText().length());
                    ViewUtil.hideKeyboard(inputMinutes);
                } else if (s.length() == 2) ViewUtil.hideKeyboard(inputMinutes);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence text, int p2, int p3, int p4) {
                if (!text.toString().isEmpty()) {
                    int integer;
                    try {
                        integer = Integer.parseInt(text.toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                        integer = 0;
                    }

                    if (integer > 1440) {
                        inputNumber.setText("1440");
                        inputNumber.setSelection(inputNumber.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable p1) {
            }
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

    public void setTitle(CharSequence title) {
        TimePickerDialog.title = title;

        textViewTitle.setVisibility(title == null || title.toString().trim().isEmpty() ? View.GONE : View.VISIBLE);
        textViewTitle.setText(this.textViewTitle.getVisibility() == View.VISIBLE ? title : null);
        space.setVisibility(this.textViewTitle.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void setListener() {
        buttonApply.setOnClickListener(applyClick);
    }

    @SuppressLint("SetTextI18n")
    private View.OnClickListener applyClick = view -> {
        if ((showTime && (inputHours.getText().toString().trim().isEmpty()) || (!showTime && inputNumber.getText().toString().trim().isEmpty())))
            return;
        dismiss();
        if (showTime) {
            if (timeListener != null) {
                String minutes = inputMinutes.getText().toString().trim();
                if (minutes.isEmpty())
                    inputMinutes.setText("00");
                timeListener.onChoosedTime(Integer.parseInt(inputHours.getText().toString()), Integer.parseInt(inputMinutes.getText().toString()));
            }
        } else {
            if (numListener != null)
                numListener.onChoosedNum(Integer.parseInt(inputNumber.getText().toString()));
        }
    };

    public void setOnChoosedNumListener(OnChoosedNumListener listener) {
        this.numListener = listener;
        setListener();
    }

    public void setOnChoosedTimeListener(OnChoosedTimeListener listener) {
        this.timeListener = listener;
    }

    public void setHintTime(int hours, int minutes) {
        inputHours.setHint(Util.leadingZero(hours));
        inputMinutes.setHint(Util.leadingZero(minutes));
    }

    public void setHintNum(int num) {
        inputNumber.setHint(String.valueOf(num));
    }

    public void setTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        this.number = -1;

        inputHours.setText(Util.leadingZero(hours));
        inputNumber.setText(Util.leadingZero(minutes));
        inputHours.setSelection(inputHours.getText().length());
        inputHours.requestFocus();
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setNum(int num) {
        number = num;
        hours = -1;
        minutes = -1;

        inputNumber.setText(String.valueOf(num));
        inputNumber.setSelection(inputNumber.getText().length());
    }

    public interface OnChoosedNumListener {
        void onChoosedNum(int num);
    }

    public interface OnChoosedTimeListener {
        void onChoosedTime(int hours, int minutes);
    }
}
