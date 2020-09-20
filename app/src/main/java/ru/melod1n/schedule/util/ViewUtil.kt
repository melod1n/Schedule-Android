package ru.melod1n.schedule.util

import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import ru.melod1n.schedule.common.ThemeEngine.currentTheme
import ru.melod1n.schedule.util.ColorUtil.darkenColor

object ViewUtil {
    fun fadeView(v: View, duration: Long) {
        v.alpha = 0f
        v.animate().alpha(1f).setDuration(duration).start()
    }

    fun fadeView(v: View) {
        fadeView(v, 200)
    }

    //TODO: переделать
    fun applyWindowStyles(window: Window, @ColorInt primaryDark: Int = currentTheme!!.colorPrimaryDark) {
        val theme = currentTheme

        window.statusBarColor = primaryDark
        window.navigationBarColor = primaryDark

        var visibility = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (theme!!.isLightStatusBar) {
                visibility += View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }

            if (theme.isLightNavigationBar) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    visibility += View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                } else {
                    window.navigationBarColor = darkenColor(primaryDark)
                }
            }
        }
        window.decorView.systemUiVisibility = visibility
    }
}