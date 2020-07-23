package ru.melod1n.schedule.database.dao

import androidx.room.*
import ru.melod1n.schedule.items.ThemeItem

@Dao
interface ThemesDao {

    @Query("SELECT * FROM themes")
    fun getAll(): List<ThemeItem>

    @Query("SELECT * FROM themes WHERE id = :id")
    fun getById(id: String): ThemeItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(theme: ThemeItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(themes: List<ThemeItem>)

    @Update
    fun update(theme: ThemeItem)

    @Update
    fun update(themes: List<ThemeItem>)

    @Delete
    fun delete(theme: ThemeItem)

    @Delete
    fun delete(themes: List<ThemeItem>)

}