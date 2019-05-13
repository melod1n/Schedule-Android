package ru.stwtforever.schedule;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.text.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import java.util.*;
import org.greenrobot.eventbus.*;
import ru.stwtforever.schedule.adapter.*;
import ru.stwtforever.schedule.adapter.items.*;
import ru.stwtforever.schedule.db.*;
import ru.stwtforever.schedule.util.*;
import ru.stwtforever.schedule.view.*;

import android.support.v7.widget.Toolbar;
import ru.stwtforever.schedule.util.ViewUtil;
import android.support.v4.content.*;
import ru.stwtforever.schedule.common.*;

public class NoteActivity extends AppCompatActivity {

	private NoteItem item;
	private int position;
	private boolean add, delete;
	private int color;

	private EditText title, text;
	private Toolbar tb;

	private static final int DEFAULT_COLOR = ThemeManager.isDark() ? Color.BLACK : Color.WHITE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(ThemeManager.getCurrentTheme());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_notes_dialog);
		initViews();
		
		tb.setTitle("");
		
		setSupportActionBar(tb);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent i = getIntent();
		if (i == null) return;

		item = (NoteItem) i.getSerializableExtra("item");
		position = i.getIntExtra("position", -1);
		add = i.getBooleanExtra("add", true);

		if (!add) {
			title.setText(item.getTitle());
			text.setText(item.getText());
		}

		color = add ? DEFAULT_COLOR : item.getColor();

		HorizontalColorPicker picker = findViewById(R.id.picker);
		
		ArrayList<Integer> colors = new ArrayList<>();
		String[] arr = getResources().getStringArray(R.array.notes_colors);
		
		for (String s : arr)
			colors.add(Color.parseColor(s));

		picker.setColors(colors);
		picker.setSelectedColor(color);
		setColor(color);

		picker.setOnChoosedColorListener(new HorizontalColorPicker.OnChoosedColorListener() {

				@Override
				public void onChoosedColor(int position, int color) {
					setColor(color);
				}
			});
			
		title.setOnEditorActionListener(new TextView.OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int id, KeyEvent event) {
					if (id == R.id.next || id == EditorInfo.IME_NULL || id == EditorInfo.IME_ACTION_GO) {
						text.requestFocus();
						text.setSelection(text.getText().length());
						return true;
					}
					return false;
				}

			});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem delete = menu.add(R.string.delete);
		delete.setIcon(R.drawable.trash_can_outline);
		delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem p1) {
					showConfirmDeleteDialog();
					return true;

				}});
		delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem delete = menu.getItem(0);
		
		delete.getIcon().setTint(Utils.isDark(color) ? Color.WHITE : Color.BLACK);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		postData();
		super.onDestroy();
	}

	private void showConfirmDeleteDialog() {
		new AlertDialog.Builder(this)
			.setTitle(R.string.warning)
			.setMessage(R.string.confirm_delete_text)
			.setNegativeButton(R.string.no, null)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2) {
					if (add) {
						finish();
						return;
					}
					
					delete = true;
					
					CacheStorage.delete(DatabaseHelper.TABLE_NOTES, "id=" + item.getId());
					finish();
				}
			}).create().show();
	}

	private void postData() {
		String newTitle = title.getText().toString().trim();
		String newText = text.getText().toString().trim();
		int newColor = color;

		if (!delete && TextUtils.isEmpty(newTitle) && TextUtils.isEmpty(newText)) return;
		EventBus.getDefault().postSticky(delete ? new Object[] {"delete_note", item.getId()} : new Object[] {add ? "add_note" : "edit_note", newTitle, newText, newColor, add ? -1 : position});
	}

	private void setColor(int color) {
		this.color = color;

		int textColor = Utils.isDark(color) ? Color.WHITE : Color.BLACK;
		
		title.setTextColor(textColor);
		text.setTextColor(textColor);
		
		int hintColor = ColorUtil.alphaColor(textColor, 0.6f);
		
		title.setHintTextColor(hintColor);
		text.setHintTextColor(hintColor);
		
		if (tb.getNavigationIcon() != null)
			tb.getNavigationIcon().setTint(textColor);
		
		ViewUtil.applyWindowStyles(getWindow(), color);
		tb.setBackgroundColor(color);
		getWindow().setBackgroundDrawable(new ColorDrawable(color));
		
		invalidateOptionsMenu();
	}

	private void initViews() {
		title = findViewById(R.id.title);
		text = findViewById(R.id.text);
		tb = findViewById(R.id.toolbar);
	}

}
