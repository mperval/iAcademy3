package es.iescarrillo.android.iacademy3.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "classroom", foreignKeys = @ForeignKey(entity = Academy.class, parentColumns = "id",
        childColumns = "academy_id", onDelete = ForeignKey.CASCADE))
public class Classroom extends DomainEntity {

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "capacity")
    private Integer capacity;
    @ColumnInfo(name = "academy_id")
    private long academyId;

    public Classroom() {
        super();
    }

    //Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public long getAcademyId() {
        return academyId;
    }

    public void setAcademyId(long academyId) {
        this.academyId = academyId;
    }

}
