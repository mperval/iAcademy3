package es.iescarrillo.android.iacademy3.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.time.LocalDate;
@Entity(tableName = "course",
        foreignKeys = {
                @ForeignKey(entity = Academy.class, parentColumns = "id",
                        childColumns = "academy_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Teacher.class, parentColumns = "id",
                        childColumns = "teacher_id", onDelete = ForeignKey.CASCADE)
        })
public class Course extends DomainEntity{

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "level")
    private String level;
    @ColumnInfo(name = "capacity")
    private Integer capacity;
    @ColumnInfo(name = "startDate")
    private LocalDate startDate;
    @ColumnInfo(name = "endDate")
    private LocalDate endDate;
    @ColumnInfo(name = "activated")
    private Boolean activated;
    @ColumnInfo(name = "academy_id")
    private long academyId;
    @ColumnInfo(name = "teacher_id")
    private long teacherId;

    // Constructor
    public Course() {
        super();
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public long getAcademyId() {
        return academyId;
    }

    public void setAcademyId(long academyId) {
        this.academyId = academyId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }


}

