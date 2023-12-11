package es.iescarrillo.android.iacademy3.services;

import android.app.Application;

import java.util.List;

import es.iescarrillo.android.iacademy3.daos.AdminDao;
import es.iescarrillo.android.iacademy3.database.DatabaseHelper;
import es.iescarrillo.android.iacademy3.models.Admin;

public class AdminService implements AdminDao {

    private AdminDao adminDao;

    public AdminService(Application application){
        DatabaseHelper db = DatabaseHelper.getInstance(application);
        adminDao = db.adminDao();
    }

    @Override
    public long insertAdmin(Admin admin) {
        return adminDao.insertAdmin(admin);
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminDao.updateAdmin(admin);
    }

    @Override
    public void deleteAdmin(Admin admin) {
        adminDao.deleteAdmin(admin);
    }

    @Override
    public List<Admin> getAll() {
        return adminDao.getAll();
    }
}
