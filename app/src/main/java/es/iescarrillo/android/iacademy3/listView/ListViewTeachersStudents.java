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
import es.iescarrillo.android.iacademy3.activities.MenuActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterLVTeachersStudents;
import es.iescarrillo.android.iacademy3.models.StudentCourseInfo;
import es.iescarrillo.android.iacademy3.services.StudentService;

public class ListViewTeachersStudents extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Button btnBack;
    private ListView lvTeachersStudents;
    private AdapterLVTeachersStudents adapterLVTeachersStudents;
    private List<StudentCourseInfo> studentCourseInfo;
    private StudentService studentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_teachers_students);

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long teacherId = sharedPreferences.getLong("id", 0);

        lvTeachersStudents = findViewById(R.id.lvTeachersStudents);
        btnBack = findViewById(R.id.btnBackMenuTeachersStudentsListView);

        studentService = new StudentService(getApplication());

        //Forma de traernos student y course
        Thread thread = new Thread(() -> {

            List<StudentCourseInfo> list = studentService.getStudentsByTeacherId(teacherId);

            studentCourseInfo = new ArrayList<>(list);

            runOnUiThread(() -> {

                adapterLVTeachersStudents = new AdapterLVTeachersStudents((Context) this, (ArrayList<StudentCourseInfo>) studentCourseInfo);
                lvTeachersStudents.setAdapter(adapterLVTeachersStudents);
            });
        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }


        lvTeachersStudents.setOnItemClickListener(((parent, view, position, id) ->{

            StudentCourseInfo studentCourseInfo1 = (StudentCourseInfo) parent.getItemAtPosition(position);





        }));

        btnBack.setOnClickListener(v -> {
            Intent viewMenuActivity = new Intent(this, MenuActivity.class);
            startActivity(viewMenuActivity);
            finish();
        });

    }
}