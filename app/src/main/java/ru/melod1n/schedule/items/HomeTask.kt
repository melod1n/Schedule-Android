package ru.melod1n.schedule.items

data class HomeTask(val id: String, val subjectId: String, val title: String, val description: String, val createdAt: Long, val updatedAt: Long, val deadlineAt: Long, val subtasks: ArrayList<SubTask>, val done: Boolean)