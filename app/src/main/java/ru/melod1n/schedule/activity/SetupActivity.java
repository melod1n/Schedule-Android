package ru.melod1n.schedule.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.AppGlobal;
import ru.melod1n.schedule.olddatabase.OldCacheStorage;
import ru.melod1n.schedule.olddatabase.DatabaseHelper;
import ru.melod1n.schedule.helper.TimeHelper;
import ru.melod1n.schedule.util.Util;
import ru.melod1n.schedule.util.ViewUtil;
import ru.melod1n.schedule.widget.HorizontalColorPicker;

public class SetupActivity extends AppCompatActivity {

    private LayoutInflater inflater;

    @BindView(R.id.fragmentContainer)
    FrameLayout container;

    @BindView(R.id.screen_title)
    TextView title;

    @BindView(R.id.next)
    FloatingActionButton fab;

    private int stage = 0, res = -1;
    private String title_text;
    private View v;

    private int l = -1, b = -1, h = -1, m = -1, bells = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!Util.isFirstLaunch()) {
            startMainActivity();
        }

        inflater = LayoutInflater.from(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_screen);
        ButterKnife.bind(this);

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
        picker.setVisibility(View.GONE);
    }

    private void initSecond() {
        initView();

        fab.show();
        fab.setEnabled(true);

        Button input = v.findViewById(R.id.input);

        if (b == -1 || l == -1)
            fab.hide();

        setFabClick();

        input.setOnClickListener(p1 -> {
//            TimePickerDialog subject = new TimePickerDialog(SetupActivity.this);
//            subject.setTitle(getString(R.string.lesson_length_title));
//
//            if (l != -1) {
//                subject.setHintNum(l);
//                subject.setNum(l);
//            } else
//                subject.setHintNum(0);
//
//            subject.setOnChoosedNumListener(num -> {
//                l = num;
//
//                TimePickerDialog break_ = new TimePickerDialog(SetupActivity.this);
//                break_.setTitle(getString(R.string.break_length));
//
//                if (b != -1) {
//                    break_.setHintNum(b);
//                    break_.setNum(b);
//                } else
//                    break_.setHintNum(0);
//
//                break_.setOnChoosedNumListener(num1 -> {
//                    b = num1;
//
//                    fab.show();
//
//                    stage++;
//                    init();
//
//                    TimeHelper.lesson_length = l;
//                    TimeHelper.break_length = b;
//                    TimeHelper.save();
//                });
//
//                break_.show();
//            });
//
//            subject.show();
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

        input.setOnClickListener(p1 -> {
//            TimePickerDialog dialog = new TimePickerDialog(SetupActivity.this, true);
//            dialog.setTitle(R.string.set_lessons_time);
//
//            if (h != -1 && m != -1) {
//                dialog.setTime(h, m);
//                dialog.setHintTime(h, m);
//            } else
//                dialog.setHintTime(0, 0);
//
//            dialog.setOnChoosedTimeListener((hours, minutes) -> {
//                fab.show();
//
//                h = hours;
//                m = minutes;
//
//                stage++;
//                init();
//
//                setLessonsStart();
//            });
//
//            dialog.show();
        });
    }

    private void initFourth() {
        initView();

        fab.hide();

        setFabClick();
        findViewById(R.id.auto).setOnClickListener(p1 -> showNumBell(false));

        findViewById(R.id.manual).setOnClickListener(p1 -> {
            OldCacheStorage.delete(DatabaseHelper.TABLE_BELLS);
            showNumBell(true);
        });
    }

    private void initFiveth() {
        initView();

        fab.show();
        fab.setEnabled(true);

        fab.setOnClickListener(p1 -> checkPermissions());
    }

    private void initSixth() {
        initView();

        fab.setOnClickListener(p1 -> {
            Util.setFirstLaunch(false);
            finish();
            startActivity(new Intent(SetupActivity.this, MainActivity.class));
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
        fab.setOnClickListener(p1 -> {
            stage++;
            init();
            fab.setEnabled(false);
        });
    }

    @Override
    public void onBackPressed() {
        if (stage > 0) {
            stage--;
            init();
        } else super.onBackPressed();
    }

    private void startMainActivity() {
        Util.setFirstLaunch(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setLessonsStart() {
        TimeHelper.start_time = h + ":" + m;
        TimeHelper.save();
    }

    private void checkPermissions() {
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        //TODO: запрашивать разрешение
    }

    private void showNumBell(final boolean manual) {
        View v = getLayoutInflater().inflate(R.layout.choose_bells_count, null, false);

        NumberPicker picker = v.findViewById(R.id.picker);

        picker.setMaxValue(10);
        picker.setMinValue(1);

        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener((picker1, old_val, new_val) -> bells = new_val);

        new AlertDialog.Builder(this)
                .setTitle(R.string.bells_count)
                .setView(v).setNegativeButton(android.R.string.cancel, null).setPositiveButton(android.R.string.ok, (p1, p2) -> {
            AppGlobal.preferences.edit().putInt("bells_count", bells).apply();

            fab.show();

            if (manual)
                startActivityForResult(new Intent(SetupActivity.this, ShortcutActivity.class).putExtra("fragment", 1), 667);
            else {
                stage++;
                init();
                createTimetable();
            }
        }).setCancelable(false).create().show();
    }

    private void createTimetable() {
        OldCacheStorage.delete(DatabaseHelper.TABLE_BELLS);
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
        if (requestCode == 1) {
            stage++;
            init();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
