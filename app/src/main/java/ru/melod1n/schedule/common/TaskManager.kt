package ru.melod1n.schedule.common

import ru.melod1n.schedule.concurrent.LowThread

object TaskManager {

    fun execute(runnable: Runnable) {
        LowThread(runnable).start()
    }

    fun execute(unit: () -> Unit) {
        execute(Runnable(unit))
    }

    fun executeNow(runnable: Runnable) {
        Thread(runnable).start()
    }

    fun executeNow(unit: () -> Unit) {
        executeNow(Runnable(unit))
    }

}