package es.iescarrillo.android.iacademy3.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class IdCourseClassroomLessonInfo {

    private String title;
    private String name;
    private LocalDate lessonDate;
    private LocalTime lessonHour;
    private long id;

    public IdCourseClassroomLessonInfo(String title, String name, LocalDate lessonDate, LocalTime lessonHour, long id) {
        this.title = title;
        this.name = name;
        this.lessonDate = lessonDate;
        this.lessonHour = lessonHour;
        this.id = id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
