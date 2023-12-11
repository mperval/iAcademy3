package es.iescarrillo.android.iacademy3.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import es.iescarrillo.android.iacademy3.daos.AcademyDao;
import es.iescarrillo.android.iacademy3.daos.AdminDao;
import es.iescarrillo.android.iacademy3.daos.ClassroomDao;
import es.iescarrillo.android.iacademy3.daos.CourseDao;
import es.iescarrillo.android.iacademy3.daos.InscriptionDao;
import es.iescarrillo.android.iacademy3.daos.LessonDao;
import es.iescarrillo.android.iacademy3.daos.ManagerDao;
import es.iescarrillo.android.iacademy3.daos.StudentDao;
import es.iescarrillo.android.iacademy3.daos.TeacherDao;
import es.iescarrillo.android.iacademy3.models.Academy;
import es.iescarrillo.android.iacademy3.models.Admin;
import es.iescarrillo.android.iacademy3.models.Classroom;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.models.DomainEntity;
import es.iescarrillo.android.iacademy3.models.Inscription;
import es.iescarrillo.android.iacademy3.models.Lesson;
import es.iescarrillo.android.iacademy3.models.Manager;
import es.iescarrillo.android.iacademy3.models.Person;
import es.iescarrillo.android.iacademy3.models.Student;
import es.iescarrillo.android.iacademy3.models.Teacher;


/* Dentro de los corchetes de entities habrá que añadir las clases que queremos convertir en tablas
en nuestra base de datos.
   La versión tendremos que ir incementándola cada vez que hagamos un cambio dentro de nuestra BBDD
* */

@Database(entities = {Person.class, Admin.class, Academy.class, Classroom.class, Course.class, DomainEntity.class,
        Inscription.class, Lesson.class, Manager.class, Student.class, Teacher.class}, version = 1)
@TypeConverters({LocalDateConverter.class, LocalTimeConverter.class, LocalDateTimeConverter.class})
public abstract class DatabaseHelper extends RoomDatabase {

    // Añadir los DAOs
    public abstract AdminDao adminDao();
    public abstract ManagerDao managerDao();
    public abstract StudentDao studentDao();
    public abstract TeacherDao teacherDao();
    public abstract AcademyDao academyDao();
    public abstract ClassroomDao classroomDao();
    public abstract CourseDao courseDao();
    public abstract LessonDao lessonDao();
    public abstract InscriptionDao inscriptionDao();

    // Instancia estática de la clase, para oder usarla en toda la aplicación
    private static DatabaseHelper instance;

    // Método de Android Room para crear la base de datos
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseHelper.class, "iAcademy3")
                    .fallbackToDestructiveMigration() // Si se cambia la versión elimina y reconstruye
                    .build();
        }
        return instance;
    }

    @Override
    public void clearAllTables() {

    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return null;
    }

}
