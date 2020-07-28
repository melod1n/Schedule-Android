package ru.melod1n.schedule.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeEngine;
import ru.melod1n.schedule.current.FullScreenDialog;
import ru.melod1n.schedule.items.Note;
import ru.melod1n.schedule.util.ColorUtil;
import ru.melod1n.schedule.util.ViewUtil;
import ru.melod1n.schedule.widget.HorizontalColorPicker;
import ru.melod1n.schedule.widget.TextArea;

public class FullScreenNoteDialog extends FullScreenDialog<Note> {

    private static final String TAG = "fullscreen_note_dialog";

    private static final int DEFAULT_COLOR = ThemeEngine.isDark() ? ThemeEngine.COLOR_PALETTE_DARK[0] : ThemeEngine.COLOR_PALETTE_LIGHT[0];

    @BindView(R.id.note_title)
    TextArea title;

    @BindView(R.id.note_text)
    TextArea text;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.note_dialog_root)
    LinearLayout root;

    @BindView(R.id.picker)
    HorizontalColorPicker picker;

    private Note item;
    private boolean edit;
    private int color;

    public FullScreenNoteDialog(FragmentManager fragmentManager, Note item) {
        super(fragmentManager, item);
    }

    public void display(FragmentManager manager, Note item) {
        this.item = item;
        this.edit = item != null;
        this.show(manager, TAG);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_alert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        //TODO: говно, надо переделать

        List<Integer> colors = new ArrayList<>();

        if (ThemeEngine.isDark()) {
            for (int color : ThemeEngine.COLOR_PALETTE_DARK) {
                colors.add(color);
            }
        } else {
            for (int color : ThemeEngine.COLOR_PALETTE_LIGHT) {
                colors.add(color);
            }
        }

        picker.setColors(colors);

//        picker.setColors(ThemeEngine.isDark() ? ThemeEngine.COLOR_PALETTE_DARK : ThemeEngine.COLOR_PALETTE_LIGHT);

        try {
            color = !edit ? DEFAULT_COLOR : item.getColor();
        } catch (Exception e) {
            color = DEFAULT_COLOR;
        }

        picker.setSelectedColor(color);
        setColor(color);

        picker.setOnChoosedColorListener((position, color) -> {
            FullScreenNoteDialog.this.color = color;
            setColor(color);
        });

        toolbar.setNavigationOnClickListener(p1 -> dismiss());

        int navigationColor = ThemeEngine.isDark() ? Color.WHITE : Color.BLACK;

        toolbar.getNavigationIcon().setTint(navigationColor);

        if (edit) {
            title.setText(item.getTitle());
            text.setText(item.getBody());
            title.setSelection(title.getText().length());
        }

        MenuItem delete = toolbar.getMenu().add(R.string.delete);
        delete.setIcon(R.drawable.ic_trash_outline);
        delete.getIcon().setTint(navigationColor);
        delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        toolbar.setOnMenuItemClickListener(i -> {
            if (i.getTitle().toString().equals(getString(R.string.delete))) {
                showConfirmDeleteDialog();
            }
            return true;
        });
    }

    private void setColor(int primary) {
        this.color = primary;

        if (getDialog() == null || getDialog().getWindow() == null) return;

        Window w = getDialog().getWindow();

        int primaryDark = ColorUtil.darkenColor(primary);

        ViewUtil.applyWindowStyles(w, primaryDark);
        w.setBackgroundDrawable(new ColorDrawable(primary));

        int visibility = 0;

        if (!ThemeEngine.getCurrentTheme().isDark()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                }
            }
        }

        w.getDecorView().setSystemUiVisibility(visibility);
    }

    private void showConfirmDeleteDialog() {
        if (getOnActionListener() != null && edit)
            getOnActionListener().onItemDelete(item);

        dismiss();
//        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
//        adb.setMessage(R.string.confirm_delete_text);
//        adb.setNegativeButton(R.string.no, null);
//        adb.setPositiveButton(R.string.yes, (dialog, which) -> {
//
//        });
//
//        adb.create().show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        String title_ = title.getText().toString().trim();
        String text_ = text.getText().toString().trim();

        if (title_.isEmpty() && text_.isEmpty()) {
            super.onDismiss(dialog);
            return;
        }

        if (color == 0)
            color = DEFAULT_COLOR;

        if (!edit) {
            item = new Note(0, "", "", "", 0, "");
        }

        item.setTitle(title_);
        item.setBody(text_);
        item.setColor(picker.getSelectedColor());

        if (getOnActionListener() != null) {
            if (edit) {
                getOnActionListener().onItemEdit(item);
            } else {
                getOnActionListener().onItemInsert(item);
            }
        }

        super.onDismiss(dialog);
    }

}
