package ru.melod1n.schedule.database

import androidx.annotation.WorkerThread
import ru.melod1n.schedule.common.AppGlobal
import ru.melod1n.schedule.common.TaskManager
import ru.melod1n.schedule.api.model.Lesson
import ru.melod1n.schedule.api.model.Note

object CacheStorage {

    private val notesDao = AppGlobal.database.notes
    private val lessonsDao = AppGlobal.database.lessons


    fun insertNote(note: Note) {
        TaskManager.execute {
            notesDao.insert(note)
        }
    }

    fun insertNotes(notes: List<Note>) {
        TaskManager.execute {
            notesDao.insert(notes)
        }
    }

    fun deleteNote(note: Note) {
        TaskManager.execute {
            notesDao.delete(note)
        }
    }

    fun deleteNotes(notes: List<Note>) {
        TaskManager.execute {
            notesDao.delete(notes)
        }
    }

    fun updateNote(note: Note) {
        TaskManager.execute {
            notesDao.update(note)
        }
    }

    fun updateNotes(notes: List<Note>) {
        TaskManager.execute {
            notesDao.update(notes)
        }
    }

    fun updateNotesWithDelete(notes: List<Note>) {
        TaskManager.execute {
            notesDao.delete(notes)

            AppGlobal.handler.post { TaskManager.execute { notesDao.insert(notes) } }
        }
    }

    @WorkerThread
    fun getNotes(): List<Note> {
        return notesDao.getAll()
    }

    @JvmStatic
    fun insertLesson(lesson: Lesson) {
        TaskManager.execute {
            lessonsDao.insert(lesson)
        }
    }

    fun updateLesson(lesson: Lesson) {
        TaskManager.execute {
            lessonsDao.update(lesson)
        }
    }

    fun deleteLesson(lesson: Lesson) {
        TaskManager.execute {
            lessonsDao.delete(lesson)
        }
    }

}