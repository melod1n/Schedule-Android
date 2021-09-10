package ru.melod1n.schedule.common

import android.annotation.SuppressLint
import android.text.format.DateUtils
import org.greenrobot.eventbus.EventBus
import ru.melod1n.schedule.util.Util
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

object Engine {
    fun getTimeByInt(time: Int): String {
        val hours = floor(time / 60.0).toInt()
        val minutes = time - hours * 60
        return String.format("%s:%s", Util.leadingZero(hours), Util.leadingZero(minutes))
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]

        return day.toString() + " " + Util.getMonthString(month)
    }

    fun getInterim(): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val firstDayOfWeek = Calendar.MONDAY
        val lastDayOfWeek = Calendar.SATURDAY
        val currentDay = calendar[Calendar.DAY_OF_WEEK]

        val day = DateUtils.DAY_IN_MILLIS

        val date = calendar.timeInMillis

        val firstDayDate = date - day * (currentDay - firstDayOfWeek)
        val lastDayDate = date + day * (lastDayOfWeek - currentDay)
        val firstDay = getFormattedDate(firstDayDate)
        val lastDay = getFormattedDate(lastDayDate)
        return "$firstDay — $lastDay"
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(date: Long): String {
        return SimpleDateFormat("dd.MM").format(Date(date))
    }

    fun sendEvent(info: EventInfo<Any>, sticky: Boolean) {
        if (sticky) {
            EventBus.getDefault().postSticky(info)
        } else {
            EventBus.getDefault().post(info)
        }
    }

    fun sendEvent(info: EventInfo<Any>) {
        sendEvent(info, false)
    }
}