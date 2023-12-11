package es.iescarrillo.android.iacademy3.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class LessonsOrdered {

    private LocalDate lessonDate;
    private LocalTime lessonHour;
    private String classroomName;

    private String title;

    public LessonsOrdered(LocalDate lessonDate, LocalTime lessonHour, String classroomName, String title) {
        this.lessonDate = lessonDate;
        this.lessonHour = lessonHour;
        this.classroomName = classroomName;
        this.title = title;
    }

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

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
