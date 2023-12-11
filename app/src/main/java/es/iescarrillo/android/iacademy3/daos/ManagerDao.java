package es.iescarrillo.android.iacademy3.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.Manager;
import es.iescarrillo.android.iacademy3.models.Teacher;

@Dao
public interface ManagerDao {

    @Insert
    long insertManager(Manager manager);

    @Update
    void updateManager(Manager manager);

    @Delete
    void deleteManager(Manager manager);

    @Query("SELECT * FROM manager")
    List<Manager> getAll();

    @Query("SELECT * FROM manager WHERE username = :username")
    Manager getManagerByUsername(String username);

    @Query("SELECT * FROM manager WHERE id = :id")
    Manager getManagerById(Long id);


}
