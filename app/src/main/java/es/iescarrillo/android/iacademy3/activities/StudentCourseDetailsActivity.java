package es.iescarrillo.android.iacademy3.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.services.CourseService;
import es.iescarrillo.android.iacademy3.services.InscriptionService;



public class StudentCourseDetailsActivity extends AppCompatActivity {

    private Button btnBackStudentCourseDetails;
    private TextView tvTitleStudentCourseDetails, tvDescriptionStudentCourseDetails, tvLevelStudentCourseDetails, tvCapacityStudentCourseDetails,
            tvStartStudentCourseDetails, tvEndDateStudentCourseDetails, tvActivatedStudentCourseDetails;
    private CourseService courseService;
    private InscriptionService inscriptionService;
    private SharedPreferences sharedPreferences;

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_details);

        tvTitleStudentCourseDetails = findViewById(R.id.tvTitleStudentCourseDetails);
        tvDescriptionStudentCourseDetails = findViewById(R.id.tvDescriptionStudentCourseDetails);
        tvLevelStudentCourseDetails = findViewById(R.id.tvLevelStudentCourseDetails);
        tvCapacityStudentCourseDetails = findViewById(R.id.tvCapacityStudentCourseDetails);
        tvStartStudentCourseDetails = findViewById(R.id.tvStartDateStudentCourseDetails);
        tvEndDateStudentCourseDetails = findViewById(R.id.tvEndDateStudentCourseDetails);
        tvActivatedStudentCourseDetails = findViewById(R.id.tvActivatedStudentCourseDetails);


        btnBackStudentCourseDetails = findViewById(R.id.btnBackStudentCourseDetails);

        tvStartStudentCourseDetails.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(StudentCourseDetailsActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        tvStartStudentCourseDetails.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });
        tvEndDateStudentCourseDetails.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(StudentCourseDetailsActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        tvEndDateStudentCourseDetails.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });
        Intent viewStudentCourseDetails = getIntent();

        courseService = new CourseService(getApplication());
        inscriptionService = new InscriptionService(getApplication());

        //Variables Globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long studentId = sharedPreferences.getLong("id", 0);

        //Traer información de la vista anterior
        long courseId = getIntent().getLongExtra("id", 0);

        Thread thread = new Thread(() -> {

            course = courseService.getCourseById(courseId);


            tvTitleStudentCourseDetails.setText(course.getTitle());
            tvDescriptionStudentCourseDetails.setText(course.getDescription());
            tvLevelStudentCourseDetails.setText(course.getLevel());
            tvCapacityStudentCourseDetails.setText(String.valueOf(course.getCapacity()));
            tvStartStudentCourseDetails.setText(String.valueOf(course.getStartDate()));
            tvEndDateStudentCourseDetails.setText(String.valueOf(course.getEndDate()));
            tvActivatedStudentCourseDetails.setText(String.valueOf(course.isActivated()));
            long academyId = course.getAcademyId();
            long teacherId = course.getTeacherId();

        });

        thread.start();
        try {
            thread.join(); // Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }



        Log.i("id course", String.valueOf(courseId));


        //Creo un objeto curso con los datos que traigo



        btnBackStudentCourseDetails.setOnClickListener(v -> {

            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
            startActivity(viewMenuIntent);
            finish();

        });
    }
}