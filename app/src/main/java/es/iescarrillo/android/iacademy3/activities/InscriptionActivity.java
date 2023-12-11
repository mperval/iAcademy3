package es.iescarrillo.android.iacademy3.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.util.Calendar;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.models.Inscription;
import es.iescarrillo.android.iacademy3.services.CourseService;
import es.iescarrillo.android.iacademy3.services.InscriptionService;

public class InscriptionActivity extends AppCompatActivity {

    private Button btnInscriptionCourseStudent, btnBackCourseStudent;

    private TextView tvTitleCourseStudent, tvDescriptionCourseStudent, tvLevelCourseStudent, tvCapacityCourseStudent,
            tvStartDateCourseStudent, tvEndDateCourseStudent, tvActivatedCourseStudent;
    private CourseService courseService;
    private InscriptionService inscriptionService;
    private SharedPreferences sharedPreferences;

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_student);

        tvTitleCourseStudent = findViewById(R.id.tvTitleCourseStudent);
        tvDescriptionCourseStudent = findViewById(R.id.tvDescriptionCourseStudentActivity);
        tvLevelCourseStudent = findViewById(R.id.tvLevelCourseStudentActivity);
        tvCapacityCourseStudent = findViewById(R.id.tvCapacityCourseStudentActivity);
        tvStartDateCourseStudent = findViewById(R.id.tvStartDateCourseStudentActivity);
        tvEndDateCourseStudent = findViewById(R.id.tvEndDateCourseStudentActivity);
        tvActivatedCourseStudent = findViewById(R.id.tvActivatedCourseStudentActivity);

        btnInscriptionCourseStudent = findViewById(R.id.btnInscriptionCourseStudentActivity);
        btnBackCourseStudent = findViewById(R.id.btnBackCourseStudentActivity);

        tvStartDateCourseStudent.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(InscriptionActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        tvStartDateCourseStudent.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });
        tvEndDateCourseStudent.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(InscriptionActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        tvEndDateCourseStudent.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });

        Intent viewCourseTeacherActivityIntent = getIntent();

        courseService = new CourseService(getApplication());
        inscriptionService = new InscriptionService(getApplication());

        //Variables Globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long studentId = sharedPreferences.getLong("id", 0);

        //Traer información de la vista anterior
        long courseId = getIntent().getLongExtra("id", 0);

        Thread thread = new Thread(() -> {

            course = courseService.getCourseById(courseId);

            tvTitleCourseStudent.setText(course.getTitle());
            tvDescriptionCourseStudent.setText(course.getDescription());
            tvLevelCourseStudent.setText(course.getLevel());
            tvCapacityCourseStudent.setText(String.valueOf(course.getCapacity()));
            tvStartDateCourseStudent.setText(String.valueOf(course.getStartDate()));
            tvEndDateCourseStudent.setText(String.valueOf(course.getEndDate()));
            tvActivatedCourseStudent.setText(String.valueOf(course.isActivated()));
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

        btnInscriptionCourseStudent.setOnClickListener(v -> {
            if (String.valueOf(course.isActivated()).equalsIgnoreCase("false")) {
                // Manejar la situación en la que el curso esté desactivado
                runOnUiThread(() -> {
                    Toast.makeText(InscriptionActivity.this, "Course deactivated", Toast.LENGTH_SHORT).show();
                });
            } else {
                new Thread(() -> {
                    if (inscriptionService.getInscriptionByStudentAndCourse(studentId, courseId) != null) {
                        // Manejar la situación en la que ya esté matriculado en el curso
                        runOnUiThread(() -> {
                            Toast.makeText(InscriptionActivity.this, "Already enrolled in the course", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Inscription inscription = new Inscription();
                        inscription.setStudentId(studentId);
                        inscription.setCourseId(courseId);
                        inscription.setRegistrationTime(LocalDateTime.now());

                        long idInscription = inscriptionService.insertInscription(inscription);

                        Log.i("Id_inscription", String.valueOf(idInscription));

                        runOnUiThread(() -> {
                            Toast.makeText(InscriptionActivity.this, "Enrolled", Toast.LENGTH_SHORT).show();
                        });

                        Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                        startActivity(viewMenuIntent);
                        finish();
                    }
                }).start();
            }
        });

        btnBackCourseStudent.setOnClickListener(v -> {

            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
            startActivity(viewMenuIntent);
            finish();

        });
    }
}