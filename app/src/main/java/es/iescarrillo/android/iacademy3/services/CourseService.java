package es.iescarrillo.android.iacademy3.services;

import android.app.Application;

import java.util.List;
import android.util.Log;

import es.iescarrillo.android.iacademy3.daos.CourseDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.AllCourses;
import es.iescarrillo.android.iacademy3.models.Course;

public class CourseService implements CourseDao {
    private CourseDao courseDao;
    public CourseService(Application application) {
        DatabaseHelper db = DatabaseHelper.getInstance(application);
        courseDao = db.courseDao();
    }
    @Override
    public long insertCourse(Course course) {
        return courseDao.insertCourse(course);
    }

    @Override
    public void deleteCourse(Course course) {
        courseDao.deleteCourse(course);
    }

    @Override
    public void updateCourse(Course course) {
        courseDao.updateCourse(course);
    }

    @Override
    public List<Course> getAll() {
        return courseDao.getAll();
    }

    @Override
    public List<Course> getCourseByAcademyId(long academyId) {
        return courseDao.getCourseByAcademyId(academyId);
    }

    @Override
    public long getCourseIdByAcademyAndTitle(long idAcademy, String courseTitle) {
        return 0;
    }
    public long getCourseIdByTitle(String title) {
        return courseDao.getCourseIdByTitle(title);
    }

    @Override
    public boolean countCoursesByAcademyAndTitle(long idAcademy, String courseTitle) {
        return courseDao.countCoursesByAcademyAndTitle(idAcademy, courseTitle);
    }

    @Override
    public List<Course> getCourseByIdManager(long academy_id) {
        return courseDao.getCourseByIdManager(academy_id);
    }

    @Override
    public Course getCourseById(long id) { return courseDao.getCourseById(id); }

    @Override
    public List<Course> getCourseByIdTeacher(long teacher_id) { return courseDao.getCourseByIdTeacher(teacher_id); }

    public Course getCourseByTitle(String title, long academy_id) {
        return courseDao.getCourseByTitle(title, academy_id);
    }

    @Override
    public void updateAllCoursesActivation() {

        Log.d("CourseService", "updateAllCoursesActivation - Start");
        courseDao.updateAllCoursesActivation();
        Log.d("CourseService", "updateAllCoursesActivation - End");
    }

    @Override
    public List<AllCourses> getAllCourses() {
        return courseDao.getAllCourses();
    }


}
