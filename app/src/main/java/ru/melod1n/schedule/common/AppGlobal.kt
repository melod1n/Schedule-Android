package ru.melod1n.schedule.common

import android.app.Application
import android.content.ClipboardManager
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import androidx.core.content.pm.PackageInfoCompat
import androidx.preference.PreferenceManager
import androidx.room.Room
import ru.melod1n.schedule.database.AppDatabase
import java.io.File

class AppGlobal : Application() {

    companion object {

        lateinit var app_version_name: String
        var app_version_code = 0L

        lateinit var preferences: SharedPreferences

        lateinit var filesDir: File

        lateinit var resources: Resources

        lateinit var inputMethodManager: InputMethodManager
        lateinit var clipboardManager: ClipboardManager

        lateinit var database: AppDatabase

        lateinit var handler: Handler
    }

    override fun onCreate() {
        super.onCreate()
        Companion.resources = resources

        clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        Companion.filesDir = filesDir

        handler = Handler(mainLooper)

        try {
            val info = packageManager.getPackageInfo(packageName, 0)

            app_version_name = info.versionName
            app_version_code = PackageInfoCompat.getLongVersionCode(info)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()

        TimeManager.init()
    }


}