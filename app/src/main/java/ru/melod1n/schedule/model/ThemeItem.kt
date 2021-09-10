package ru.melod1n.schedule.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "themes")
class ThemeItem() {
    @PrimaryKey
    var id = ""
    var title: String? = null
    var author: String? = null
    var engineVersion = 0
    var isDark = false
    var isLightStatusBar = false
    var isLightNavigationBar = false
    var isSelected = false
    var colorSurface = 0
    var colorPrimary = 0
    var colorPrimaryDark = 0
    var colorAccent = 0
    var colorBackground = 0
    var colorTabsTextNormal = 0
    var colorTabsTextActive = 0
    var colorTabsIndicator = 0
    var colorControlNormal = 0
    var colorTextPrimary = 0
    var colorTextSecondary = 0
    var colorTextPrimaryInverse = 0
    var colorTextSecondaryInverse = 0
    var colorDrawer = 0
    var colorDrawerTextNormal = 0
    var colorDrawerTextActive = 0
    var colorDrawerIconNormal = 0
    var colorDrawerIconActive = 0
    var colorDrawerHeaderBackground = 0
    var colorDrawerHeaderTitle = 0
    var colorDrawerHeaderSubtitle = 0
    var colorBottomBar = 0
    var colorBottomBarIconsNormal = 0
    var colorBottomBarIconsActive = 0
    var colorHighlight = 0
    var fabColor = 0
    var fabIconColor = 0

    constructor(o: JSONObject) : this() {
        id = o.optString("id")
        title = o.optString("title")
        author = o.optString("author")
        engineVersion = o.optInt("version")

        o.optJSONObject("ui")?.let {
            isDark = it.optBoolean("dark")
            isLightStatusBar = it.optBoolean("light_status_bar")
            isLightNavigationBar = it.optBoolean("light_navigation_bar")
        }

        o.optJSONObject("colors")?.let {
            colorSurface = parseColor(it.optString("surface"))
            colorPrimary = parseColor(it.optString("primary"))
            colorPrimaryDark = parseColor(it.optString("primary_dark"))
            colorAccent = parseColor(it.optString("accent"))
            colorBackground = parseColor(it.optString("background"))
            colorTabsTextNormal = parseColor(it.optString("tabs_text_normal"))
            colorTabsTextActive = parseColor(it.optString("tabs_text_active"))
            colorTabsIndicator = parseColor(it.optString("tabs_indicator"))
            colorControlNormal = parseColor(it.optString("control_normal"))
            colorTextPrimary = parseColor(it.optString("text_primary"))
            colorTextSecondary = parseColor(it.optString("text_secondary"))
            colorTextPrimaryInverse = parseColor(it.optString("text_primary_inverse"))
            colorTextSecondaryInverse = parseColor(it.optString("text_secondary_inverse"))
            colorDrawer = parseColor(it.optString("drawer"))
            colorDrawerTextNormal = parseColor(it.optString("drawer_text_normal"))
            colorDrawerTextActive = parseColor(it.optString("drawer_text_active"))
            colorDrawerIconNormal = parseColor(it.optString("drawer_icon_normal"))
            colorDrawerIconActive = parseColor(it.optString("drawer_icon_active"))
            colorDrawerHeaderBackground = parseColor(it.optString("drawer_header_background"))
            colorDrawerHeaderTitle = parseColor(it.optString("drawer_header_title"))
            colorDrawerHeaderSubtitle = parseColor(it.optString("drawer_header_subtitle"))
            colorBottomBar = parseColor(it.optString("bottom_bar"))
            colorBottomBarIconsNormal = parseColor(it.optString("bottom_bar_icons_normal"))
            colorBottomBarIconsActive = parseColor(it.optString("bottom_bar_icons_active"))
            colorHighlight = parseColor(it.optString("highlight"))
            fabColor = parseColor(it.optString("fab_color"))
            fabIconColor = parseColor(it.optString("fab_icon_color"))
        }
    }

    private fun parseColor(color: String): Int {
        return try {
            Color.parseColor(color)
        } catch (e: Exception) {
            when (color) {
                "primary" -> colorPrimary
                "primary_dark" -> colorPrimaryDark
                "accent" -> colorAccent
                "surface" -> colorSurface
                "background" -> colorBackground
                else -> -1
            }
        }
    }

    fun alphaColor(color: Int, alphaFactor: Float): Int {
        val alpha = Color.alpha(color)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb((alpha * alphaFactor).toInt(), red, green, blue)
    }

    fun alphaColor(color: String?, alphaFactor: Float): Int {
        return alphaColor(Color.parseColor(color), alphaFactor)
    }
}