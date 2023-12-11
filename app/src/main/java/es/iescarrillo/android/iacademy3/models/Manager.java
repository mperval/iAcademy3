package es.iescarrillo.android.iacademy3.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "manager")
public class Manager extends Person {
    @ColumnInfo(name = "dni")
    private String dni;
    @ColumnInfo(name = "phone")
    private String phone;

    public Manager() {
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



}
