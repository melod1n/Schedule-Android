package ru.melod1n.schedule.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.melod1n.schedule.database.dao.LessonsDao
import ru.melod1n.schedule.database.dao.NotesDao
import ru.melod1n.schedule.database.dao.ThemesDao
import ru.melod1n.schedule.model.Lesson
import ru.melod1n.schedule.model.Note
import ru.melod1n.schedule.model.ThemeItem


@Database(
        entities = [Note::class, ThemeItem::class, Lesson::class],
        exportSchema = false,
        version = 7
)
abstract class AppDatabase : RoomDatabase() {
    abstract val notes: NotesDao
    abstract val themes: ThemesDao
    abstract val lessons: LessonsDao
}