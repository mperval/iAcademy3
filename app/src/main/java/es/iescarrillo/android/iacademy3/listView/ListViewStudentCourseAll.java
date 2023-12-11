package es.iescarrillo.android.iacademy3.listView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.activities.CourseManagerActivity;
import es.iescarrillo.android.iacademy3.activities.InscriptionActivity;
import es.iescarrillo.android.iacademy3.activities.MenuActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterListViewLesson;
import es.iescarrillo.android.iacademy3.adapters.AdapterListViewStudentCourseAll;
import es.iescarrillo.android.iacademy3.models.AllCourses;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.models.IdCourseClassroomLessonInfo;
import es.iescarrillo.android.iacademy3.services.CourseService;

//ListView donde se visualiza todos los cursos que estan activados en la aplicacion.
public class ListViewStudentCourseAll extends AppCompatActivity {
    private CourseService cs;
    private ArrayList<AllCourses> allCourses;
    private AdapterListViewStudentCourseAll adapterListViewStudentCourseAll;
    private Button btnBack;
    private ListView lvAllCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_student_course_all);

        lvAllCourses = findViewById(R.id.lvAllResgisteredCourses);
        btnBack = findViewById(R.id.btnBackAllCourses);

        cs = new CourseService(getApplication());

        // Forma de traernos todas los cursos
        Thread thread = new Thread(() -> {

            List<AllCourses> courses = cs.getAllCourses();

            allCourses = new ArrayList<>(courses);

            runOnUiThread(() -> {
                adapterListViewStudentCourseAll = new AdapterListViewStudentCourseAll((Context) this, allCourses);
                lvAllCourses.setAdapter(adapterListViewStudentCourseAll);
            });
        });

        thread.start();
        try {
            thread.join(); // Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        lvAllCourses.setOnItemClickListener((parent, view, position, id) ->{
            AllCourses allCourses1 = (AllCourses) parent.getItemAtPosition(position);

            Intent viewCoursesDetails = new Intent(this, InscriptionActivity.class);
            viewCoursesDetails.putExtra("id", allCourses1.getCourseId());
            startActivity(viewCoursesDetails);

        });

        btnBack.setOnClickListener(v -> {
            Intent viewMainActivity = new Intent(this, MenuActivity.class);
            startActivity(viewMainActivity);
            finish();
        });

    }
}