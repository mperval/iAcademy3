package es.iescarrillo.android.iacademy3.models;

public class AllCourses {

    private String title;
    private String academy_name;
    private long course_id;

    public AllCourses(String title, String academy_name, long course_id) {
        this.title = title;
        this.academy_name = academy_name;
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return academy_name;
    }

    public void setName(String academy_name) {
        this.academy_name = academy_name;
    }

    public long getCourseId() {
        return course_id;
    }

    public void setCourseId(long courseId) {
        this.course_id = courseId;
    }
}
