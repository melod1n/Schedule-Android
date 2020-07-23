package ru.melod1n.schedule.common;


import ru.melod1n.schedule.concurrent.LowThread;

public class TaskManager {

    private static final String TAG = "TaskManager";

    public static void execute(Runnable runnable) {
        new LowThread(runnable).start();
    }

}
