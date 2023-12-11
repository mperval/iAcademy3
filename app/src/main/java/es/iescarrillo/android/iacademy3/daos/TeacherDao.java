package es.iescarrillo.android.iacademy3.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.Teacher;

@Dao
public interface TeacherDao {

    @Insert
    long insertTeacher(Teacher teacher);

    @Update
    void updateTeacher(Teacher teacher);

    @Delete
    void deleteTeacher(Teacher teacher);

    @Query("SELECT * FROM teacher")
    List<Teacher> getAll();

    @Query("SELECT * FROM teacher WHERE username = :username")
    Teacher getTeacherByUsername(String username);

    @Query("SELECT * FROM teacher WHERE id= :id")
    Teacher getTeacherById(long id);

    @Query("SELECT academy_id FROM TEACHER WHERE id =:id")
    long getAcadamyIdByTeacherId(long id);

    @Query("SELECT * FROM TEACHER WHERE academy_id=:academyId")
    List<Teacher> getAcademyTeachers(long academyId);

    @Query("SELECT id FROM teacher WHERE username = :username LIMIT 1")
    long getTeacherIdByUsername(String username);

    @Query("SELECT username FROM teacher WHERE id = :id")
    String getTeacherUsernameById(long id);


}
