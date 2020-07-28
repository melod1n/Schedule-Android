package ru.melod1n.schedule.common;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class FragmentSwitcher {

    public static void addFragments(FragmentManager fragmentManager, int containerId, Collection<Fragment> fragments) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment fragment : fragments) {
            transaction.add(containerId, fragment, fragment.getClass().getSimpleName());
        }

        transaction.commitNow();
    }

    public static void showFragment(FragmentManager fragmentManager, String tag) {
        showFragment(fragmentManager, tag, false);
    }

    public static void showFragment(@NotNull FragmentManager fragmentManager, String tag, boolean hideOthers) {
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments.isEmpty())
            throw new RuntimeException("FragmentManager's fragments is empty");

        Fragment fragmentToShow = null;

        for (Fragment fragment : fragments) {
            if (fragment.getTag() != null && fragment.getTag().equals(tag)) {
                fragmentToShow = fragment;
                break;
            }
        }

        if (fragmentToShow == null)
            throw new NullPointerException("Fragment with tag \"" + tag + "\" not found.");

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.show(fragmentToShow);

        if (hideOthers) {
            for (Fragment fragment : fragments) {
                if (fragment.getTag() != null && fragment.getTag().equals(tag)) continue;

                transaction.hide(fragment);
            }
        }

        transaction.commitNow();
    }

}
