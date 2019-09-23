package ru.melod1n.schedule.current;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public abstract class BaseActivity extends AppCompatActivity {

    public int color(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }

}
