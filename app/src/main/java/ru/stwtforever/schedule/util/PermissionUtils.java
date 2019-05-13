package ru.stwtforever.schedule.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

public class PermissionUtils {

    public static final Integer CODE_CONTACTS = 0;
    public static final Integer CODE_SD_CARD = 1;

    private static Context context;
    private static Fragment fragment;

    private PermissionUtils(Context context) {
        PermissionUtils.context = context;
    }

    private PermissionUtils(Fragment fragment) {
        PermissionUtils.fragment = fragment;
    }

    public static void setContext(Context context) {
        PermissionUtils.context = context;
    }

    public static void setFragment(Fragment fragment) {
        PermissionUtils.fragment = fragment;
    }

    public static PermissionUtils init(Activity activity) {
        return new PermissionUtils(activity);
    }

    public static PermissionUtils init(Fragment fragment) {
        return new PermissionUtils(fragment);
    }

    public static void requestPermission(String perm, int request_code) {
        if (!checkPermission(perm)) {
            if (context != null)
                ActivityCompat.requestPermissions((Activity) context, new String[]{perm}, request_code);
            else
                fragment.requestPermissions(new String[]{perm}, request_code);
        }
    }

    public static void requestPermissions(String[] perms, int request_code) {
        if (context == null)
            fragment.requestPermissions(perms, request_code);
        else
            ActivityCompat.requestPermissions((Activity) context, perms, request_code);
    }

    public static boolean checkPermission(String perm) {
        int state = ActivityCompat.checkSelfPermission(context == null ? fragment.getActivity() : context, perm);
        return state == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean[] checkPermissions(String[] perms) {
        int[] states = new int[]{};
        boolean[] values = new boolean[]{};
        for (int i = 0; i < perms.length; i++) {
            states[i] = ActivityCompat.checkSelfPermission(context == null ? fragment.getActivity() : context, perms[i]);
        }
        for (int i = 0; i < states.length; i++) {
            values[i] = states[i] == PackageManager.PERMISSION_GRANTED;
        }
        return values;
    }

}
