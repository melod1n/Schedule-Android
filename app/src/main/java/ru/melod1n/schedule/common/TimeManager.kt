package ru.melod1n.schedule.common

import android.annotation.SuppressLint
import android.text.format.DateUtils
import ru.melod1n.schedule.util.Util
import java.text.SimpleDateFormat
import java.util.*

object TimeManager {

    var currentHour = 0
    var currentMinute = 0
    var currentSecond = 0

    private val onHourChangeListeners: ArrayList<OnHourChangeListener>? = ArrayList()
    private val onMinuteChangeListeners: ArrayList<OnMinuteChangeListener>? = ArrayList()
    private val onSecondChangeListeners: ArrayList<OnSecondChangeListener>? = ArrayList()
    private val onTimeChangeListeners: ArrayList<OnTimeChangeListener>? = ArrayList()

    fun init() {
        val calendar = Calendar.getInstance()
        currentHour = calendar[Calendar.HOUR]
        currentMinute = calendar[Calendar.MINUTE]
        currentSecond = calendar[Calendar.SECOND]

        TaskManager.execute(process)
    }

    private val timer = Timer()


    @SuppressLint("ConstantLocale")
    private val process = {
        timer.schedule(object : TimerTask() {
            override fun run() {
                val date = Date(System.currentTimeMillis())
                val sHour = SimpleDateFormat("HH", Locale.getDefault()).format(date)
                val sMinute = SimpleDateFormat("mm", Locale.getDefault()).format(date)
                val sSecond = SimpleDateFormat("ss", Locale.getDefault()).format(date)
                val hour = Util.tryToParseInt(sHour)
                val minute = Util.tryToParseInt(sMinute)
                val second = Util.tryToParseInt(sSecond)
                if (currentHour != hour) {
                    currentHour = hour
                    if (onHourChangeListeners != null) {
                        for (onHourChangeListener in onHourChangeListeners) onHourChangeListener.onHourChange(hour)
                    }
                    if (onTimeChangeListeners != null) {
                        for (onTimeChangeListener in onTimeChangeListeners) onTimeChangeListener.onHourChange(hour)
                    }
                }
                if (currentMinute != minute) {
                    currentMinute = minute
                    if (onMinuteChangeListeners != null) {
                        for (onMinuteChangeListener in onMinuteChangeListeners) onMinuteChangeListener.onMinuteChange(minute)
                    }
                    if (onTimeChangeListeners != null) {
                        for (onTimeChangeListener in onTimeChangeListeners) onTimeChangeListener.onMinuteChange(minute)
                    }
                }
                if (currentSecond != second) {
                    currentSecond = second
                    if (onSecondChangeListeners != null) {
                        for (onSecondChangeListener in onSecondChangeListeners) onSecondChangeListener.onSecondChange(second)
                    }
                    if (onTimeChangeListeners != null) {
                        for (onTimeChangeListener in onTimeChangeListeners) onTimeChangeListener.onSecondChange(second)
                    }
                }
            }
        }, 0, DateUtils.SECOND_IN_MILLIS)
    }

    fun isMorning() = currentHour > 6 && currentHour < 12

    fun isAfternoon() = currentHour > 11 && currentHour < 17

    fun isEvening() = currentHour > 16 && currentHour < 23

    fun isNight() = currentHour == 23 || currentHour < 6 && currentHour > -1

    fun addOnHourChangeListener(onHourChangeListeners: OnHourChangeListener) {
        TimeManager.onHourChangeListeners!!.add(onHourChangeListeners)
    }

    fun removeOnHourChangeListener(onHourChangeListener: OnHourChangeListener?) {
        onHourChangeListeners!!.remove(onHourChangeListener)
    }

    fun addOnMinuteChangeListener(onMinuteChangeListener: OnMinuteChangeListener) {
        onMinuteChangeListeners!!.add(onMinuteChangeListener)
    }

    fun removeOnMinuteChangeListener(onMinuteChangeListener: OnMinuteChangeListener?) {
        onMinuteChangeListeners!!.remove(onMinuteChangeListener)
    }

    fun addOnSecondChangeListener(onSecondChangeListener: OnSecondChangeListener) {
        onSecondChangeListeners!!.add(onSecondChangeListener)
    }

    fun removeOnSecondChangeListener(onSecondChangeListener: OnSecondChangeListener?) {
        onSecondChangeListeners!!.remove(onSecondChangeListener)
    }

    fun addOnTimeChangeListener(onTimeChangeListener: OnTimeChangeListener) {
        onTimeChangeListeners!!.add(onTimeChangeListener)
    }

    fun removeOnTimeChangeListener(onTimeChangeListener: OnTimeChangeListener?) {
        onTimeChangeListeners!!.remove(onTimeChangeListener)
    }

    interface OnHourChangeListener {
        fun onHourChange(currentHour: Int)
    }

    interface OnMinuteChangeListener {
        fun onMinuteChange(currentMinute: Int)
    }

    interface OnSecondChangeListener {
        fun onSecondChange(currentSecond: Int)
    }

    interface OnTimeChangeListener {
        fun onHourChange(currentHour: Int)
        fun onMinuteChange(currentMinute: Int)
        fun onSecondChange(currentSecond: Int)
    }
}