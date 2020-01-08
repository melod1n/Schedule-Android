package ru.melod1n.schedule.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.GravityCompat
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.app.AlertBuilder
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.ThemeEngine
import ru.melod1n.schedule.common.TimeManager
import ru.melod1n.schedule.current.BaseActivity
import ru.melod1n.schedule.fragment.*
import ru.melod1n.schedule.util.ArrayUtil
import ru.melod1n.schedule.util.Util
import ru.melod1n.schedule.widget.DrawerToggle
import ru.melod1n.schedule.widget.TextPlain
import ru.melod1n.schedule.widget.Toolbar
import java.util.*

class MainActivity : BaseActivity() {

    private val parentScheduleFragment = ParentScheduleFragment()
    private val notesFragment = NotesFragment()
    private val parentAgendaFragment = ParentAgendaFragment()
    private val updatesFragment = UpdatesFragment()

    private var selectedId = 0
    private var selectedFragment: Fragment? = null

    private var drawerEdgeSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkTheme()

        TimeManager.addOnHourChangeListener { checkTheme() }

        applyBackground()
        checkFirstLaunch(savedInstanceState)
        askPermissions()
        checkCrash()
        prepareDrawerHeader()
        prepareScreenSwipe(0)

        navigationDrawer.setNavigationItemSelectedListener { item: MenuItem -> onDrawerItemSelected(item) }
        navigationView.setOnNavigationItemSelectedListener { item: MenuItem -> onItemSelected(item) }
    }

    fun prepareScreenSwipe(page: Int) {
        val gesturesEnabled = booleanArrayOf(false)
        if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) {
            val asked = AppGlobal.preferences.getBoolean("asked_for_gestures", false)
            if (!asked) {
                val builder = AlertBuilder(this)
                builder.setTitle(R.string.using_gestures_ask_title)
                builder.setMessage(R.string.using_gestures_ask_message)
                builder.setPositiveButton(R.string.yes, View.OnClickListener { gesturesEnabled[0] = true })
                builder.setNegativeButton(R.string.no)
                builder.show()
                AppGlobal.preferences.edit().putBoolean("asked_for_gestures", true).apply()
            }
        }

        try {
            val mDragger = drawerLayout.javaClass.getDeclaredField("mLeftDragger")
            mDragger.isAccessible = true
            val draggerObj = mDragger[drawerLayout] as ViewDragHelper
            val mEdgeSize = draggerObj.javaClass.getDeclaredField("mEdgeSize")
            mEdgeSize.isAccessible = true
            if (drawerEdgeSize == 0 && page == 0) {
                drawerEdgeSize = mEdgeSize.getInt(draggerObj)
            }
            val edge = if (page > 0 || !gesturesEnabled[0]) drawerEdgeSize else (resources.displayMetrics.widthPixels * 0.25).toInt()
            mEdgeSize.setInt(draggerObj, edge)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkTheme() {
        if (ThemeEngine.isAutoTheme()) {
            val item = if (TimeManager.isMorning() || TimeManager.isAfternoon()) ThemeEngine.getDayTheme() else ThemeEngine.getNightTheme()
            if (ThemeEngine.getCurrentTheme() != item) {
                ThemeEngine.setCurrentTheme(item.id)
            }
        }
    }

    fun prepareDrawerHeader() {
        navigationDrawer.getHeaderView(0).apply {
            setOnClickListener { openLoginScreen() }

            findViewById<TextPlain>(R.id.drawerTitle).apply {
                setText(R.string.drawer_title_no_user)
            }

            findViewById<TextPlain>(R.id.drawerSubtitle).apply {
                setText(R.string.drawer_subtitle_no_user)
            }

            findViewById<AppCompatImageView>(R.id.drawerAvatar).apply {
                setImageResource(R.drawable.ic_account_circle)
                drawable.setTint(ThemeEngine.getCurrentTheme().colorTextPrimary)
            }
        }
    }

    private fun openLoginScreen() {
        startActivityForResult(Intent(this, LoginActivity::class.java), REQUEST_LOGIN)
    }

    fun getDrawerLayout(): DrawerLayout {
        return drawerLayout
    }

    fun initToggle(toolbar: Toolbar?): DrawerToggle {
        return DrawerToggle(this@MainActivity, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
    }

    private fun checkFirstLaunch(savedInstanceState: Bundle?) {
        if (!Util.isFirstLaunch()) {
            startActivity(Intent(this, SetupActivity::class.java))
            finish()
        } else {
            if (savedInstanceState == null) {
                replaceFragment(getFragmentById(selectedId))
                navigationView.selectedItemId = selectedId
            }
        }
    }

    private fun askPermissions() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSIONS)
        }
    }

    private fun checkCrash() {
        if (AppGlobal.preferences.getBoolean("isCrashed", false)) {
            val trace = AppGlobal.preferences.getString("crashLog", "")

            if (!AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SHOW_ERROR, true)) {
                AppGlobal.preferences.edit().putBoolean("isCrashed", false).putString("crashLog", "").apply()
                return
            }

            val builder = AlertBuilder(this)
            builder.setTitle(R.string.warning)
            builder.setMessage(R.string.cause_error)
            builder.setNeutralButton(R.string.show, View.OnClickListener {
                val alertBuilder = AlertBuilder(this@MainActivity)
                alertBuilder.setTitle(R.string.stack_trace)
                alertBuilder.setMessage(trace)
                alertBuilder.setPositiveButton(android.R.string.ok)
                alertBuilder.setNeutralButton(R.string.copy_trace, View.OnClickListener { Util.copyText(trace) })
                alertBuilder.show()
            })
            builder.setPositiveButton(android.R.string.ok)
            builder.setOnDismissListener {
                AppGlobal.preferences.edit().putBoolean("isCrashed", false).putString("crashLog", "").apply()
            }
            builder.show()
        }
    }

    private fun replaceFragment(fragment: Fragment?) {
        if (fragment == null || fragment === selectedFragment) return

        selectedFragment = fragment

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragments = supportFragmentManager.fragments
        val classesNames: MutableList<String> = ArrayList(fragments.size)
        val fragmentContainerId = R.id.fragmentContainer

        if (ArrayUtil.isEmpty(fragments)) {
            transaction.add(fragmentContainerId, fragment, fragment.javaClass.simpleName)
        } else {
            for (f in fragments) {
                transaction.hide(f!!)
                classesNames.add(f.javaClass.simpleName)
            }
            if (classesNames.contains(fragment.javaClass.simpleName)) {
                for (f in fragments) if (f.javaClass.simpleName == fragment.javaClass.simpleName) {
                    transaction.show(f!!)
                    break
                }
            } else {
                transaction.add(fragmentContainerId, fragment, fragment.javaClass.simpleName)
            }
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
    }

    private fun onItemSelected(item: MenuItem): Boolean {
        selectedId = item.itemId

        visibleFragment ?: return false

        if (!(visibleFragment != null && visibleFragment!!.javaClass.simpleName == ScheduleFragment::class.java.simpleName && selectedId == R.id.nav_schedule)) {
            replaceFragment(getFragmentById(selectedId))
            return true
        }
        return false
    }

    private fun onDrawerItemSelected(item: MenuItem): Boolean {
        selectedId = item.itemId
        replaceFragment(getFragmentById(selectedId))
        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    private val visibleFragment: Fragment?
        get() {
            val fragments = supportFragmentManager.fragments
            if (fragments.isEmpty()) return null
            for (fragment in fragments) {
                if (fragment.isVisible) return fragment
            }
            return null
        }

    private fun getFragmentById(navId: Int): Fragment? {
        return when (navId) {
            R.id.nav_notes -> notesFragment
            R.id.nav_agenda -> parentAgendaFragment
            R.id.nav_updates -> updatesFragment
            R.id.drawer_settings -> {
                openSettingsScreen()
                selectedFragment
            }
            R.id.drawer_about -> {
                openAboutScreen()
                selectedFragment
            }
            else -> parentScheduleFragment
        }
    }

    private fun openSettingsScreen() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openAboutScreen() {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) { //успешно авторизовались
            Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        val visibleFragment = visibleFragment
        if (visibleFragment != null && visibleFragment.javaClass.simpleName == SettingsFragment::class.java.simpleName) {
            replaceFragment(getFragmentById(navigationView.selectedItemId))
        } else if (visibleFragment != null && visibleFragment.javaClass.simpleName == ParentAgendaFragment::class.java.simpleName && !(visibleFragment as ParentAgendaFragment).isSearchViewCollapsed) {
            visibleFragment.searchViewItem.collapseActionView()
        } else if (visibleFragment != null && visibleFragment.javaClass.simpleName == NotesFragment::class.java.simpleName && !(visibleFragment as NotesFragment).isSearchViewCollapsed) {
            visibleFragment.searchViewItem.collapseActionView()
        } else if (visibleFragment != null && visibleFragment.javaClass.simpleName == ParentScheduleFragment::class.java.simpleName && !(visibleFragment as ParentScheduleFragment).isSearchViewCollapsed) {
            visibleFragment.searchViewItem.collapseActionView()
        } else {
            if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                drawerLayout!!.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }

    companion object {
        private const val REQUEST_LOGIN = 1
        private const val REQUEST_PERMISSIONS = 2
    }
}