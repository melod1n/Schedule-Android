package ru.melod1n.schedule.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.FullScreenDialog;
import ru.melod1n.schedule.items.Lesson;
import ru.melod1n.schedule.widget.HorizontalColorPicker;

public class FullScreenLessonDialog extends FullScreenDialog<Lesson> {

    private static final String TAG = "fullscreen_lesson_dialog";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.subject_name)
    AppCompatEditText name;

    @BindView(R.id.subject_cab)
    AppCompatEditText cab;

    @BindView(R.id.subject_homework)
    AppCompatEditText homework;

    @BindView(R.id.picker)
    HorizontalColorPicker picker;

    private Lesson item;
    private boolean edit;
    private int color;

    public FullScreenLessonDialog(FragmentManager fragmentManager, Lesson item) {
        super(fragmentManager, item);
    }

    public void display(FragmentManager manager, Lesson item) {
        this.item = item;
        this.edit = item != null;
        this.show(manager, TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_alert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        picker.setColors(ThemeEngine.isDark() ? ThemeEngine.COLOR_PALETTE_DARK : ThemeEngine.COLOR_PALETTE_LIGHT);

        if (edit) {
//            picker.setSelectedPosition(item.getPosition());
            this.color = picker.getSelectedColor();
        } else {
            picker.setSelectedColor(Color.BLACK);
            this.color = Color.BLACK;
        }

        picker.setOnChoosedColorListener((position, color) -> FullScreenLessonDialog.this.color = color);

        if (edit) {
//            name.setText(item.getName());
//            cab.setText(item.getCab());
//            homework.setText(item.getHomework());
            name.setSelection(name.getText().length());
        }

        toolbar.setNavigationOnClickListener(p1 -> dismiss());

        int color = ThemeEngine.isDark() ? Color.WHITE : Color.BLACK;

        toolbar.getNavigationIcon().setTint(color);
        toolbar.inflateMenu(R.menu.fragment_schedule_alert);

        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            MenuItem item = toolbar.getMenu().getItem(i);
            if (item.getIcon() != null)
                item.getIcon().setTint(color);
        }

        toolbar.setOnMenuItemClickListener(i -> {
            switch (i.getItemId()) {
                case R.id.subject_delete:
                    showConfirmDeleteDialog();
                    break;
                case R.id.subject_save:
                    trySave();
                    break;
            }
            return true;
        });

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                toolbar.getMenu().findItem(R.id.subject_save).setVisible(!name.getText().toString().trim().isEmpty());

                if (name.getLineCount() == 3) {
                    cab.requestFocus();
                    name.setText(name.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable p1) {
            }
        });

        toolbar.getMenu().findItem(R.id.subject_save).setVisible(!name.getText().toString().trim().isEmpty());
    }

    private void trySave() {
        String name_ = name.getText().toString().trim();
        String cab_ = cab.getText().toString().trim();
        String hw_ = homework.getText().toString().trim();

        if (!edit)
            item = new Lesson();

//        item.setName(name_);
//        item.setCab(cab_);
//        item.setHomework(hw_);
//        item.setPosition(picker.getSelectedPosition());

        if (getOnActionListener() != null) {
            if (edit) {
                getOnActionListener().onItemEdit(item);
            } else {
                getOnActionListener().onItemInsert(item);
            }
        }

        if (getDialog() != null)
            getDialog().dismiss();
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setMessage(R.string.confirm_delete_text);
        adb.setNegativeButton(R.string.no, null);
        adb.setPositiveButton(R.string.yes, (dialog, which) -> {
            if (edit && getOnActionListener() != null) {
                getOnActionListener().onItemDelete(item);
            }

//            if (!item.getHomework().isEmpty())
//                EventBus.getDefault().postSticky(new Object[]{"delete_subject", item.getHomework()});

            dismiss();
        });

        adb.create().show();
    }
}
