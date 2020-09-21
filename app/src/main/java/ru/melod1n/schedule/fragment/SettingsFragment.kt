package ru.melod1n.schedule.fragment

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import ru.melod1n.schedule.R
import ru.melod1n.schedule.activity.SettingsActivity
import ru.melod1n.schedule.base.Extensions.runOnUiThread
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.EventInfo
import ru.melod1n.schedule.common.TimeManager
import java.util.*

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    companion object {
        //Categories
        private const val CATEGORY_ACCOUNT = "account"
        private const val CATEGORY_APPEARANCE = "appearance"
        private const val CATEGORY_SCHEDULE = "schedule"
        private const val CATEGORY_DEBUG = "debug"
        const val KEY_USER_NAME = "user_name"
        const val KEY_THEME = "theme"
        private const val KEY_THEME_MANAGER = "theme_manager"
        const val KEY_AUTO_SWITCH_THEME = "auto_switch_theme"
        const val KEY_DAY_TIME_THEME = "day_time_theme"
        const val KEY_NIGHT_TIME_THEME = "night_time_theme"
        const val KEY_SELECT_CURRENT_DAY = "select_current_day"
        const val KEY_SHOW_DATE = "show_date_instead_interim"
        const val KEY_SHOW_ERROR = "show_error"
        const val KEY_SET_MORNING_TIME = "set_morning_time"
        const val KEY_SET_EVENING_TIME = "set_evening_time"
    }

    private var currentPreferenceLayout = 0
    private var dayTimeTheme: Preference? = null
    private var nightTimeTheme: Preference? = null
    private var themeManager: Preference? = null

    override fun onCreatePreferences(p1: Bundle?, p2: String?) {
        currentPreferenceLayout = if (arguments != null) {
            requireArguments().getInt("layout_id", R.xml.fragment_settings)
        } else {
            R.xml.fragment_settings
        }
        init()

        TimeManager.addOnHourChangeListener(object : TimeManager.OnHourChangeListener {
            override fun onHourChange(currentHour: Int) {
                runOnUiThread { autoSwitchVisibility(null) }
            }
        })

    }

    private fun init() {
        setTitle()
        setPreferencesFromResource(currentPreferenceLayout, null)

        val account = findPreference<PreferenceScreen>(CATEGORY_ACCOUNT)
        if (account != null) {
            account.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference -> changeRootLayout(preference) }
        }

        val userName = findPreference<PreferenceScreen>(KEY_USER_NAME)
        if (userName != null) {
            userName.onPreferenceClickListener = this
            userName.summary = AppGlobal.preferences.getString(KEY_USER_NAME, "")
        }

        val appearance = findPreference<PreferenceScreen>(CATEGORY_APPEARANCE)
        if (appearance != null) {
            appearance.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference -> changeRootLayout(preference) }
        }

//        val theme = findPreference<Preference>(KEY_THEME)
//        if (theme != null) {
//            val currentTheme = ThemeEngine.currentTheme
//            if (StringUtils.isEmpty(currentTheme!!.author)) {
//                theme.summary = currentTheme.title
//            } else {
//                theme.summary = getString(R.string.theme_summary, currentTheme.title, currentTheme.author)
//            }
//        }
//
//        themeManager = findPreference(KEY_THEME_MANAGER)
//
//        if (themeManager != null) {
//            themeManager!!.onPreferenceClickListener = this
//        }
//
//        val autoSwitchTheme = findPreference<Preference>(KEY_AUTO_SWITCH_THEME)
//        if (autoSwitchTheme != null) {
//            autoSwitchTheme.onPreferenceChangeListener = this
//        }
//
//        dayTimeTheme = findPreference(KEY_DAY_TIME_THEME)
//        if (dayTimeTheme != null) {
//            dayTimeTheme!!.onPreferenceClickListener = this
//        }
//
//        nightTimeTheme = findPreference(KEY_NIGHT_TIME_THEME)
//        if (nightTimeTheme != null) {
//            nightTimeTheme!!.onPreferenceClickListener = this
//        }
//
//        autoSwitchVisibility(null)

        val schedule = findPreference<Preference>(CATEGORY_SCHEDULE)
        if (schedule != null) {
            schedule.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference -> changeRootLayout(preference) }
        }

        val showDate = findPreference<Preference>(KEY_SHOW_DATE)
        if (showDate != null) {
            showDate.onPreferenceChangeListener = this
        }

        val debug = findPreference<Preference>(CATEGORY_DEBUG)
        if (debug != null) {
            debug.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference -> changeRootLayout(preference) }
        }

        val setMorningTime = findPreference<Preference>(KEY_SET_MORNING_TIME)
        if (setMorningTime != null) {
            setMorningTime.onPreferenceClickListener = this
        }

        val setEveningTime = findPreference<Preference>(KEY_SET_EVENING_TIME)
        if (setEveningTime != null) {
            setEveningTime.onPreferenceClickListener = this
        }

        applyTintInPreferenceScreen(preferenceScreen)
    }

    private fun setTitle() {
        var title = R.string.settings
        when (currentPreferenceLayout) {
            R.xml.fragment_settings -> title = R.string.settings
            R.xml.category_account -> title = R.string.pref_account_title
            R.xml.category_appearance -> title = R.string.pref_appearance_title
            R.xml.category_schedule -> title = R.string.pref_schedule_title
            R.xml.category_debug -> title = R.string.pref_debug_title
        }
        requireActivity().setTitle(title)
    }

    private fun applyTintInPreferenceScreen(rootScreen: PreferenceScreen) {
        if (rootScreen.preferenceCount > 0) {
            for (i in 0 until rootScreen.preferenceCount) {
                val preference = rootScreen.getPreference(i)
                tintPreference(preference)
            }
        }
    }

    private fun tintPreference(preference: Preference) {
//        if (preference.icon != null && context != null) {
//            preference.icon.setTint(ContextCompat.getColor(requireContext(), if (currentTheme!!.isDark) R.color.dark_accent else R.color.accent))
//        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            KEY_SET_MORNING_TIME -> {
                setMorningTime()
                return true
            }
            KEY_SET_EVENING_TIME -> {
                setEveningTime()
                return true
            }
            KEY_USER_NAME -> showUserNameDialog()
        }
        return false
    }

    private fun showUserNameDialog() {
//        val builder = AlertBuilder(requireContext())
//        builder.setTitle(R.string.pref_account_user_name_title)
//        val area = TextArea(requireContext())
//        area.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        area.setHint(R.string.pref_account_user_name_title)
//        area.setText(AppGlobal.preferences.getString(KEY_USER_NAME, ""))
//        area.requestFocus()
//        builder.setCustomView(area)
//        builder.setPositiveButton(R.string.change, View.OnClickListener {
//            val name: String = if (area.text == null || area.text.toString().trim().isEmpty()) {
//                getString(R.string.drawer_title_no_user)
//            } else {
//                area.text.toString().trim()
//            }
//
//            if (TextUtils.isEmpty(name)) return@OnClickListener
//
//            AppGlobal.preferences.edit().putString(KEY_USER_NAME, name).apply()
//
//            findPreference<Preference>(KEY_USER_NAME)!!.summary = name
//
//            EventBus.getDefault().postSticky(EventInfo(EventInfo.KEY_USER_NAME_UPDATE, name))
//        })
//
//        builder.setNegativeButton(android.R.string.cancel)
//        builder.show()
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        when (preference.key) {
            KEY_SHOW_DATE -> {
                sendChangeShowDateEvent(newValue as Boolean)
                return true
            }
//            KEY_AUTO_SWITCH_THEME -> {
//                val autoTheme = newValue as Boolean
//                isAutoTheme = autoTheme
//                autoSwitchVisibility(autoTheme)
//                if (autoTheme) {
//                    if (TimeManager.isMorning() || TimeManager.isAfternoon()) {
//                        if (currentTheme != dayTheme) {
//                            ThemeEngine.setCurrentTheme(dayTheme!!.id)
//                        }
//                    } else {
//                        if (currentTheme != nightTheme) {
//                            ThemeEngine.setCurrentTheme(nightTheme!!.id)
//                        }
//                    }
//                } else {
//                    if (currentTheme!!.id.toLowerCase(Locale.getDefault()) != ThemeEngine.selectedThemeKey) {
//                        ThemeEngine.setCurrentTheme(ThemeEngine.selectedThemeKey!!)
//                    }
//                }
//                return true
//            }
        }
        return false
    }

    private fun autoSwitchVisibility(b: Boolean?) {
        val visible = b ?: AppGlobal.preferences.getBoolean(KEY_AUTO_SWITCH_THEME, false)

        if (themeManager != null) {
            themeManager!!.isVisible = !visible
        }

        if (dayTimeTheme == null || nightTimeTheme == null) return

        dayTimeTheme!!.isVisible = visible
        nightTimeTheme!!.isVisible = visible

        if (TimeManager.isMorning() || TimeManager.isAfternoon()) {
            dayTimeTheme!!.setSummary(R.string.pref_appearance_time_theme_current_theme)
        } else {
            dayTimeTheme!!.summary = ""
        }

        if (TimeManager.isEvening() || TimeManager.isNight()) {
            nightTimeTheme!!.setSummary(R.string.pref_appearance_time_theme_current_theme)
        } else {
            nightTimeTheme!!.summary = ""
        }
    }

    private fun sendChangeShowDateEvent(newValue: Boolean) {

    }

    private fun changeRootLayout(preference: Preference): Boolean {
        currentPreferenceLayout = when (preference.key) {
            CATEGORY_ACCOUNT -> R.xml.category_account
            CATEGORY_APPEARANCE -> R.xml.category_appearance
            CATEGORY_DEBUG -> R.xml.category_debug
            CATEGORY_SCHEDULE -> R.xml.category_schedule
            else -> R.xml.fragment_settings
        }
        init()
        return true
    }

    private fun setMorningTime() {
        val currentCalendar = Calendar.getInstance()
        val morningCalendar = currentCalendar.clone() as Calendar
        morningCalendar[Calendar.HOUR] = 8
        setTime(morningCalendar.timeInMillis)
    }

    private fun setEveningTime() {
        val currentCalendar = Calendar.getInstance()
        val morningCalendar = currentCalendar.clone() as Calendar
        morningCalendar[Calendar.HOUR] = 19
        setTime(morningCalendar.timeInMillis)
    }

    private fun setTime(millis: Long) {
        if (context == null) return
        val manager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        manager.setTime(millis)
    }

    fun onBackPressed(): Boolean {
        return if (currentPreferenceLayout == R.xml.fragment_settings) {
            true
        } else {
            currentPreferenceLayout = R.xml.fragment_settings
            init()
            false
        }
    }

    override fun onDestroy() {
        if (activity != null) {
            (activity as SettingsActivity?)!!.setFragmentElement(currentPreferenceLayout)
        }
        super.onDestroy()
    }
}