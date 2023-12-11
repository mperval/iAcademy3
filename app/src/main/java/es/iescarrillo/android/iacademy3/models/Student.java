package es.iescarrillo.android.iacademy3.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.time.LocalDate;
@Entity(tableName = "student")
public class Student extends Person{
    @ColumnInfo(name = "dni")
    private String dni;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "familyPhone")
    private String familyPhone;
    @ColumnInfo(name = "birthday")
    private LocalDate birthday;

    public Student() {
        super();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Student{" +
                "dni='" + dni + '\'' +
                ", phone='" + phone + '\'' +
                ", familyPhone='" + familyPhone + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
