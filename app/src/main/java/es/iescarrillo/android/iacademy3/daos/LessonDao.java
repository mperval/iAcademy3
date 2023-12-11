package es.iescarrillo.android.iacademy3.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.CourseClassroomLessonInfo;
import es.iescarrillo.android.iacademy3.models.IdCourseClassroomLessonInfo;
import es.iescarrillo.android.iacademy3.models.Lesson;

@Dao
public interface LessonDao {
    @Insert
    long insertLesson(Lesson lesson);

    @Delete
    void deleteLesson(Lesson lesson);

    @Update
    void updateLesson(Lesson lesson);

    @Query("SELECT * FROM lesson")
    List<Lesson> getAll();

    @Query("SELECT C.TITLE AS title, CL.NAME AS name, L.LESSONDATE, L.LESSONHOUR " +
            "FROM LESSON L " +
            "JOIN CLASSROOM CL ON L.CLASSROOM_ID = CL.ID " +
            "JOIN COURSE C ON L.COURSE_ID = C.ID " +
            "WHERE C.TEACHER_ID = :teacherId;")
    List<CourseClassroomLessonInfo> getLessonInfoByTeacherId(long teacherId);

    @Query("SELECT C.TITLE AS title, CL.NAME AS name, L.LESSONDATE, L.LESSONHOUR, L.id " +
            "FROM LESSON L " +
            "JOIN CLASSROOM CL ON L.CLASSROOM_ID = CL.ID " +
            "JOIN COURSE C ON L.COURSE_ID = C.ID " +
            "WHERE C.TEACHER_ID = :teacherId;")
    List<IdCourseClassroomLessonInfo> getTeacherLessons(long teacherId);

    /*@Query("SELECT C.TITLE AS title, CL.NAME AS name, L.LESSONDATE, L.LESSONHOUR, C.id " +
            "FROM LESSON L " +
            "JOIN CLASSROOM CL ON L.CLASSROOM_ID = CL.ID " +
            "JOIN COURSE C ON L.COURSE_ID = C.ID " +
            "WHERE C.TEACHER_ID = :teacherId;")
    List<IdCourseClassroomLessonInfo> getStudentLessons(long studentId);*/



}
