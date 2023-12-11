package es.iescarrillo.android.iacademy3.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.CourseAndAcademy;
import es.iescarrillo.android.iacademy3.models.Inscription;
import es.iescarrillo.android.iacademy3.models.LessonsOrdered;

@Dao
public interface InscriptionDao {

    @Insert
    long insertInscription(Inscription inscription);

    @Update
    void updateInscription(Inscription inscription);

    @Delete
    void deleteInscription(Inscription inscription);

    @Query("SELECT * FROM inscription")
    List<Inscription> getAll();

    // 2 Query para inscribir el alumno al curso
    @Query("SELECT * FROM inscription WHERE student_id = :studentId AND course_id = :courseId")
    Inscription getInscriptionByStudentAndCourse(long studentId, long courseId);

    @Query("DELETE FROM inscription WHERE student_id = :studentId AND course_id = :courseId")
    void deleteInscription(long studentId, long courseId);

    @Query("SELECT COUNT(*) FROM inscription WHERE course_id = :courseId")
    int getNumberOfStudentsInCourse(long courseId);

    @Query("SELECT Academy.name AS name, Course.title AS title, Course.id as course_id " +
            "FROM Inscription " +
            "INNER JOIN Course ON Inscription.course_id = Course.id " +
            "INNER JOIN Academy ON Course.academy_id = Academy.id " +
            "WHERE Inscription.student_id = :studentId")
    List<CourseAndAcademy> getCourseAndAcademy(long studentId);

    @Query("SELECT   Lesson.lessonDate, Lesson.lessonHour, Classroom.name AS classroomName, Course.title AS title FROM Inscription \n" +
            "       INNER JOIN Course ON Inscription.course_id = Course.id \n" +
            "       INNER JOIN Lesson ON Course.id = Lesson.course_id \n" +
            "       INNER JOIN Classroom ON Lesson.classroom_id = Classroom.id \n" +
            "       WHERE Inscription.student_id = :studentId ORDER BY Lesson.lessonDate ASC, Lesson.lessonHour ASC;")
    List<LessonsOrdered> getLessonsOrdered (long studentId);




}

