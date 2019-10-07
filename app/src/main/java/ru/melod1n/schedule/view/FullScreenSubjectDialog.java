package ru.melod1n.schedule.view;

import android.app.Dialog;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.items.LessonItem;

public class FullScreenSubjectDialog extends DialogFragment {

    private static final String TAG = "fullscreen_subject_dialog";

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

    private boolean edit;
    private int color;

    private LessonItem item;

    private OnDoneListener listener;

    public static FullScreenSubjectDialog display(FragmentManager manager, LessonItem item) {
        FullScreenSubjectDialog dialog = new FullScreenSubjectDialog();
        dialog.item = item;
        dialog.edit = item != null;
        dialog.show(manager, TAG);
        return dialog;
    }

    public void setOnDoneListener(OnDoneListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, ThemeManager.getFullScreenDialogTheme());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_FullScreenDialog_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_alert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        picker.setColors(ThemeManager.isDark() ? ThemeManager.COLOR_PALETTE_DARK : ThemeManager.COLOR_PALETTE_LIGHT);

        if (edit) {
//            picker.setSelectedPosition(item.getPosition());
            this.color = picker.getSelectedColor();
        } else {
            picker.setSelectedColor(Color.BLACK);
            this.color = Color.BLACK;
        }

        picker.setOnChoosedColorListener((position, color) -> FullScreenSubjectDialog.this.color = color);

        if (edit) {
//            name.setText(item.getName());
//            cab.setText(item.getCab());
//            homework.setText(item.getHomework());
            name.setSelection(name.getText().length());
        }

        toolbar.setNavigationOnClickListener(p1 -> dismiss());

        int color = ThemeManager.isDark() ? Color.WHITE : Color.BLACK;

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
            item = new LessonItem();

//        item.setName(name_);
//        item.setCab(cab_);
//        item.setHomework(hw_);
//        item.setPosition(picker.getSelectedPosition());

        if (listener != null)
            listener.onDone(item);

        if (getDialog() != null)
            getDialog().dismiss();
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setMessage(R.string.confirm_delete_text);
        adb.setNegativeButton(R.string.no, null);
        adb.setPositiveButton(R.string.yes, (dialog, which) -> {
            if (listener != null && edit)
                listener.onDone(null);

//            if (!item.getHomework().isEmpty())
//                EventBus.getDefault().postSticky(new Object[]{"delete_subject", item.getHomework()});

            dismiss();
        });

        adb.create().show();
    }

    public interface OnDoneListener {
        void onDone(LessonItem item);
    }
}
