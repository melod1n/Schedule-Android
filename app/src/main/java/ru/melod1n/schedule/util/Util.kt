package ru.melod1n.schedule.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.util.DisplayMetrics
import ru.melod1n.schedule.R
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.io.BytesOutputStream
import ru.melod1n.schedule.io.FileStreams.write
import java.io.*
import java.util.*

object Util {

    var isFirstLaunch: Boolean
        get() = AppGlobal.preferences.getBoolean("first_launch", true)
        set(first) {
            AppGlobal.preferences.edit().putBoolean("first_launch", first).apply()
        }

    @SuppressLint("PrivateResource")
    fun restart(activity: Activity, extras: Intent?, anim: Boolean) {
        val intent = Intent(activity, activity.javaClass)
        if (extras != null) intent.putExtras(extras)
        activity.startActivity(intent)
        activity.finish()
        if (anim) activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    }

    fun restart(activity: Activity, anim: Boolean) {
        restart(activity, null, anim)
    }

    fun createFile(path: File, name: String, trace: String) {
        val file = File(path, name)
        try {
            write(trace, file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun copyText(text: String) {
        AppGlobal.clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
    }

    fun readFileFromAssets(fileName: String?): String {
        val returnString = StringBuilder()
        var fIn: InputStream? = null
        var isr: InputStreamReader? = null
        var input: BufferedReader? = null
        try {
            fIn = AppGlobal.resources.assets.open(fileName!!)
            isr = InputStreamReader(fIn)
            input = BufferedReader(isr)
            var line: String?
            while (input.readLine().also { line = it } != null) {
                returnString.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                isr?.close()
                fIn?.close()
                input?.close()
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
        }
        return returnString.toString()
    }

    fun leadingZero(num: Int): String {
        return if (num > 9) num.toString() else "0$num"
    }

    fun serialize(source: Any?): ByteArray? {
        try {
            val bos = BytesOutputStream()
            val out = ObjectOutputStream(bos)
            out.writeObject(source)
            out.close()
            return bos.getByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun deserialize(source: ByteArray?): Any? {
        try {
            val bis = ByteArrayInputStream(source)
            val `in` = ObjectInputStream(bis)
            val o = `in`.readObject()
            `in`.close()
            return o
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getStringDay(day: Int): String {
        val days = AppGlobal.resources.getStringArray(R.array.days)
        return days[day]
    }

    fun getMonthString(month: Int): String {
        val months = AppGlobal.resources.getStringArray(R.array.months)
        return months[month]
    }

    val numOfCurrentDay: Int
        get() {
            val calendar = Calendar.getInstance()
            var day = calendar[Calendar.DAY_OF_WEEK]
            when (day) {
                Calendar.SUNDAY, Calendar.MONDAY -> day = 0
                Calendar.TUESDAY -> day = 1
                Calendar.WEDNESDAY -> day = 2
                Calendar.THURSDAY -> day = 3
                Calendar.FRIDAY -> day = 4
                Calendar.SATURDAY -> day = 5
            }
            return day
        }

    fun px(dp: Float): Int {
        return (dp * (AppGlobal.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    fun dp(px: Float): Int {
        return (px / (AppGlobal.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    fun tryToParseInt(string: String): Int {
        return try {
            string.toInt()
        } catch (e: Exception) {
            0
        }
    }
}