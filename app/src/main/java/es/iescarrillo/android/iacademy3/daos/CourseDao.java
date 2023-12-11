package es.iescarrillo.android.iacademy3.daos;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Query;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.AllCourses;
import es.iescarrillo.android.iacademy3.models.Course;

@Dao
public interface CourseDao {
    @Insert
    long insertCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Query("SELECT * FROM course")
    List<Course> getAll();

    @Query("SELECT * FROM course WHERE academy_id= :academy_id")
    List<Course> getCourseByAcademyId(long academy_id);

    @Query("SELECT id FROM course WHERE title = :title")
    long getCourseIdByTitle(String title);

    @Query("SELECT * FROM course WHERE id = :id")
    Course getCourseById(long id);

    @Query("SELECT * FROM course WHERE teacher_id = :teacher_id")
    List<Course> getCourseByIdTeacher(long teacher_id);

    @Query("SELECT id FROM course WHERE academy_id = :idAcademy AND title = :courseTitle")
    long getCourseIdByAcademyAndTitle(long idAcademy, String courseTitle);

    @Query("SELECT COUNT(*) FROM course WHERE academy_id = :idAcademy AND title = :courseTitle")
    boolean countCoursesByAcademyAndTitle(long idAcademy, String courseTitle);

    @Query("SELECT * FROM course WHERE academy_id = :academy_id")
    List<Course> getCourseByIdManager(long academy_id);

    @Query("SELECT * FROM course WHERE title = :title and academy_id = :academy_id")
    Course getCourseByTitle(String title, long academy_id);

    @Query("UPDATE course SET activated = CASE WHEN (SELECT COUNT(*) FROM inscription WHERE course_id = course.id) >= capacity THEN 0 ELSE activated END")
    void updateAllCoursesActivation();

    @Query("SELECT academy.name AS academy_name, course.id AS course_id, course.title FROM academy\n" +
            "INNER JOIN course ON academy.id = course.academy_id")
    List<AllCourses> getAllCourses();

}
