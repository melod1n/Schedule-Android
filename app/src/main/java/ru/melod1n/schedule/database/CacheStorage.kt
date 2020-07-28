package ru.melod1n.schedule.database

import androidx.annotation.WorkerThread
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.TaskManager
import ru.melod1n.schedule.items.Lesson
import ru.melod1n.schedule.items.Note
import ru.melod1n.schedule.items.ThemeItem

object CacheStorage {

    private val notesDao = AppGlobal.database.notes
    private val themesDao = AppGlobal.database.themes
    private val lessonsDao = AppGlobal.database.lessons

    @JvmStatic
    fun insertNote(note: Note) {
        TaskManager.execute {
            notesDao.insert(note)
        }
    }

    @JvmStatic
    fun insertNotes(notes: List<Note>) {
        TaskManager.execute {
            notesDao.insert(notes)
        }
    }

    @JvmStatic
    fun deleteNote(note: Note) {
        TaskManager.execute {
            notesDao.delete(note)
        }
    }

    @JvmStatic
    fun deleteNotes(notes: List<Note>) {
        TaskManager.execute {
            notesDao.delete(notes)
        }
    }

    @JvmStatic
    fun updateNote(note: Note) {
        TaskManager.execute {
            notesDao.update(note)
        }
    }

    @JvmStatic
    fun updateNotes(notes: List<Note>) {
        TaskManager.execute {
            notesDao.update(notes)
        }
    }

    @JvmStatic
    fun updateNotesWithDelete(notes: List<Note>) {
        TaskManager.execute {
            notesDao.delete(notes)

            AppGlobal.handler.post { TaskManager.execute { notesDao.insert(notes) } }
        }
    }

    @WorkerThread
    @JvmStatic
    fun getNotes(): List<Note> {
        return notesDao.getAll()
    }

    @WorkerThread
    @JvmStatic
    fun getThemes(): List<ThemeItem> {
        return themesDao.getAll()
    }

    @WorkerThread
    @JvmStatic
    fun getThemeById(id: String): ThemeItem? {
        return themesDao.getById(id)
    }

    @JvmStatic
    fun insertLesson(lesson: Lesson) {
        TaskManager.execute {
            lessonsDao.insert(lesson)
        }
    }

    @JvmStatic
    fun updateLesson(lesson: Lesson) {
        TaskManager.execute {
            lessonsDao.update(lesson)
        }
    }

    @JvmStatic
    fun deleteLesson(lesson: Lesson) {
        TaskManager.execute {
            lessonsDao.delete(lesson)
        }
    }

}