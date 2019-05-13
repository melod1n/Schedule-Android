package ru.stwtforever.schedule;

import android.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.text.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import net.yslibrary.android.keyboardvisibilityevent.*;
import ru.stwtforever.schedule.adapter.*;
import ru.stwtforever.schedule.common.*;
import ru.stwtforever.schedule.db.*;
import ru.stwtforever.schedule.helper.*;
import ru.stwtforever.schedule.util.*;
import ru.stwtforever.schedule.view.*;

import ru.stwtforever.schedule.util.ViewUtil;
import ru.stwtforever.schedule.view.TimePicker;
import org.greenrobot.eventbus.*;

public class SetupActivity extends AppCompatActivity {

	private LayoutInflater inflater;

	private FrameLayout container;
	private TextView title;
	private FloatingActionButton fab;

	private int stage = 0, res = -1;
	private String title_text;
	private View v;

	private int l = -1, b = -1, h = -1, m = -1, bells = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (!Utils.isFirstLaunch()) {
			startMainActivity();
		}

		inflater = LayoutInflater.from(this);

		ViewUtil.setStyles(this, true);

		PermissionHelper.init(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_screen);

		fab = findViewById(R.id.next);
		container = findViewById(R.id.container);
		title = findViewById(R.id.title);

		init();
	}
	
	private void init() {
		switch (stage) {
			case 0:
				title_text = getString(R.string.hello);
				res = R.layout.activity_setup_1;
				initFirst();
				break;
			case 1:
				title_text = getString(R.string.necessary_data);
				res = R.layout.activity_setup_2;
				initSecond();
				break;
			case 2:
				title_text = getString(R.string.classes_start_time);
				res = R.layout.activity_setup_3;
				initThird();
				break;
			case 3:
				title_text = getString(R.string.timetable_list);
				res = R.layout.activity_setup_4;
				initFourth();
				break;
			case 4:
				title_text = getString(R.string.permissions);
				res = R.layout.activity_setup_5;
				initFiveth();
				break;
			case 5:
				title_text = getString(R.string.done_msg);
				res = R.layout.activity_setup_6;
				initSixth();
				break;
			default:
				title_text = "";
				res = R.layout.activity_setup_1;
				initFirst();
				break;
		}
	}

	private void initFirst() {
		initView();
		
		fab.show();
		fab.setEnabled(true);
		
		setFabClick();

		final HorizontalColorPicker picker = v.findViewById(R.id.picker);

		picker.setColors(ThemeManager.COLORS);
		picker.setSelectedColor(ThemeManager.getPrimary());
		picker.setOnChoosedColorListener(new HorizontalColorPicker.OnChoosedColorListener() {

				@Override
				public void onChoosedColor(int position, int color) {
					if (ThemeManager.getPrimary() == color) return;
					switchTheme(color != Color.WHITE);
				}
			});
	}

	private void initSecond() {
		initView();
		
		fab.show();
		fab.setEnabled(true);

		Button input = v.findViewById(R.id.input);

		if (b == -1 || l == -1)
			fab.hide();

		setFabClick();

		input.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					TimePickerDialog subject = new TimePickerDialog(SetupActivity.this);
					
					if (l != -1) {
						subject.setHintNum(l);
						subject.setNum(l);
					} else
						subject.setHintNum(0);

					subject.setOnChoosedNumListener(new TimePickerDialog.OnChoosedNumListener() {

							@Override
							public void onChoosedNum(int num) {
								l = num;

								TimePickerDialog break_ = new TimePickerDialog(SetupActivity.this);

								if (b != -1) {
									break_.setHintNum(b);
									break_.setNum(b);
								} else
									break_.setHintNum(0);

								break_.setOnChoosedNumListener(new TimePickerDialog.OnChoosedNumListener() {

										@Override
										public void onChoosedNum(int num) {
											b = num;

											fab.show();

											stage++;
											init();

											TimeHelper.lesson_length = l;
											TimeHelper.break_length = b;
											TimeHelper.save();
										}
									});

								break_.show();
							}
						});

					subject.show();
				}
			});
	}

	private void initThird() {
		initView();

		fab.show();
		fab.setEnabled(true);
		
		Button input = v.findViewById(R.id.input);

		if (h == -1 || m == -1)
			fab.hide();

		setFabClick();

		input.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					TimePickerDialog dialog = new TimePickerDialog(SetupActivity.this, true);

					if (h != -1 && m != -1) {
						dialog.setTime(h, m);
						dialog.setHintTime(h, m);
					} else 
						dialog.setHintTime(0, 0);
					
					dialog.setOnChoosedTimeListener(new TimePickerDialog.OnChoosedTimeListener() {

							@Override
							public void onChoosedTime(int hours, int minutes) {
								fab.show();

								h = hours;
								m = minutes;

								stage++;
								init();

								setLessonsStart();
							}
						});

					dialog.show();
				}
			});
	}

	private void initFourth() {
		initView();
		
		fab.show();
		fab.setEnabled(true);

		setFabClick();
		findViewById(R.id.auto).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					showNumBell(false);
				}
			});

		findViewById(R.id.manual).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					CacheStorage.delete(DatabaseHelper.TABLE_BELLS);
					showNumBell(true);
				}
			});
	}

	private void initFiveth() {
		initView();
		
		fab.show();
		fab.setEnabled(true);

		fab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					checkPermissions();
				}
			});
	}

	private void initSixth() {
		initView();

		fab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					Utils.setFirstLaunch(false);
					finish();
					startActivity(new Intent(SetupActivity.this, MainActivity.class));
				}
			});
	}

	private void clearChilds() {
		if (container.getChildCount() > 0) {
			container.removeAllViews();
		}
	}

	private void initView() {
		v = inflater.inflate(res, null, false);
		clearChilds();

		container.addView(v);
		ViewUtil.fadeView(v);
		ViewUtil.fadeView(title);
		title.setText(title_text);
	}

	private void setFabClick() {
		fab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View p1) {
					stage++;
					init();
					fab.setEnabled(false);
				}
			});
	}

	private void switchTheme(boolean dark) {
		ThemeManager.switchTheme(dark);
		Utils.restart(this, true);
	}

	@Override
	public void onBackPressed() {
		if (stage > 0) {
			stage--;
			init();
		} else super.onBackPressed();
	}

	private void startMainActivity() {
		Utils.setFirstLaunch(false);
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	private void setLessonsStart() {
		TimeHelper.start_time = h + ":" + m;
		TimeHelper.save();
	}

	private void checkPermissions() {
		String[] perms = new String[] {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			//Manifest.permission.READ_CONTACTS
		};
		PermissionHelper.requestPermissions(perms, Requests.REQUEST_GET_SETUP_PERMISSIONS);
	}

	private void showNumBell(final boolean manual) {
		View v = getLayoutInflater().inflate(R.layout.choose_bells_count, null, false);

		NumberPicker picker = v.findViewById(R.id.picker);
		picker.setMaxValue(10);
		picker.setMinValue(1);

		picker.setWrapSelectorWheel(false);
		picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

				@Override
				public void onValueChange(NumberPicker picker, int old_val, int new_val) {
					bells = new_val;
				}
			});

		new AlertDialog.Builder(this)
			.setTitle(R.string.bells_count)
			.setView(v).setNegativeButton(android.R.string.cancel, null).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2) {
					AppGlobal.preferences.edit().putInt("bells_count", bells).apply();

					if (manual)
						startActivityForResult(new Intent(SetupActivity.this, ShortcutActivity.class).putExtra("fragment", 1), 667);
					else {
						stage++;
						init();
						createTimetable();
					}
				}
			}).setCancelable(false).create().show();
	}

	private void createTimetable() {
		CacheStorage.delete(DatabaseHelper.TABLE_BELLS);
		TimeHelper.load();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == 55 || requestCode == 667) && resultCode == RESULT_OK) {
			stage++;
			init();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == Requests.REQUEST_GET_SETUP_PERMISSIONS) {
			stage++;
			init();
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}
