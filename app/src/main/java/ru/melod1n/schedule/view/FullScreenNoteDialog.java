package ru.melod1n.schedule.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.schedule.R;
import ru.melod1n.schedule.common.ThemeManager;
import ru.melod1n.schedule.current.FullScreenDialog;
import ru.melod1n.schedule.items.NoteItem;

public class FullScreenNoteDialog extends FullScreenDialog<NoteItem> {

    private static final String TAG = "fullscreen_note_dialog";

    private static final int DEFAULT_COLOR = ThemeManager.isDark() ? ThemeManager.COLOR_PALETTE_DARK[0] : ThemeManager.COLOR_PALETTE_LIGHT[0];

    private NoteItem item;
    private boolean edit;
    private int color;

    @BindView(R.id.note_title)
    EditText title;

    @BindView(R.id.note_text)
    EditText text;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.note_dialog_root)
    RelativeLayout root;

    @BindView(R.id.picker)
    HorizontalColorPicker picker;

    public static FullScreenNoteDialog display(FragmentManager manager, NoteItem item) {
        FullScreenNoteDialog dialog = new FullScreenNoteDialog();
        dialog.item = item;
        dialog.edit = item != null;
        dialog.show(manager, TAG);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_alert, container, false);
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        picker.setColors(ThemeManager.isDark() ? ThemeManager.COLOR_PALETTE_DARK : ThemeManager.COLOR_PALETTE_LIGHT);

        color = !edit ? DEFAULT_COLOR : picker.getColors().get(item.getPosition());
        picker.setSelectedColor(color);

        picker.setOnChoosedColorListener((position, color) -> {
            FullScreenNoteDialog.this.color = color;
            setColor(color);
        });

        toolbar.setNavigationOnClickListener(p1 -> dismiss());

        int color = ThemeManager.isDark() ? Color.WHITE : Color.BLACK;

        toolbar.getNavigationIcon().setTint(color);

        if (edit) {
            title.setText(item.getTitle());
            text.setText(item.getText());
            title.setSelection(title.getText().length());
        }

        MenuItem delete = toolbar.getMenu().add(R.string.delete);
        delete.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_trash_outline));
        delete.getIcon().setTint(color);
        delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        setColor(this.color);

        toolbar.setOnMenuItemClickListener(i -> {
            if (i.getTitle().toString().equals(getString(R.string.delete))) {
                showConfirmDeleteDialog();
            }
            return true;
        });
    }

    private void setColor(int color) {
        this.color = color;
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        adb.setMessage(R.string.confirm_delete_text);
        adb.setNegativeButton(R.string.no, null);
        adb.setPositiveButton(R.string.yes, (dialog, which) -> {
            if (getOnActionListener() != null && edit)
                getOnActionListener().onItemDelete(item);

            dismiss();
        });

        adb.create().show();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        String title_ = title.getText().toString().trim();
        String text_ = text.getText().toString().trim();

        if (title_.isEmpty() && text_.isEmpty()) {
            super.onDismiss(dialog);
            return;
        }

        if (!edit)
            item = new NoteItem();

        if (color == 0)
            color = ThemeManager.getCurrentTheme().getColorAccent();

        item.setTitle(title_);
        item.setText(text_);
        item.setPosition(picker.getSelectedPosition());

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
