package ru.melod1n.schedule.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.melod1n.schedule.database.dao.LessonsDao
import ru.melod1n.schedule.database.dao.NotesDao
import ru.melod1n.schedule.api.model.Lesson
import ru.melod1n.schedule.api.model.Note


@Database(
        entities = [Note::class, Lesson::class],
        exportSchema = false,
        version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract val notes: NotesDao
    abstract val lessons: LessonsDao
}