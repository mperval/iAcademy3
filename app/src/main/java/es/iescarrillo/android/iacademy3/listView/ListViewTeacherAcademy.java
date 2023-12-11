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
import es.iescarrillo.android.iacademy3.activities.AcademyActivity;
import es.iescarrillo.android.iacademy3.activities.TeacherActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterAcademyAll;
import es.iescarrillo.android.iacademy3.adapters.AdapterTeacher;
import es.iescarrillo.android.iacademy3.models.Academy;
import es.iescarrillo.android.iacademy3.models.Teacher;
import es.iescarrillo.android.iacademy3.services.AcademyService;
import es.iescarrillo.android.iacademy3.services.TeacherService;

public class ListViewTeacherAcademy extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private AdapterTeacher adapterTeacher;
    private List<Teacher> teachers;
    private Button btnAddTeacher;
    private ListView lvTeacherAcademy;

    private TeacherService teacherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_teacher);

        //Varibles globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long academyIdd = sharedPreferences.getLong("academy_id", 0);
        Boolean login = sharedPreferences.getBoolean("login", false);
        Log.i(" academy_id: ", String.valueOf(academyIdd));

        lvTeacherAcademy = findViewById(R.id.lvTeacherAcademy);
        btnAddTeacher = findViewById(R.id.btnAddTeacher);

        teacherService = new TeacherService(getApplication());

        //Forma de traernos todas las academias
        Thread thread = new Thread(() -> {

            List<Teacher> teacherList = teacherService.getAcademyTeachers(academyIdd);

            teachers = new ArrayList<>(teacherList);

            runOnUiThread(() -> {

                adapterTeacher = new AdapterTeacher((Context) this, (ArrayList<Teacher>) teachers);
                lvTeacherAcademy.setAdapter(adapterTeacher);
            });
        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e){
            Log.e("error hilo", e.getMessage());
        }

        lvTeacherAcademy.setOnItemClickListener(((parent, view, position, id) -> {
            Teacher teacher = (Teacher) parent.getItemAtPosition(position);

            Intent viewTeacherActivity = new Intent(this, TeacherActivity.class);

            viewTeacherActivity.putExtra("id", teacher.getId());
            viewTeacherActivity.putExtra("name", teacher.getName());
            viewTeacherActivity.putExtra("surname", teacher.getSurname());
            viewTeacherActivity.putExtra("email", teacher.getEmail());
            viewTeacherActivity.putExtra("dni", teacher.getDni());
            viewTeacherActivity.putExtra("phone", teacher.getPhone());
            viewTeacherActivity.putExtra("address", teacher.getAddress());
            viewTeacherActivity.putExtra("username", teacher.getUserAccount().getUsername());

            startActivity(viewTeacherActivity);

        }));

        btnAddTeacher.setOnClickListener(v -> {
            Intent viewTeacherActivity = new Intent(this, TeacherActivity.class);
            startActivity(viewTeacherActivity);
            finish();
        });
    }
}