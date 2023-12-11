
package es.iescarrillo.android.iacademy3.services;

import android.app.Application;

import java.util.List;

import es.iescarrillo.android.iacademy3.daos.InscriptionDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.CourseAndAcademy;
import es.iescarrillo.android.iacademy3.models.Inscription;
import es.iescarrillo.android.iacademy3.models.LessonsOrdered;

public class InscriptionService implements InscriptionDao {

    private InscriptionDao inscriptionDao;

    public InscriptionService(Application application) {
        DatabaseHelper db = DatabaseHelper.getInstance(application);
        inscriptionDao = db.inscriptionDao();
    }

    @Override
    public long insertInscription(Inscription inscription) {
        return inscriptionDao.insertInscription(inscription);
    }

    @Override
    public void updateInscription(Inscription inscription) {
        inscriptionDao.updateInscription(inscription);
    }

    @Override
    public void deleteInscription(Inscription inscription) {
        inscriptionDao.deleteInscription(inscription);
    }

    @Override
    public List<Inscription> getAll() {
        return inscriptionDao.getAll();
    }

    @Override
    public Inscription getInscriptionByStudentAndCourse(long studentId, long courseId) {
        return inscriptionDao.getInscriptionByStudentAndCourse(studentId, courseId);
    }

    @Override
    public void deleteInscription(long studentId, long courseId) {

    }

    @Override
    public int getNumberOfStudentsInCourse(long courseId) {
        return inscriptionDao.getNumberOfStudentsInCourse(courseId);
    }

    @Override
    public List<CourseAndAcademy> getCourseAndAcademy(long studentId){
        return inscriptionDao.getCourseAndAcademy(studentId);
    }

    @Override
    public List<LessonsOrdered> getLessonsOrdered(long studentId) {
        return inscriptionDao.getLessonsOrdered(studentId);
    }


}