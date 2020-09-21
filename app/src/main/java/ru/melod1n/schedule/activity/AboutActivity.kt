package ru.melod1n.schedule.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.recycler_view.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.adapter.AboutAdapter
import ru.melod1n.schedule.base.BaseActivity
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.api.model.About

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        prepareToolbar()
        prepareRecyclerView()
        prepareAppVersion()

        createAdapter(createItems())
    }

    private fun createItems(): ArrayList<About> {
        return arrayListOf(
                About(R.drawable.ic_computer, getString(R.string.danil_nikolaev), getString(R.string.main_developer), "https://t.me/melod1n"),
                About(R.drawable.innomax, getString(R.string.max_hubach), getString(R.string.backend_logic_developer), "https://t.me/innomaxx")
        )
    }

    private fun createAdapter(items: ArrayList<About>) {
        val adapter = AboutAdapter(this, items)
        recyclerView.adapter = adapter
    }

    private fun prepareAppVersion() {
        val version = getString(R.string.version_about, AppGlobal.app_version_name, AppGlobal.app_version_code)
        aboutAppVersion.text = version
    }

    private fun prepareRecyclerView() {
        val manager = LinearLayoutManager(this).apply {
            orientation =
                    if (!resources.getBoolean(R.bool.is_tablet)
                            && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) RecyclerView.HORIZONTAL
                    else RecyclerView.VERTICAL
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = manager

        refreshLayout.isEnabled = false
    }

    private fun prepareToolbar() {
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}