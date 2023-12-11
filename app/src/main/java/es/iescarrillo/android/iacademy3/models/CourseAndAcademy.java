package es.iescarrillo.android.iacademy3.models;

public class CourseAndAcademy {

    private String name;
    private String title;
    private long course_id;

    public CourseAndAcademy(String name, String title, long course_id) {
        this.name = name;
        this.title = title;
        this.course_id =course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }
}
