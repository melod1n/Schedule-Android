package ru.melod1n.schedule.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_themes.*
import kotlinx.android.synthetic.main.recycler_view.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.adapter.ThemeAdapter
import ru.melod1n.schedule.base.BaseActivity
import ru.melod1n.schedule.base.BaseAdapter
import ru.melod1n.schedule.common.ThemeEngine
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.common.ThemeEngine.dayTheme
import ru.melod1n.schedule.common.ThemeEngine.nightTheme
import ru.melod1n.schedule.model.ThemeItem
import java.util.*

class ThemesActivity : BaseActivity(), BaseAdapter.OnItemClickListener {

    private var adapter: ThemeAdapter? = null

    private var request = 0

    private var animating = false

    //    private Drawable navigationIcon;
    private var rootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        request = intent.getIntExtra("request", -1)

        applyBackground()
        applyTitle()

        rootView = toolbar.rootView

//        navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_backward);

        toolbar.setNavigationOnClickListener { onBackPressed() }

        refreshLayout.setOnRefreshListener { onRefresh() }

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        initThemes()

        if (savedInstanceState == null) {
            scrollToCurrentTheme()
        }
    }

    private fun scrollToCurrentTheme() {
        var index = 0

        for (i in 0 until adapter!!.itemCount) {
            val item = adapter!!.getItem(i)
            if (item == currentTheme) index = i
        }

        recyclerView.smoothScrollToPosition(index)
    }

    private fun applyTitle() {
        val theme = currentTheme
        val dayTheme = dayTheme
        val nightTheme = nightTheme
        val formatString = "%s – %s"
        val title: String
        val subtitle: Int

        when (request) {
            REQUEST_PICK_THEME -> {
                subtitle = -1
                title = String.format(formatString, theme!!.title, theme.author)
            }
            REQUEST_PICK_DAY_THEME -> {
                subtitle = R.string.pref_appearance_day_time_theme_title
                title = String.format(formatString, dayTheme!!.title, dayTheme.author)
            }
            REQUEST_PICK_NIGHT_THEME -> {
                subtitle = R.string.pref_appearance_night_time_theme_title
                title = String.format(formatString, nightTheme!!.title, nightTheme.author)
            }
            else -> {
                subtitle = -1
                title = String.format(formatString, theme!!.title, theme.author)
            }
        }

        toolbar.title = title
        if (subtitle == -1) toolbar.subtitle = "" else toolbar!!.setSubtitle(subtitle)
    }

    private fun onRefresh() {
        initThemes()
    }

    private fun initThemes() {
        val items = ArrayList(ThemeEngine.themes)

        val deleteItems = ArrayList<ThemeItem>()

        for (item in items) {
            if (request == REQUEST_PICK_DAY_THEME && item.isDark) {
                deleteItems.add(item)
            } else if (request == REQUEST_PICK_NIGHT_THEME && !item.isDark) {
                deleteItems.add(item)
            }
        }

        items.removeAll(deleteItems)

        createAdapter(items)

        refreshLayout!!.isRefreshing = false
    }

    private fun createAdapter(items: ArrayList<ThemeItem>) {
        if (adapter == null) {
            adapter = ThemeAdapter(this, items).apply {
                onItemClickListener = this@ThemesActivity
            }

            recyclerView.adapter = adapter
            return
        }

        adapter!!.setItems(items)
        adapter!!.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
        val item = adapter!!.getItem(position)

        if (ThemeEngine.isThemeCompatible(item)) {
            if (animating ||
                    request == REQUEST_PICK_THEME && item == theme ||
                    request == REQUEST_PICK_DAY_THEME && item == dayTheme ||
                    request == REQUEST_PICK_NIGHT_THEME && item == nightTheme) return

            when (request) {
                REQUEST_PICK_THEME -> {
                    ThemeEngine.setCurrentTheme(item.id)
                }
                REQUEST_PICK_DAY_THEME -> {
                    ThemeEngine. setDayTheme(item.id)
                }
                REQUEST_PICK_NIGHT_THEME -> {
                    ThemeEngine.setNightTheme(item.id)
                }
            }

            applyTitle()
            fadeLayout()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.warning)
            builder.setMessage(if (item.engineVersion > ThemeEngine.ENGINE_VERSION) R.string.warning_theme_new else R.string.warning_theme_old)
            builder.setPositiveButton(android.R.string.ok, null)
            builder.show()
        }
    }

    private fun fadeLayout() {
        animating = true
        rootView!!.alpha = 0f
        rootView!!.animate().alpha(1f).setDuration(500).withEndAction { animating = false }.start()
    }

    companion object {
        const val REQUEST_PICK_THEME = 0
        const val REQUEST_PICK_DAY_THEME = 1
        const val REQUEST_PICK_NIGHT_THEME = 2
    }
}