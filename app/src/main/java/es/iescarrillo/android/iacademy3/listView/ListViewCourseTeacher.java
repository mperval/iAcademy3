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
import es.iescarrillo.android.iacademy3.activities.CourseTeacherActivity;
import es.iescarrillo.android.iacademy3.activities.TeacherActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterLVCourseTeacher;
import es.iescarrillo.android.iacademy3.adapters.AdapterTeacher;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.models.Teacher;
import es.iescarrillo.android.iacademy3.services.CourseService;

public class ListViewCourseTeacher extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private AdapterLVCourseTeacher adapterLVCourseTeacher;
    private List<Course> courses;
    private Button btnAddCourseTeacher;
    private ListView lvCourseTeacher;
    private CourseService courseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_course_teacher);

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long teacherId = sharedPreferences.getLong("id", 0);

        lvCourseTeacher = findViewById(R.id.lvCourseTeacher);
        btnAddCourseTeacher = findViewById(R.id.btnAddCourseTeacher);

        courseService = new CourseService(getApplication());

        //Forma de traernos las academias
        Thread thread = new Thread(() -> {

            List<Course> courseList = courseService.getCourseByIdTeacher(teacherId);

            courses = new ArrayList<>(courseList);

            runOnUiThread(() -> {

                adapterLVCourseTeacher = new AdapterLVCourseTeacher((Context) this, (ArrayList<Course>) courses);
                lvCourseTeacher.setAdapter(adapterLVCourseTeacher);
            });
        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        lvCourseTeacher.setOnItemClickListener(((parent, view, position, id) -> {
            Course course = (Course) parent.getItemAtPosition(position);

            Intent viewCourseTeacherActivity = new Intent(this, CourseTeacherActivity.class);

            viewCourseTeacherActivity.putExtra("id", course.getId());
            viewCourseTeacherActivity.putExtra("title", course.getTitle());
            viewCourseTeacherActivity.putExtra("description", course.getDescription());
            viewCourseTeacherActivity.putExtra("level", course.getLevel().toString());
            viewCourseTeacherActivity.putExtra("capacity", String.valueOf(course.getCapacity()));
            viewCourseTeacherActivity.putExtra("startDate", String.valueOf(course.getStartDate()));
            viewCourseTeacherActivity.putExtra("endDate", String.valueOf(course.getEndDate()));
            viewCourseTeacherActivity.putExtra("activated", String.valueOf(course.isActivated()));
            viewCourseTeacherActivity.putExtra("academy_id", course.getAcademyId());
            viewCourseTeacherActivity.putExtra("teacher_id", course.getTeacherId());

            startActivity(viewCourseTeacherActivity);
            finish();

        }));

        btnAddCourseTeacher.setOnClickListener(v -> {
            Intent viewCourseTeacherActivity = new Intent(this, CourseTeacherActivity.class);
            startActivity(viewCourseTeacherActivity);
            finish();
        });
    }
}