package es.iescarrillo.android.iacademy3.models;

public class StudentCourseInfo {

    private String name;
    private String surname;
    private String title;

    public StudentCourseInfo(String name, String surname, String title) {
        this.name = name;
        this.surname = surname;
        this.title = title;
    }

    // Getters y setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
