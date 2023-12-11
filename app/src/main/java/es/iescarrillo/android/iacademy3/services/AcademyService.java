package es.iescarrillo.android.iacademy3.services;

import android.app.Application;

import java.util.List;

import es.iescarrillo.android.iacademy3.daos.AcademyDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.Academy;

public class AcademyService implements AcademyDao {

private AcademyDao academyDao;

    public AcademyService(Application application){
        DatabaseHelper db = DatabaseHelper.getInstance(application);
        //se ponen los servicios
        academyDao = db.academyDao();
    }

    @Override
    public long insertAcademy(Academy academy) {
        return academyDao.insertAcademy(academy);
    }

    @Override
    public void updateAcademy(Academy academy) { academyDao.updateAcademy(academy);}

    @Override
    public void deleteAcademy(Academy academy) {academyDao.deleteAcademy(academy);}

    @Override
    public List<Academy> getAll() {
        return academyDao.getAll();
    }

    @Override
    public Academy getNameAcademy(String academy) {
        return academyDao.getNameAcademy(academy);
    }

    @Override
    public Academy getAcademyManagerById(long manager_id) {return academyDao.getAcademyManagerById(manager_id);}

    @Override
    public long getAcademyIdByName(String name) {
        return academyDao.getAcademyIdByName(name);
    }
}
