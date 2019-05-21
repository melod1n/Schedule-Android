package ru.stwtforever.schedule.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import ru.stwtforever.schedule.R;
import ru.stwtforever.schedule.adapter.items.NoteItem;
import ru.stwtforever.schedule.common.ThemeManager;
import ru.stwtforever.schedule.util.ColorUtil;
import ru.stwtforever.schedule.util.ViewUtil;

public class FullScreenNoteDialog extends DialogFragment {

    private static final String TAG = "fullscreen_note_dialog";

    private static final int DEFAULT_COLOR = ThemeManager.isDark() ? Color.BLACK : Color.WHITE;
    private NoteItem item;
    private boolean edit;
    private int color;
    private EditText title, text;
    private Toolbar toolbar;

    private OnDoneListener listener;

    public static FullScreenNoteDialog display(FragmentManager manager, NoteItem item) {
        FullScreenNoteDialog dialog = new FullScreenNoteDialog();
        dialog.item = item;
        dialog.edit = item != null;
        dialog.show(manager, TAG);
        return dialog;
    }

    public static FullScreenNoteDialog display(FragmentManager manager) {
        return display(manager, null);
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
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_alert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        HorizontalColorPicker picker = view.findViewById(R.id.picker);

        ArrayList<Integer> colors = new ArrayList<>();
        String[] arr = getResources().getStringArray(R.array.notes_colors);

        for (String s : arr)
            colors.add(Color.parseColor(s));

        picker.setColors(colors);

        color = !edit ? DEFAULT_COLOR : item.getColor();
        picker.setSelectedColor(color);

        picker.setOnChoosedColorListener(new HorizontalColorPicker.OnChoosedColorListener() {

            @Override
            public void onChoosedColor(int position, int color) {
                FullScreenNoteDialog.this.color = color;
                setColor(color);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                dismiss();
            }
        });

        int color = ThemeManager.isDark() ? Color.WHITE : Color.BLACK;

        toolbar.getNavigationIcon().setTint(color);

        if (edit) {
            title.setText(item.getTitle());
            text.setText(item.getText());
            title.setSelection(title.getText().length());
        }

        MenuItem delete = toolbar.getMenu().add(R.string.delete);
        delete.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.trash_can_outline));
        delete.getIcon().setTint(color);
        delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        setColor(this.color);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem i) {
                if (i.getTitle().toString().equals(getString(R.string.delete))) {
                    showConfirmDeleteDialog();
                }
                return true;
            }
        });
    }

    private void initViews(View v) {
        toolbar = v.findViewById(R.id.toolbar);

        title = v.findViewById(R.id.note_title);
        text = v.findViewById(R.id.note_text);
    }

    private void setColor(int color) {
        this.color = color;

        int textColor = ColorUtil.isDark(color) ? Color.WHITE : Color.BLACK;

        title.setTextColor(textColor);
        text.setTextColor(textColor);

        int hintColor = ColorUtil.alphaColor(textColor, 0.6f);

        title.setHintTextColor(hintColor);
        text.setHintTextColor(hintColor);

        if (toolbar.getNavigationIcon() != null)
            toolbar.getNavigationIcon().setTint(textColor);

        toolbar.getMenu().getItem(0).getIcon().setTint(ColorUtil.isDark(color) ? Color.WHITE : Color.BLACK);

        ViewUtil.applyWindowStyles(getDialog().getWindow(), color);
        toolbar.setBackgroundColor(color);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(color));
    }

    private void showConfirmDeleteDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setMessage(R.string.confirm_delete_text);
        adb.setNegativeButton(R.string.no, null);
        adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null && edit)
                    listener.onDone(null);

                dismiss();
            }
        });

        adb.create().show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        String title_ = title.getText().toString().trim();
        String text_ = text.getText().toString().trim();

        if (title_.isEmpty() && text_.isEmpty()) {
            super.onDismiss(dialog);
            return;
        }

        if (!edit)
            item = new NoteItem();

        if (color == 0)
            color = ThemeManager.getAccent();

        item.setTitle(title_);
        item.setText(text_);
        item.setColor(color);

        if (listener != null)
            listener.onDone(item);

        super.onDismiss(dialog);
    }

    public void setOnDoneListener(OnDoneListener listener) {
        this.listener = listener;
    }

    public interface OnDoneListener {
        void onDone(NoteItem item);
    }

}
