package ru.melod1n.schedule.items

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class Lesson(
        
        @PrimaryKey(autoGenerate = true)
        private val id: Int = 0,

        val order: Int = 0,
        val lessonType: Int = LESSON_TYPE_DEFAULT,
        val lessonStringType: String = "",
        val lessonTypeCustom: String = "",

        @Embedded
        val subject: Subject? = null,

        @Embedded
        val teacher: Teacher? = null,

        @Embedded
        val classRoom: Location? = null,
        
        val participants: ArrayList<Participant> = ArrayList()
) {

//    enum class LessonType {
//        DEFAULT, LECTION, PRACTICE, LABWORK, COURSES, CUSTOM
//    }

    //TODO: дописать
    companion object {
        const val LESSON_TYPE_DEFAULT = 0
    }

}