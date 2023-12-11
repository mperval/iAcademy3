package es.iescarrillo.android.iacademy3.services;

import android.app.Application;
import android.util.Log;

import java.util.List;

import es.iescarrillo.android.iacademy3.daos.LessonDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.CourseClassroomLessonInfo;
import es.iescarrillo.android.iacademy3.models.IdCourseClassroomLessonInfo;
import es.iescarrillo.android.iacademy3.models.Lesson;

public class LessonService implements LessonDao {
    private LessonDao lessonDao;
    public LessonService(Application application) {
        DatabaseHelper db = DatabaseHelper.getInstance(application);
        lessonDao = db.lessonDao();
    }
    @Override
    public long insertLesson(Lesson lesson) {
        return lessonDao.insertLesson(lesson);
    }

    @Override
    public void deleteLesson(Lesson lesson) {
        lessonDao.deleteLesson(lesson);
    }

    @Override
    public void updateLesson(Lesson lesson) {
        Log.d("LessonService", "updateStart");
        lessonDao.updateLesson(lesson);
        Log.d("LessonService", "updateFinish");
    }

    @Override
    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    public List<CourseClassroomLessonInfo> getLessonInfoByTeacherId(long teacherId) {
        return lessonDao.getLessonInfoByTeacherId(teacherId);
    }

    public List<IdCourseClassroomLessonInfo> getTeacherLessons(long teacherId){
        return lessonDao.getTeacherLessons(teacherId);
    }






}
