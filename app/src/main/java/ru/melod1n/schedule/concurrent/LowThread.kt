package ru.melod1n.schedule.concurrent

import android.os.Process

class LowThread(runnable: Runnable) : Thread(runnable) {

    override fun run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
        super.run()
    }
}