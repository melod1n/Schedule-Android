package ru.melod1n.schedule.model

data class About(
        var icon: Int = 0,
        var name: String = "",
        var job: String = "",
        var link: String = "",
        var expanded: Boolean = false
)