package es.iescarrillo.android.iacademy3.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.time.LocalDateTime;

@Entity(tableName = "inscription",
        foreignKeys = {
                @ForeignKey(entity = Student.class, parentColumns = "id",
                        childColumns = "student_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Course.class, parentColumns = "id",
                        childColumns = "course_id", onDelete = ForeignKey.CASCADE)
        })
public class Inscription extends DomainEntity {
    @ColumnInfo(name = "registrationTime")
    private LocalDateTime registrationTime;
    @ColumnInfo(name = "student_id")
    private long studentId;
    @ColumnInfo(name = "course_id")
    private long courseId;

    public Inscription() {
        super();
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

}
