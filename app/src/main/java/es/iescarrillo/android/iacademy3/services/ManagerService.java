package es.iescarrillo.android.iacademy3.services;

import android.app.Application;

import java.util.List;

import es.iescarrillo.android.iacademy3.daos.ManagerDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.Manager;

public class ManagerService implements ManagerDao {

    private ManagerDao managerDao;

    public ManagerService(Application application){
        DatabaseHelper db= DatabaseHelper.getInstance(application);
        managerDao = db.managerDao();
    }

    @Override
    public long insertManager(Manager manager) {
        return managerDao.insertManager(manager);
    }

    @Override
    public void updateManager(Manager manager) {
        managerDao.updateManager(manager);
    }

    @Override
    public void deleteManager(Manager manager) {
        managerDao.deleteManager(manager);
    }

    @Override
    public List<Manager> getAll() {
        return managerDao.getAll();
    }

    @Override
    public Manager getManagerByUsername(String username) {
        return managerDao.getManagerByUsername(username);
    }

    @Override
    public Manager getManagerById(Long id) {
        return managerDao.getManagerById(id);
    }



}
