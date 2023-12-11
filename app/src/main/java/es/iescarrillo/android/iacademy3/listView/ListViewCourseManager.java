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
import es.iescarrillo.android.iacademy3.activities.CourseManagerActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterLVCourseTeacher;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.services.CourseService;
import es.iescarrillo.android.iacademy3.services.TeacherService;

public class ListViewCourseManager extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    private AdapterLVCourseTeacher adapterLVCourseTeacher;
    private List<Course> courses;
    private Button btnAddCourseManager;
    private ListView lvCourseManager;
    private CourseService courseService;
    private TeacherService ts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_course_manager);

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long idAcademy = sharedPreferences.getLong("academy_id", 0);

        lvCourseManager = findViewById(R.id.lvCourseManager);
        btnAddCourseManager = findViewById(R.id.btnAddCourseManager);

        courseService = new CourseService(getApplication());

        //Forma de traernos las academias
        Thread thread = new Thread(() -> {

            List<Course> courseList = courseService.getCourseByIdManager(idAcademy);

            courses = new ArrayList<>(courseList);


            runOnUiThread(() -> {

                adapterLVCourseTeacher = new AdapterLVCourseTeacher((Context) this, (ArrayList<Course>) courses);
                lvCourseManager.setAdapter(adapterLVCourseTeacher);
            });
        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        lvCourseManager.setOnItemClickListener(((parent, view, position, id) -> {
            Course course = (Course) parent.getItemAtPosition(position);

            Intent viewCourseManagerActivity = new Intent(this, CourseManagerActivity.class);

            viewCourseManagerActivity.putExtra("id", course.getId());
            viewCourseManagerActivity.putExtra("title", course.getTitle());
            viewCourseManagerActivity.putExtra("description", course.getDescription());
            viewCourseManagerActivity.putExtra("level", course.getLevel().toString());
            viewCourseManagerActivity.putExtra("capacity", String.valueOf(course.getCapacity()));
            viewCourseManagerActivity.putExtra("startDate", String.valueOf(course.getStartDate()));
            viewCourseManagerActivity.putExtra("endDate", String.valueOf(course.getEndDate()));
            viewCourseManagerActivity.putExtra("activated", String.valueOf(course.isActivated()));
            viewCourseManagerActivity.putExtra("teacher_id", course.getTeacherId());
            Log.i("teacher_id", String.valueOf(course.getTeacherId()));
            viewCourseManagerActivity.putExtra("academy_id", course.getAcademyId());

            startActivity(viewCourseManagerActivity);
            finish();

        }));

        btnAddCourseManager.setOnClickListener(v -> {
            Intent viewCourseManagerActivity = new Intent(this, CourseManagerActivity.class);
            startActivity(viewCourseManagerActivity);
            finish();
        });
    }
}