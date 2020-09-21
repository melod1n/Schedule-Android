package ru.melod1n.schedule.database.dao

import androidx.room.*
import ru.melod1n.schedule.api.model.Note


@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<Note>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getById(id: String): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notes: List<Note>)

    @Update
    fun update(note: Note)

    @Update
    fun update(notes: List<Note>)

    @Delete
    fun delete(note: Note)

    @Delete
    fun delete(notes: List<Note>)

}