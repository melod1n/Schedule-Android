package ru.melod1n.schedule.api.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class Lesson(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        var order: Int = 0,
        var lessonType: Int = LESSON_TYPE_DEFAULT,
        var lessonStringType: String = "",
        var lessonTypeCustom: String = "",

        @Embedded(prefix = "subject")
        var subject: Subject? = null,

        @Embedded(prefix = "teacher")
        var teacher: Teacher? = null,

        @Embedded(prefix = "location")
        var classroom: Location? = null,

        @Embedded(prefix = "agenda")
        var agenda: Agenda? = null,

        //TODO: не игнорить
        @Ignore
        var participants: ArrayList<Participant> = ArrayList()
) {

//    enum class LessonType {
//        DEFAULT, LECTION, PRACTICE, LABWORK, COURSES, CUSTOM
//    }

    //TODO: дописать
    companion object {
        const val LESSON_TYPE_DEFAULT = 0
    }

}