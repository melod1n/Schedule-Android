package ru.melod1n.schedule.database.dao

import androidx.room.*
import ru.melod1n.schedule.model.Lesson


@Dao
interface LessonsDao {

    @Query("SELECT * FROM lessons")
    fun getAll(): List<Lesson>

    @Query("SELECT * FROM lessons WHERE id = :id")
    fun getById(id: String): Lesson?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lesson: Lesson)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lessons: List<Lesson>)

    @Update
    fun update(lesson: Lesson)

    @Update
    fun update(lessons: List<Lesson>)

    @Delete
    fun delete(lesson: Lesson)

    @Delete
    fun delete(lessons: List<Lesson>)

}