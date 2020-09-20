package ru.melod1n.schedule.common

import android.graphics.Color
import org.json.JSONArray
import ru.melod1n.schedule.database.CacheStorage.getThemeById
import ru.melod1n.schedule.database.CacheStorage.getThemes
import ru.melod1n.schedule.fragment.SettingsFragment
import ru.melod1n.schedule.model.ThemeItem
import ru.melod1n.schedule.util.ColorUtil
import ru.melod1n.schedule.util.Util
import java.util.*

object ThemeEngine {

    const val ENGINE_VERSION = 4

    val COLOR_PALETTE_LIGHT = intArrayOf(
        Color.WHITE,
        0xffEF9A9A.toInt(),
        0xffF48FB1.toInt(),
        0xffCE93D8.toInt(),
        0xff9FA8DA.toInt(),
        0xff90CAF9.toInt(),
        0xff81D4FA.toInt(),
        0xff80DEEA.toInt(),
        0xff80CBC4.toInt(),
        0xffA5D6A7.toInt(),
        0xffC5E1A5.toInt(),
        0xffE6EE9C.toInt(),
        0xffFFF59D.toInt(),
        0xffFFE082.toInt(),
        0xffFFCC80.toInt(),
        0xffFFAB91.toInt()
    )

    val COLOR_PALETTE_DARK = intArrayOf(
        Color.DKGRAY,
        0xffB71C1C.toInt(),
        0xff880E4F.toInt(),
        0xff4A148C.toInt(),
        0xff283593.toInt(),
        0xff1565C0.toInt(),
        0xff0277BD.toInt(),
        0xff00838F.toInt(),
        0xff00695C.toInt(),
        0xff2E7D32.toInt(),
        0xff558B2F.toInt(),
        0xff9E9D24.toInt(),
        0xffF9A825.toInt(),
        0xffFF8F00.toInt(),
        0xffEF6C00.toInt(),
        0xffD84315.toInt()
    )

    private const val DEFAULT_THEME = "teal"
    private const val DEFAULT_THEME_DARK = "teal_dark"
    private const val THEMES_FILE_NAME = "stock_themes.json"

    var themes: MutableList<ThemeItem> = ArrayList()

    private val stockThemes: MutableList<ThemeItem> = ArrayList()
    private var addedThemes: List<ThemeItem> = ArrayList()

    var colorMain = 0

    var isAutoTheme = false

    var selectedThemeKey: String? = null
    var dayThemeKey: String? = null
    var nightThemeKey: String? = null

    var currentTheme: ThemeItem? = null
    var dayTheme: ThemeItem? = null
    var nightTheme: ThemeItem? = null

    fun init() {
        initStockThemes()
        initAddedThemes()

        selectedThemeKey = AppGlobal.preferences.getString(SettingsFragment.KEY_THEME, DEFAULT_THEME)
        dayThemeKey = AppGlobal.preferences.getString(SettingsFragment.KEY_DAY_TIME_THEME, DEFAULT_THEME)
        nightThemeKey = AppGlobal.preferences.getString(SettingsFragment.KEY_NIGHT_TIME_THEME, DEFAULT_THEME_DARK)

        initThemes()

        isAutoTheme = AppGlobal.preferences.getBoolean(SettingsFragment.KEY_AUTO_SWITCH_THEME, false)

        if (currentTheme == null) {
            selectedThemeKey = DEFAULT_THEME
            AppGlobal.preferences.edit().putString(SettingsFragment.KEY_THEME, selectedThemeKey).apply()
            TaskManager.execute { currentTheme = getThemeById(DEFAULT_THEME) }
        }

        if (dayTheme == null) {
            dayThemeKey = DEFAULT_THEME
            AppGlobal.preferences.edit().putString(SettingsFragment.KEY_DAY_TIME_THEME, dayThemeKey).apply()
            TaskManager.execute { dayTheme = getThemeById(DEFAULT_THEME) }
        }

        if (nightTheme == null) {
            nightThemeKey = DEFAULT_THEME_DARK
            AppGlobal.preferences.edit().putString(SettingsFragment.KEY_NIGHT_TIME_THEME, nightThemeKey).apply()
            TaskManager.execute { nightTheme = getThemeById(DEFAULT_THEME_DARK) }
        }
        initMainColor()
    }

    private fun initThemes() {
        for (theme in themes) {
            if (selectedThemeKey!!.toLowerCase(Locale.getDefault()) == theme.id.toLowerCase(Locale.getDefault())) {
                currentTheme = theme
            }

            if (dayThemeKey!!.toLowerCase(Locale.getDefault()) == theme.id.toLowerCase(Locale.getDefault())) {
                dayTheme = theme
            }

            if (nightThemeKey!!.toLowerCase(Locale.getDefault()) == theme.id.toLowerCase(Locale.getDefault())) {
                nightTheme = theme
            }
        }
    }

    private fun initMainColor() {
        colorMain = if (ColorUtil.isLight(currentTheme!!.colorPrimary)) Color.BLACK else Color.WHITE
    }

    private fun initStockThemes() {
        try {
            val o = JSONArray(Util.readFileFromAssets(THEMES_FILE_NAME))

            for (i in 0 until o.length()) {
                val item = ThemeItem(o.optJSONObject(i))
                themes.add(item)
                stockThemes.add(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initAddedThemes() {
        TaskManager.execute {
            addedThemes = getThemes()
            themes.addAll(addedThemes)
        }
    }

    fun setCurrentTheme(id: String) {
        AppGlobal.preferences.edit().putString(SettingsFragment.KEY_THEME, id).apply()

        selectedThemeKey = id

        initThemes()
        initMainColor()

        Engine.sendEvent(EventInfo(EventInfo.KEY_THEME_UPDATE, currentTheme!!), true)
    }

    fun setDayTheme(id: String) {
        AppGlobal.preferences.edit().putString(SettingsFragment.KEY_DAY_TIME_THEME, id).apply()

        dayThemeKey = id

        if ((TimeManager.isMorning() || TimeManager.isAfternoon()) && isAutoTheme) {
            setCurrentTheme(id)
            return
        }

        initThemes()
        Engine.sendEvent(EventInfo(EventInfo.KEY_THEME_UPDATE_DAY, dayTheme!!), true)
    }

    fun setNightTheme(id: String) {
        AppGlobal.preferences.edit().putString(SettingsFragment.KEY_NIGHT_TIME_THEME, id).apply()

        nightThemeKey = id

        if ((TimeManager.isNight() || TimeManager.isEvening()) && isAutoTheme) {
            setCurrentTheme(id)
            return
        }

        initThemes()
        Engine.sendEvent(EventInfo(EventInfo.KEY_THEME_UPDATE_NIGHT, nightTheme!!))
    }

    fun isDark(): Boolean = currentTheme!!.isDark

    fun isThemeCompatible(item: ThemeItem): Boolean {
        return item.engineVersion == ENGINE_VERSION
    }
}