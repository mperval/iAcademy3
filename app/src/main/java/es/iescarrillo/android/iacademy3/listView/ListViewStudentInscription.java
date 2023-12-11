package es.iescarrillo.android.iacademy3.listView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.activities.LessonActivity;
import es.iescarrillo.android.iacademy3.activities.MenuActivity;
import es.iescarrillo.android.iacademy3.activities.StudentCourseDetailsActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterStudentInscription;
import es.iescarrillo.android.iacademy3.models.CourseAndAcademy;
import es.iescarrillo.android.iacademy3.models.IdCourseClassroomLessonInfo;
import es.iescarrillo.android.iacademy3.services.InscriptionService;

public class ListViewStudentInscription extends AppCompatActivity {

    private AdapterStudentInscription adapterStudentInscription;
    private ArrayList<CourseAndAcademy> courseAndAcademiesListInfo;
    private Button btnBack, btnMyLessons;
    private ListView lvMyInscriptions;
    private InscriptionService inscriptionService;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_student_inscription);

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long studentId = sharedPreferences.getLong("id", 0);

        lvMyInscriptions = findViewById(R.id.lvMyInscriptions);
        btnBack = findViewById(R.id.btnBack);
        btnMyLessons = findViewById(R.id.btnMyLessons);

        inscriptionService = new InscriptionService(getApplication());

        // Forma de traernos todas las lecciones
        Thread thread = new Thread(() -> {

            List<CourseAndAcademy> courseAndAcademiesList = inscriptionService.getCourseAndAcademy(studentId);

            courseAndAcademiesListInfo = new ArrayList<>(courseAndAcademiesList);

            runOnUiThread(() -> {
                adapterStudentInscription = new AdapterStudentInscription((Context) this, courseAndAcademiesListInfo);
                lvMyInscriptions.setAdapter(adapterStudentInscription);
            });
        });

        thread.start();
        try {
            thread.join(); // Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        lvMyInscriptions.setOnItemClickListener((parent, view, position, id) -> {
            CourseAndAcademy courseAndAcademy = (CourseAndAcademy) parent.getItemAtPosition(position);

            Intent viewLessonActivity = new Intent(this, StudentCourseDetailsActivity.class);

            viewLessonActivity.putExtra("id", courseAndAcademy.getCourse_id());

            startActivity(viewLessonActivity);

            finish();


        });

        btnBack.setOnClickListener(v -> {

            Intent viewLessonActivity = new Intent(this, MenuActivity.class);
            startActivity(viewLessonActivity);
            finish();

        });

        btnMyLessons.setOnClickListener(v -> {

            Intent viewLessonsOrdered = new Intent(this, ListViewLessonsOrdered.class);
            startActivity(viewLessonsOrdered);

            finish();

        });





    }
}