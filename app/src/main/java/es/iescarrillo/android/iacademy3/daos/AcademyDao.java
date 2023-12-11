package es.iescarrillo.android.iacademy3.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.Academy;

@Dao
public interface AcademyDao {

    @Insert
    long insertAcademy(Academy academy);

    @Update
    void updateAcademy(Academy academy);

    @Delete
    void deleteAcademy(Academy academy);

    @Query("SELECT * FROM academy")
    List<Academy> getAll();

    @Query("SELECT * FROM academy a WHERE a.name = :academy")
    Academy getNameAcademy(String academy);

    @Query("SELECT * FROM academy WHERE manager_id = :manager_id")
    Academy getAcademyManagerById(long manager_id);

    @Query("SELECT id FROM academy WHERE name = :name LIMIT 1")
    long getAcademyIdByName(String name);
}
