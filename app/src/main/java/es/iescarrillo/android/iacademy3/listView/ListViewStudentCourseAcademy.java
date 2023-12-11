package es.iescarrillo.android.iacademy3.listView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.activities.InscriptionActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterListViewStudentCourseAcademy;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.services.CourseService;

//listar el curso de una academia. (Este activity solo se muestra con el boton del activity AcademyAllTemplateActivity.java).
public class ListViewStudentCourseAcademy extends AppCompatActivity {
    private CourseService courseService;
    private AdapterListViewStudentCourseAcademy adapterListViewStudentCourseAcademy;
    private List<Course> courses;
    private Button backAcademyCourses;
    private ListView lvAcademyCourse;
    private TextView tvAcademyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_student_course_academy);

        lvAcademyCourse = findViewById(R.id.lvAcademyCourses);
        tvAcademyName = findViewById(R.id.tvTitleAcademyCourses);
        backAcademyCourses =findViewById(R.id.btnBackAcademyCourses);
        courseService = new CourseService(getApplication());

        Intent viewLvCourseAcademyIntent = getIntent();

        //Traer información de la vista anterior
        long academyId = getIntent().getLongExtra("id", 0);
        tvAcademyName.setText(viewLvCourseAcademyIntent.getStringExtra("name") + " Academy Courses");

        Log.i("id_academy", String.valueOf(academyId));

        //Forma de traernos los cursos
        Thread thread = new Thread(() -> {

            List<Course> courseList = courseService.getCourseByAcademyId(academyId);

            courses = new ArrayList<>(courseList);

            runOnUiThread(() -> {

                adapterListViewStudentCourseAcademy = new AdapterListViewStudentCourseAcademy((Context) this, (ArrayList<Course>) courses);
                lvAcademyCourse.setAdapter(adapterListViewStudentCourseAcademy);
            });
        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }


        lvAcademyCourse.setOnItemClickListener((parent, view, position, id) -> {
            Course course = (Course) parent.getItemAtPosition(position);

            Intent viewCourseTeacherActivity = new Intent(this, InscriptionActivity.class);

            viewCourseTeacherActivity.putExtra("id", course.getId());
            viewCourseTeacherActivity.putExtra("title", course.getTitle());
            viewCourseTeacherActivity.putExtra("description", course.getDescription());
            viewCourseTeacherActivity.putExtra("level", course.getLevel().toString());
            viewCourseTeacherActivity.putExtra("capacity", String.valueOf(course.getCapacity()));
            viewCourseTeacherActivity.putExtra("startDate", String.valueOf(course.getStartDate()));
            viewCourseTeacherActivity.putExtra("endDate", String.valueOf(course.getEndDate()));
            viewCourseTeacherActivity.putExtra("activated", String.valueOf(course.isActivated()));
            viewCourseTeacherActivity.putExtra("academy_id", String.valueOf(course.getAcademyId()));
            viewCourseTeacherActivity.putExtra("teacher_id", String.valueOf(course.getTeacherId()));

            startActivity(viewCourseTeacherActivity);
            finish();
        });

        backAcademyCourses.setOnClickListener(v -> {
            Intent viewStudentAcademyAll = new Intent(this, ListViewStudentAcademyAll.class);
            startActivity(viewStudentAcademyAll);
            finish();
        });

    }
}