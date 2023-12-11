package es.iescarrillo.android.iacademy3.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(tableName = "lesson",
        foreignKeys = {
                @ForeignKey(entity = Classroom.class, parentColumns = "id",
                        childColumns = "classroom_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Course.class, parentColumns = "id",
                        childColumns = "course_id", onDelete = ForeignKey.CASCADE)
        })
public class Lesson extends DomainEntity{
    @ColumnInfo(name = "lessonDate")
    private LocalDate lessonDate;
    @ColumnInfo(name = "lessonHour")
    private LocalTime lessonHour;
    @ColumnInfo(name = "classroom_id")
    private long classroomId;
    @ColumnInfo(name = "course_id")
    private long courseId;

    // Constructor
    public Lesson() {
        super();
    }

    // Getters and setters
    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDate lessonDate) {
        this.lessonDate = lessonDate;
    }

    public LocalTime getLessonHour() {
        return lessonHour;
    }

    public void setLessonHour(LocalTime lessonHour) {
        this.lessonHour = lessonHour;
    }

    public long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(long classroomId) {
        this.classroomId = classroomId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonDate=" + lessonDate +
                ", lessonHour=" + lessonHour +
                ", classroomId=" + classroomId +
                ", courseId=" + courseId +
                '}';
    }
}
