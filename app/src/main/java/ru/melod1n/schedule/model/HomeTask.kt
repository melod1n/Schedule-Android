package ru.melod1n.schedule.model

data class HomeTask(
        var id: String,
        var subjectId: String,
        var title: String,
        var description: String,
        var createdAt: Long,
        var updatedAt: Long,
        var deadlineAt: Long,
        var subtasks: ArrayList<SubTask>,
        var done: Boolean
)