package es.iescarrillo.android.iacademy3.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class CourseClassroomLessonInfo {

    private String title;
    private String name;
    private LocalDate lessonDate;
    private LocalTime lessonHour;

    public CourseClassroomLessonInfo(String title, String name, LocalDate lessonDate, LocalTime lessonHour) {
        this.title = title;
        this.name = name;
        this.lessonDate = lessonDate;
        this.lessonHour = lessonHour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
