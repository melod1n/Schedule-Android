package ru.melod1n.schedule.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.melod1n.schedule.database.dao.NotesDao
import ru.melod1n.schedule.database.dao.ThemesDao
import ru.melod1n.schedule.items.Note
import ru.melod1n.schedule.items.ThemeItem


@Database(entities = [Note::class, ThemeItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract val notes: NotesDao
    abstract val themes: ThemesDao
}