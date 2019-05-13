package ru.stwtforever.schedule.view;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.adapter.items.*;
import ru.stwtforever.schedule.common.*;
import ru.stwtforever.schedule.util.*;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

public class FullScreenSubjectDialog extends DialogFragment {

    public static final String TAG = "fullscreen_subject_dialog";

    private Toolbar toolbar;
	private EditText name, cab, homework;

	private boolean edit;
	private int color;

	private SubjectItem item;

	private OnDoneListener listener;

    public static FullScreenSubjectDialog display(FragmentManager manager, SubjectItem item) {
        FullScreenSubjectDialog dialog = new FullScreenSubjectDialog();
		dialog.item = item;
		dialog.edit = item != null;
        dialog.show(manager, TAG);
        return dialog;
    }

	public static FullScreenSubjectDialog display(FragmentManager manager) {
		return display(manager, null);
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
			ViewUtil.applyWindowStyles(dialog.getWindow());
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_alert, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		initViews(view);

		HorizontalColorPicker picker = view.findViewById(R.id.picker);

		ArrayList<Integer> colors = new ArrayList<>();
		String[] arr = getResources().getStringArray(R.array.notes_colors);

		for (String s : arr)
			colors.add(Color.parseColor(s));

		picker.setColors(colors);
		
		if (edit) {
			picker.setSelectedColor(item.getColor());
			this.color = picker.getSelectedColor();
		}
		
		picker.setOnChoosedColorListener(new HorizontalColorPicker.OnChoosedColorListener() {

				@Override
				public void onChoosedColor(int position, int color) {
					FullScreenSubjectDialog.this.color = color;
				}
			});
		
		name.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					if (name.getLineCount() == 3) {
						cab.requestFocus();
						name.setText(name.getText().toString().trim());
					}
				}

				@Override
				public void afterTextChanged(Editable p1) {}
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
			toolbar.setTitle(item.getName());
			name.setText(item.getName());
			cab.setText(item.getCab());
			homework.setText(item.getHomework());
			name.setSelection(name.getText().length());
		}
		
		MenuItem delete = toolbar.getMenu().add(R.string.delete);
		delete.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.trash_can_outline));
		delete.getIcon().setTint(color);
		delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
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

		name = v.findViewById(R.id.subject_name);
		cab = v.findViewById(R.id.subject_cab);
		homework = v.findViewById(R.id.subject_homework);
	}
	
	private void showConfirmDeleteDialog() {
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
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
		String name_ = name.getText().toString().trim();
		String cab_ = cab.getText().toString().trim();
		String hw_ = homework.getText().toString().trim();

		if (name_.isEmpty()) {
			super.onDismiss(dialog);
			return;
		}
		
		if (!edit)
			item = new SubjectItem();
			
		if (color == 0)
			color = ThemeManager.getAccent();
		
		item.setName(name_);
		item.setCab(cab_);
		item.setHomework(hw_);
		item.setColor(color);

		if (listener != null)
			listener.onDone(item);
			
		super.onDismiss(dialog);
	}

	public interface OnDoneListener {
		void onDone(SubjectItem item);
	}
}
