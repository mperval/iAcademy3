package es.iescarrillo.android.iacademy3.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.Classroom;

@Dao
public interface ClassroomDao {
    @Insert
    long insertClassroom(Classroom classroom);

    @Delete
    void deleteClassroom(Classroom classroom);

    @Update
    void updateClassroom(Classroom classroom);

    @Query("SELECT * FROM classroom")
    List<Classroom> getAll();

    @Query("SELECT * FROM classroom c WHERE c.name = :name")
    Classroom getNameClassroom(String name);

    @Query("SELECT * FROM classroom WHERE id= :id")
    Classroom getClassroomById(Long id);

    @Query("SELECT * FROM classroom c WHERE c.name = :name and academy_id = :academy_id;")
    Classroom getClassroomByNameAndAcademyId (String name, long academy_id);

    @Query("SELECT * FROM CLASSROOM WHERE academy_id=:academyId")
    List<Classroom> getAcademyClassrooms(long academyId);
}
