package ru.melod1n.schedule.common

class EventInfo<T>(var key: String, var data: T) {

    companion object {
        const val KEY_THEME_UPDATE = "theme_update"
        const val KEY_THEME_UPDATE_DAY = "theme_update_day"
        const val KEY_THEME_UPDATE_NIGHT = "theme_update_night"
        const val KEY_USER_NAME_UPDATE = "user_name_update"
    }
}