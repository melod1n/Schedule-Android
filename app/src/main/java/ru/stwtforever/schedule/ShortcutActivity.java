package ru.stwtforever.schedule;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.view.*;
import org.greenrobot.eventbus.*;
import ru.stwtforever.schedule.fragment.*;
import ru.stwtforever.schedule.util.*;

public class ShortcutActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ViewUtil.setStyles(this, true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shortcut);
		
		Intent i = getIntent();
		int fragment = i.getIntExtra("fragment", -1);
		Fragment f = fragment == 0 ? new DayTabFragment() : fragment == 1 ? new NotesFragment() : new TimetableFragment();
		
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
		EventBus.getDefault().register(this);
	}
	
	@Subscribe (threadMode = ThreadMode.MAIN)
	public void onReceive(Object[] data) {
		String key = (String) data[0];
		if (key.equals("theme_update")) {
			recreate();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) onBackPressed();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
