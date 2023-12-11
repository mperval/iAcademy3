package es.iescarrillo.android.iacademy3.services;

import android.app.Application;

import java.util.List;

import es.iescarrillo.android.iacademy3.daos.ClassroomDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.Classroom;

public class ClassroomService implements ClassroomDao {
   private ClassroomDao classroomDao;
    public ClassroomService(Application application) {
        DatabaseHelper db = DatabaseHelper.getInstance(application);
        classroomDao = db.classroomDao();
    }
    @Override
    public long insertClassroom(Classroom classroom) {
        return classroomDao.insertClassroom(classroom);
    }

    @Override
    public void deleteClassroom(Classroom classroom) {
        classroomDao.deleteClassroom(classroom);
    }

    @Override
    public void updateClassroom(Classroom classroom) {
        classroomDao.updateClassroom(classroom);
    }

    @Override
    public List<Classroom> getAll() {
        return classroomDao.getAll();
    }

    @Override
    public Classroom getNameClassroom(String name) {
        return classroomDao.getNameClassroom(name);
    }

    @Override
    public Classroom getClassroomById(Long id) {
        return classroomDao.getClassroomById(id);
    }

    @Override
    public Classroom getClassroomByNameAndAcademyId (String name, long academy_id) {return classroomDao.getClassroomByNameAndAcademyId(name, academy_id);}

    @Override
    public List<Classroom> getAcademyClassrooms(long academyId) {
        return classroomDao.getAcademyClassrooms(academyId);
    }
}
