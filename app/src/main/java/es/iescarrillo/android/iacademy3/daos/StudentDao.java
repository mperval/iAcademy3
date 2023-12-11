package es.iescarrillo.android.iacademy3.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.Student;
import es.iescarrillo.android.iacademy3.models.StudentCourseInfo;

@Dao
public interface StudentDao {

    @Insert
    long insertStudent (Student student);

    @Update
    void updateStudent (Student student);

    @Delete
    void deleteStudent (Student student);

    @Query("SELECT * FROM student")
    List<Student> getAll();

    @Query("SELECT * FROM student WHERE username = :username")
    Student getStudentByUsername(String username);

    @Query("SELECT * FROM student WHERE id=:id")
    Student getStudentById(long id);

    @Query("SELECT id FROM student WHERE username = :username")
    long getStudentIdByUsername(String username);

    @Query("SELECT * FROM student WHERE id = :id")
    Student getStudentById(Long id);

    @Query("SELECT S.NAME, S.SURNAME, C.TITLE FROM STUDENT S JOIN INSCRIPTION I ON S.ID = I.STUDENT_ID JOIN COURSE C ON I.COURSE_ID = C.ID WHERE C.TEACHER_ID = :teacher_id;")
    List<StudentCourseInfo> getStudentsByTeacherId(long teacher_id);


}
