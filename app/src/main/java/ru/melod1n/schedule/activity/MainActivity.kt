package ru.melod1n.schedule.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import ru.melod1n.schedule.R
import ru.melod1n.schedule.base.BaseActivity
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.FragmentSwitcher
import ru.melod1n.schedule.fragment.*
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var parentScheduleFragment: ParentScheduleFragment
    private lateinit var notesFragment: NotesFragment
    private lateinit var parentAgendaFragment: ParentAgendaFragment
    private lateinit var updatesFragment: UpdatesFragment

    private var selectedId = 0
    private var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareFragments()

        checkFirstLaunch(savedInstanceState)
        askPermissions()
        checkCrash()
        prepareDrawerHeader()

        navigationDrawer.setNavigationItemSelectedListener { item: MenuItem -> onDrawerItemSelected(item) }
        navigationView.setOnNavigationItemSelectedListener { item: MenuItem -> onItemSelected(item) }
    }

    private fun prepareFragments() {
        parentScheduleFragment = ParentScheduleFragment()
        notesFragment = NotesFragment()
        parentAgendaFragment = ParentAgendaFragment()
        updatesFragment = UpdatesFragment()

        FragmentSwitcher.addFragments(
                supportFragmentManager,
                R.id.fragmentContainer,
                listOf(updatesFragment, notesFragment, parentAgendaFragment, parentScheduleFragment)
        )
    }

    fun prepareDrawerHeader() {
        navigationDrawer.getHeaderView(0).apply {
            setOnClickListener { openLoginScreen() }

            findViewById<TextView>(R.id.drawerTitle).apply {
                if (true) { //проверка на офлайн
                    text = AppGlobal.preferences.getString(SettingsFragment.KEY_USER_NAME, null)
                            ?: getString(R.string.drawer_title_no_user)
                } else {
                    //
                }
            }

            findViewById<TextView>(R.id.drawerSubtitle).apply {
                if (true) { //проверка на офлайн
                    setText(R.string.drawer_subtitle_no_user)
                } else {
                    //
                }
            }
        }
    }

    private fun openLoginScreen() {
        startActivityForResult(Intent(this, LoginActivity::class.java), REQUEST_LOGIN)
    }

    fun getDrawerLayout(): DrawerLayout {
        return drawerLayout
    }

    fun initToggle(toolbar: Toolbar?): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(this@MainActivity, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
    }

    private fun checkFirstLaunch(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            FragmentSwitcher.showFragment(
                    supportFragmentManager,
                    parentScheduleFragment.javaClass.simpleName,
                    true
            )

            navigationView.selectedItemId = selectedId
        }
    }

    private fun askPermissions() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSIONS)
        }
    }

    private fun checkCrash() {
        if (AppGlobal.preferences.getBoolean("isCrashed", false)) {
            val trace = AppGlobal.preferences.getString("crashLog", "") ?: return

            if (!AppGlobal.preferences.getBoolean(SettingsFragment.KEY_SHOW_ERROR, true)) {
                AppGlobal.preferences.edit().putBoolean("isCrashed", false).putString("crashLog", "").apply()
                return
            }


            //TODO: переделать
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle(R.string.warning)
//            builder.setMessage(R.string.cause_error)
//            builder.setNeutralButton(R.string.show, View.OnClickListener {
//                val alertBuilder = AlertBuilder(this@MainActivity)
//                alertBuilder.setTitle(R.string.stack_trace)
//                alertBuilder.setMessage(trace)
//                alertBuilder.setPositiveButton(android.R.string.ok)
//                alertBuilder.setNeutralButton(R.string.copy_trace, View.OnClickListener { Util.copyText(trace) })
//                alertBuilder.show()
//            })
//            builder.setPositiveButton(android.R.string.ok)
//            builder.setOnDismissListener {
//                AppGlobal.preferences.edit().putBoolean("isCrashed", false).putString("crashLog", "").apply()
//            }
//            builder.show()
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

        if (fragments.isEmpty()) {
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

        val fragment = when (item.itemId) {
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
        } ?: return false

        FragmentSwitcher.showFragment(
                supportFragmentManager,
                fragment.javaClass.simpleName,
                true
        )

//        if (!(visibleFragment != null && visibleFragment!!.javaClass.simpleName == ScheduleFragment::class.java.simpleName && selectedId == R.id.nav_schedule)) {
//            replaceFragment(getFragmentById(selectedId))
//            return true
//        }
        return true
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
            visibleFragment.searchViewItem?.collapseActionView()
        } else if (visibleFragment != null && visibleFragment.javaClass.simpleName == NotesFragment::class.java.simpleName && !(visibleFragment as NotesFragment).isSearchViewCollapsed) {
            visibleFragment.searchViewItem?.collapseActionView()
        } else if (visibleFragment != null && visibleFragment.javaClass.simpleName == ParentScheduleFragment::class.java.simpleName && !(visibleFragment as ParentScheduleFragment).isSearchViewCollapsed) {
            visibleFragment.searchViewItem?.collapseActionView()
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