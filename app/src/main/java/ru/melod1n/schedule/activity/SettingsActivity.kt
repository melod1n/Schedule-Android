package ru.melod1n.schedule.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.toolbar.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.base.BaseActivity
import ru.melod1n.schedule.fragment.SettingsFragment

class SettingsActivity : BaseActivity() {

    private lateinit var fragment: SettingsFragment
    private var layoutId = R.xml.fragment_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_backward)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        layoutId = AppGlobal.preferences.getInt("settings_layout", R.xml.fragment_settings)

        val arguments = Bundle()
        arguments.putInt("layout_id", layoutId)

        fragment = SettingsFragment()
        fragment.arguments = arguments

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.javaClass.simpleName).commit()
    }

    override fun init() {
        super.init()
        if (!created) return
        recreate()
    }

    override fun onBackPressed() {
        if (fragment.onBackPressed()) super.onBackPressed()
    }

    override fun setTitle(title: CharSequence) {
        if (toolbar != null) toolbar!!.title = title
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    fun setFragmentElement(@LayoutRes layoutId: Int) {
        this.layoutId = layoutId
        AppGlobal.preferences.edit().putInt("settings_layout", layoutId).apply()
    }

    override fun onDestroy() {
        AppGlobal.preferences.edit().remove("settings_layout").apply()
        super.onDestroy()
    }
}