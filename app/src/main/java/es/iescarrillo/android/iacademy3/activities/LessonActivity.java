package es.iescarrillo.android.iacademy3.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Classroom;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.models.Lesson;
import es.iescarrillo.android.iacademy3.services.ClassroomService;
import es.iescarrillo.android.iacademy3.services.CourseService;
import es.iescarrillo.android.iacademy3.services.LessonService;

public class LessonActivity extends AppCompatActivity {
    private Button btnTime, btnAddLesson, btnUpdateLesson, btnBackLesson;
    private EditText etTime, etTitleCourse, etDate, etNameClassroom;
    private int hour, minute;
    private LessonService lessonService;
    private CourseService courseService;
    private ClassroomService classroomService;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        etTime = findViewById(R.id.etTime);
        etTitleCourse = findViewById(R.id.etTitleCourse);
        etDate = findViewById(R.id.etDate);
        etNameClassroom = findViewById(R.id.etNameClassroom);
        btnTime = findViewById(R.id.btnTime);
        btnAddLesson = findViewById(R.id.btnAddLesson);
        btnUpdateLesson = findViewById(R.id.btnUpdateLesson);
        btnBackLesson = findViewById(R.id.btnBackLesson);
        btnTime.setOnClickListener(this::onClick);

        etDate.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(LessonActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        etDate.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });
        lessonService = new LessonService(getApplication());
        courseService = new CourseService(getApplication());
        classroomService = new ClassroomService(getApplication());

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long id = sharedPreferences.getLong("id", 0);
        long academyId = sharedPreferences.getLong("academy_id", 0);

        Log.i("academy_id", String.valueOf(academyId));
        Log.i("id", String.valueOf(id));

        //Traer información de la vista anterior
        Intent viewLessonActivity = getIntent();
        etNameClassroom.setText(viewLessonActivity.getStringExtra("name"));
        etTitleCourse.setText(viewLessonActivity.getStringExtra("title"));
        etDate.setText(viewLessonActivity.getStringExtra("lessonDate"));
        etTime.setText(viewLessonActivity.getStringExtra("lessonHour"));
        long lessonId = viewLessonActivity.getLongExtra("id", 0);
        Log.i("idLesson", String.valueOf(lessonId));

        //bloqueo botón add para que no funcione si viene de pulsar una lesson en la listview
        if(lessonId!=0){
            btnAddLesson.setEnabled(false);
        }

        btnAddLesson.setOnClickListener(v -> {

            // Obtener los datos ingresados por el usuario
            String nameClassroom = etNameClassroom.getText().toString().trim();
            String titleCourse = etTitleCourse.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();


            // Verificar si algún campo está vacío
            if (checkEmpty(nameClassroom, titleCourse, date, time)) {
                // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                Toast.makeText(LessonActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {

                    // Verificar si el nombre de la clase existe en la base de datos
                    Classroom existingClassroom = classroomService.getClassroomByNameAndAcademyId(nameClassroom, academyId);
                    Log.i("idClassroom", String.valueOf(existingClassroom.getId()));
                    if (existingClassroom == null) {
                        runOnUiThread(() -> {
                            Toast.makeText(LessonActivity.this, "Classroom does not exist", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    // Verificar si el título del curso existe en la base de datos
                    Course existingCourse = courseService.getCourseByTitle(titleCourse, academyId);
                    Log.i("idCourse", String.valueOf(existingCourse.getId()));
                    if (existingCourse == null) {
                        runOnUiThread(() -> {
                            Toast.makeText(LessonActivity.this, "Course does not exist", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    // Continuar con el registro si la verificación es exitosa
                    Lesson l = new Lesson();
                    l.setClassroomId(existingClassroom.getId());
                    l.setCourseId(existingCourse.getId());
                    l.setLessonDate(LocalDate.parse(etDate.getText().toString()));
                    l.setLessonHour(LocalTime.parse(etTime.getText().toString()));

                    long idLesson = lessonService.insertLesson(l);
                    Log.i("id lesson", String.valueOf(idLesson));

                    runOnUiThread(() -> {
                        Toast.makeText(LessonActivity.this, "Lesson Inserted Successfully", Toast.LENGTH_SHORT).show();
                    });

                    //Cuando se registre vuelve a la vista del menú
                    Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                    startActivity(viewMenuIntent);
                    finish();
                });

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    Log.i("error", e.getMessage());
                }
            }
        });

        btnUpdateLesson.setOnClickListener(v -> {

            // Obtener los datos ingresados por el usuario
            String nameClassroom = etNameClassroom.getText().toString().trim();
            String titleCourse = etTitleCourse.getText().toString().trim();
            String dateUpd = etDate.getText().toString().trim();
            String timeUpd = etTime.getText().toString().trim();


            // Verificar si algún campo está vacío
            if (checkEmpty(nameClassroom, titleCourse, dateUpd, timeUpd)) {
                // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                Toast.makeText(LessonActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {

                    // Verificar si el nombre de la clase existe en la base de datos
                    Classroom existingClassroomUpd = classroomService.getClassroomByNameAndAcademyId(nameClassroom, academyId);
                    Log.i("idClassroom", String.valueOf(existingClassroomUpd.getId()));
                    if (existingClassroomUpd == null) {
                        runOnUiThread(() -> {
                            Toast.makeText(LessonActivity.this, "Classroom does not exist", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    // Verificar si el título del curso existe en la base de datos
                    Course existingCourseUpd = courseService.getCourseByTitle(titleCourse, academyId);
                    Log.i("idCourse", String.valueOf(existingCourseUpd.getId()));
                    if (existingCourseUpd == null) {
                        runOnUiThread(() -> {
                            Toast.makeText(LessonActivity.this, "Course does not exist", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    Log.i("dateUpd", String.valueOf(dateUpd));
                    Log.i("timeUpd", String.valueOf(timeUpd));

                    // Continuar con el registro si la verificación es exitosa
                    Lesson l2 = new Lesson();
                    l2.setClassroomId(existingClassroomUpd.getId());
                    l2.setCourseId(existingCourseUpd.getId());
                    l2.setLessonDate(LocalDate.parse(dateUpd));
                    l2.setLessonHour(LocalTime.parse(timeUpd));
                    l2.setId(lessonId);

                    Log.i("LessonUpdated", l2.toString());

                    lessonService.updateLesson(l2);
                    Log.i("id lesson", String.valueOf(lessonId));

                    runOnUiThread(() -> {
                        Toast.makeText(LessonActivity.this, "Lesson Updated Successfully", Toast.LENGTH_SHORT).show();
                    });

                    //Cuando se registre vuelve a la vista del menú
                    Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                    startActivity(viewMenuIntent);
                    finish();
                });

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    Log.i("error", e.getMessage());
                }
            }
        });

        btnBackLesson.setOnClickListener(v -> {

            //Cuando se registre vuelve a la vista del menú
            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
            startActivity(viewMenuIntent);
            finish();

        });


    }

    public void onClick(View v) {

        if (v == btnTime) {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // Agregar un "0" delante de la hora si es menor que 10
                    String formattedHour = (hourOfDay < 10) ? "0" + hourOfDay : String.valueOf(hourOfDay);
                    // Agregar un "0" delante de los minutos si son menores que 10
                    String formattedMinute = (minute < 10) ? "0" + minute : String.valueOf(minute);
                    // Establecer el texto en el EditText
                    etTime.setText(formattedHour + ":" + formattedMinute);
                }
            }, hour, minute, true);
            timePickerDialog.show();
        }
    }

    private boolean checkEmpty(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                return true; // Devuelve true si al menos un campo está vacío
            }
        }
        return false; // Devuelve false si todos los campos están llenos
    }

}