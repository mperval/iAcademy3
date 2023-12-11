package es.iescarrillo.android.iacademy3.services;

import android.app.Application;

import java.util.List;

import es.iescarrillo.android.iacademy3.daos.StudentDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.Student;
import es.iescarrillo.android.iacademy3.models.StudentCourseInfo;

public class StudentService implements StudentDao {

    private StudentDao studentDao;

    public StudentService(Application application){
        DatabaseHelper db=DatabaseHelper.getInstance(application);
        studentDao=db.studentDao();
    }

    @Override
    public long insertStudent(Student student) {
        return studentDao.insertStudent(student);
    }

    @Override
    public void updateStudent(Student student) {
        studentDao.updateStudent(student);
    }

    @Override
    public void deleteStudent(Student student) {
        studentDao.deleteStudent(student);
    }

    @Override
    public List<Student> getAll() {
        return studentDao.getAll();
    }

    @Override
    public Student getStudentByUsername(String username) {
        return studentDao.getStudentByUsername(username);
    }

    @Override
    public Student getStudentById(long id) {return studentDao.getStudentById(id);}

    @Override
    public long getStudentIdByUsername(String username) {
        return 0;
    }

    @Override
    public Student getStudentById(Long id) {
        return studentDao.getStudentById(id);
    }

    @Override
    public List<StudentCourseInfo> getStudentsByTeacherId(long teacher_id) { return studentDao.getStudentsByTeacherId(teacher_id);}
}
