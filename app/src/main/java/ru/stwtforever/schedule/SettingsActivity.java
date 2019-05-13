package ru.stwtforever.schedule;

import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import ru.stwtforever.schedule.fragment.*;
import ru.stwtforever.schedule.util.*;

import ru.stwtforever.schedule.util.ViewUtil;
import ru.stwtforever.schedule.common.*;

public class SettingsActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ViewUtil.setStyles(this, true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		Toolbar tb = findViewById(R.id.toolbar);
		setSupportActionBar(tb);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		ViewUtil.applyToolbarStyles(tb);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) finish();
		return super.onOptionsItemSelected(item);
	}
}
