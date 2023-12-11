package es.iescarrillo.android.iacademy3.listView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.activities.MenuActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterLessonsOrdered;
import es.iescarrillo.android.iacademy3.adapters.AdapterStudentInscription;
import es.iescarrillo.android.iacademy3.models.CourseAndAcademy;
import es.iescarrillo.android.iacademy3.models.LessonsOrdered;
import es.iescarrillo.android.iacademy3.services.InscriptionService;

public class ListViewLessonsOrdered extends AppCompatActivity {

    private AdapterLessonsOrdered adapterlessonsOrdered;
    private ArrayList<LessonsOrdered> lessonsOrdered;
    private Button btnBack;
    private ListView listViewLessonsOrdered;
    private InscriptionService inscriptionService;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_lessons_ordered);

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long studentId = sharedPreferences.getLong("id", 0);

        listViewLessonsOrdered = findViewById(R.id.lvMyInscriptions);
        btnBack = findViewById(R.id.btnBackLessonsOrdered);

        inscriptionService = new InscriptionService(getApplication());

        // Forma de traernos las lessons ordenadas del alumno
        Thread thread = new Thread(() -> {

            List<LessonsOrdered> lessonsOrdereds = inscriptionService.getLessonsOrdered(studentId);

            lessonsOrdered = new ArrayList<>(lessonsOrdereds);

            runOnUiThread(() -> {
                adapterlessonsOrdered = new AdapterLessonsOrdered((Context) this, lessonsOrdered);
                listViewLessonsOrdered.setAdapter(adapterlessonsOrdered);
            });
        });

        thread.start();
        try {
            thread.join(); // Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        btnBack.setOnClickListener(v -> {

            Intent viewLessonActivity = new Intent(this, ListViewStudentInscription.class);
            startActivity(viewLessonActivity);
            finish();

        });
    }
}