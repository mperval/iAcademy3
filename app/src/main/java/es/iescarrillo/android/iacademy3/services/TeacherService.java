package es.iescarrillo.android.iacademy3.services;

import android.app.Application;

import java.util.List;

import es.iescarrillo.android.iacademy3.daos.TeacherDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.Teacher;

public class TeacherService implements TeacherDao {

    private TeacherDao teacherDao;

    public TeacherService(Application application){
        DatabaseHelper db = DatabaseHelper.getInstance(application);
        teacherDao= db.teacherDao();
    }

    @Override
    public long insertTeacher(Teacher teacher) {
        return teacherDao.insertTeacher(teacher);
    }

    @Override
    public void updateTeacher(Teacher teacher) {
        teacherDao.updateTeacher(teacher);
    }

    @Override
    public void deleteTeacher(Teacher teacher) {
        teacherDao.deleteTeacher(teacher);
    }

    @Override
    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }

    @Override
    public Teacher getTeacherByUsername(String username) {
        return teacherDao.getTeacherByUsername(username);
    }

    @Override
    public Teacher getTeacherById(long id) {
        return teacherDao.getTeacherById(id);
    }

    @Override
    public long getAcadamyIdByTeacherId(long id) { return teacherDao.getAcadamyIdByTeacherId(id);}

    @Override
    public List<Teacher> getAcademyTeachers(long academyId) {
        return teacherDao.getAcademyTeachers(academyId);
    }



    @Override
    public long getTeacherIdByUsername(String username) {
        return teacherDao.getTeacherIdByUsername(username);

    }

    @Override
    public String getTeacherUsernameById(long id) {
        return teacherDao.getTeacherUsernameById(id);
    }




}
