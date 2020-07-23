package ru.melod1n.schedule.concurrent;

import android.os.Process;

public class LowThread extends Thread {

    public LowThread(Runnable runnable) {
        super(runnable);
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        super.run();
    }
}
