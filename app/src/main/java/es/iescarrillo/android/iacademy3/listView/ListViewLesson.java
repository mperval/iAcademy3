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
import es.iescarrillo.android.iacademy3.adapters.AdapterListViewLesson;
import es.iescarrillo.android.iacademy3.models.IdCourseClassroomLessonInfo;
import es.iescarrillo.android.iacademy3.services.LessonService;

public class ListViewLesson extends AppCompatActivity {

    private AdapterListViewLesson adapterListViewLesson;
    private ArrayList<IdCourseClassroomLessonInfo> lessonInfoList;
    private Button btnAddLesson;
    private ListView lvLesson;

    private LessonService lessonService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_lesson);

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long teacherId = sharedPreferences.getLong("id", 0);
        long academyId = sharedPreferences.getLong("academy_id", 0);

        lvLesson = findViewById(R.id.lvLesson);
        btnAddLesson = findViewById(R.id.btnAddLesson);

        lessonService = new LessonService(getApplication());


        // Forma de traernos todas las lecciones
        Thread thread = new Thread(() -> {

            List<IdCourseClassroomLessonInfo> lessonsList = lessonService.getTeacherLessons(teacherId);

            lessonInfoList = new ArrayList<>(lessonsList);

            runOnUiThread(() -> {
                adapterListViewLesson = new AdapterListViewLesson((Context) this, lessonInfoList);
                lvLesson.setAdapter(adapterListViewLesson);
            });
        });

        thread.start();
        try {
            thread.join(); // Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        lvLesson.setOnItemClickListener((parent, view, position, id) -> {
            IdCourseClassroomLessonInfo lessonInfo = (IdCourseClassroomLessonInfo) parent.getItemAtPosition(position);

            Intent viewLessonActivity = new Intent(this, LessonActivity.class);
            viewLessonActivity.putExtra("name", lessonInfo.getName());
            viewLessonActivity.putExtra("title", lessonInfo.getTitle());
            viewLessonActivity.putExtra("lessonDate", String.valueOf(lessonInfo.getLessonDate()));
            viewLessonActivity.putExtra("lessonHour", String.valueOf(lessonInfo.getLessonHour()));
            viewLessonActivity.putExtra("id", lessonInfo.getId());
            Log.i("idLesson", String.valueOf(lessonInfo.getId()));
            startActivity(viewLessonActivity);


        });

        btnAddLesson.setOnClickListener(v -> {

            Intent viewLessonActivity = new Intent(this, LessonActivity.class);
            startActivity(viewLessonActivity);
            finish();

        });
    }
}