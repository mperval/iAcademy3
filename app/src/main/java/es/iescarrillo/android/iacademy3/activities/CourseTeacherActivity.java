package es.iescarrillo.android.iacademy3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.Calendar;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.models.Inscription;
import es.iescarrillo.android.iacademy3.models.Teacher;
import es.iescarrillo.android.iacademy3.models.UserAccount;
import es.iescarrillo.android.iacademy3.services.CourseService;
import es.iescarrillo.android.iacademy3.services.InscriptionService;

public class CourseTeacherActivity extends AppCompatActivity {

    private Button btnUpdateCourseTeacher, btnAddCourseTeacher, btnBackCourseTeacher, btnDeleteCourseTeacher;
    private EditText etTitleCourseTeacher, etDescriptionCourseTeacher, etLevelCourseTeacher, etCapacityCourseTeacher,
            etStartDateCourseTeacher, etEndDateCourseTeacher, etActivatedCourseTeacher;
    private CourseService courseService;
    private InscriptionService inscriptionService;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_teacher);

        etTitleCourseTeacher = findViewById(R.id.etTitleCourseTeacher);
        etDescriptionCourseTeacher = findViewById(R.id.etDescriptionCourseTeacher);
        etLevelCourseTeacher = findViewById(R.id.etLevelCourseTeacher);
        etCapacityCourseTeacher = findViewById(R.id.etCapacityCourseTeacher);
        etStartDateCourseTeacher = findViewById(R.id.etStartDateCourseTeacher);
        etEndDateCourseTeacher = findViewById(R.id.etEndDateCourseTeacher);
        etActivatedCourseTeacher = findViewById(R.id.etActivatedCourseTeacher);

        btnAddCourseTeacher = findViewById(R.id.btnAddCourseTeacher);
        btnUpdateCourseTeacher = findViewById(R.id.btnUpdateCourseTeacher);
        btnDeleteCourseTeacher = findViewById(R.id.btnDeleteCourseTeacher);
        btnBackCourseTeacher = findViewById(R.id.btnBackCourseTeacher);

        etStartDateCourseTeacher.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(CourseTeacherActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        etStartDateCourseTeacher.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });
        etEndDateCourseTeacher.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(CourseTeacherActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        etEndDateCourseTeacher.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });


        Intent viewCourseTeacherActivityIntent = getIntent();

        courseService = new CourseService(getApplication());
        inscriptionService = new InscriptionService(getApplication());

        //Variables Globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long academyId = sharedPreferences.getLong("academy_id", 0);
        long teacherId = sharedPreferences.getLong("id", 0);

        //Traer información de la vista anterior
        long id = getIntent().getLongExtra("id", 0);
        etTitleCourseTeacher.setText(viewCourseTeacherActivityIntent.getStringExtra("title"));
        etDescriptionCourseTeacher.setText(viewCourseTeacherActivityIntent.getStringExtra("description"));
        etLevelCourseTeacher.setText(viewCourseTeacherActivityIntent.getStringExtra("level"));
        etCapacityCourseTeacher.setText(viewCourseTeacherActivityIntent.getStringExtra("capacity"));
        etStartDateCourseTeacher.setText(viewCourseTeacherActivityIntent.getStringExtra("startDate"));
        etEndDateCourseTeacher.setText(viewCourseTeacherActivityIntent.getStringExtra("endDate"));
        etActivatedCourseTeacher.setText(viewCourseTeacherActivityIntent.getStringExtra("activated"));

        Log.i("id course", String.valueOf(id));


        btnAddCourseTeacher.setOnClickListener(v -> {

            if (id != 0) {

                Toast.makeText(CourseTeacherActivity.this, "Disabled button", Toast.LENGTH_LONG).show();

                btnAddCourseTeacher.setEnabled(false);

            } else {

                // Obtener los datos ingresados por el usuario
                String title = etTitleCourseTeacher.getText().toString().trim();
                String description = etDescriptionCourseTeacher.getText().toString().trim();
                String level = etLevelCourseTeacher.getText().toString().trim();
                String capacity = etCapacityCourseTeacher.getText().toString().trim();
                String startDate = etStartDateCourseTeacher.getText().toString().trim();
                String endDate = etEndDateCourseTeacher.getText().toString().trim();
                String activated = etActivatedCourseTeacher.getText().toString().trim();


                // Verificar si algún campo está vacío
                if (checkEmpty(title, description, level, capacity, startDate, endDate, activated)) {
                    Toast.makeText(CourseTeacherActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();

                } else if (!activated.equalsIgnoreCase("true") && !activated.equalsIgnoreCase("false")) {

                    Toast.makeText(CourseTeacherActivity.this, "Invalid Arguments in the Activated box", Toast.LENGTH_SHORT).show();

                } else if (LocalDate.parse(startDate).isEqual(LocalDate.parse(endDate)) || LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {

                    Toast.makeText(CourseTeacherActivity.this, "Invalid Dates", Toast.LENGTH_SHORT).show();

                } else {

                    // Realizar operaciones en segundo plano
                    Thread thread = new Thread(() -> {

                        Course c = new Course();

                        c.setTitle(title);
                        c.setDescription(description);
                        c.setLevel(level);
                        c.setCapacity(Integer.parseInt(capacity));
                        c.setStartDate(LocalDate.parse(startDate));
                        c.setEndDate(LocalDate.parse(endDate));
                        c.setTeacherId(teacherId);
                        c.setAcademyId(academyId);

                        if (activated.equalsIgnoreCase("true")) {

                            c.setActivated(Boolean.TRUE);

                        } else if (activated.equalsIgnoreCase("false")) {

                            c.setActivated(Boolean.FALSE);

                        } else {

                        }

                        long idCourse = courseService.insertCourse(c);

                        Log.i("id course", "Course added successfully" + idCourse);

                        runOnUiThread(() -> {
                            Toast.makeText(CourseTeacherActivity.this, "Course Registered", Toast.LENGTH_SHORT).show();
                        });

                        //Cuando se registre vuelve a la vista del menu
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
            }

        });

        btnUpdateCourseTeacher.setOnClickListener(v -> {

            if (id == 0) {
                btnUpdateCourseTeacher.setEnabled(false);
                Toast.makeText(CourseTeacherActivity.this, "No course to update", Toast.LENGTH_SHORT).show();

            } else {

                // Obtener los datos ingresados por el usuario
                String title = etTitleCourseTeacher.getText().toString().trim();
                String description = etDescriptionCourseTeacher.getText().toString().trim();
                String level = etLevelCourseTeacher.getText().toString().trim();
                String capacity = etCapacityCourseTeacher.getText().toString().trim();
                String startDate = etStartDateCourseTeacher.getText().toString().trim();
                String endDate = etEndDateCourseTeacher.getText().toString().trim();
                String activated = etActivatedCourseTeacher.getText().toString().trim();

                // Verificar si algún campo está vacío
                if (checkEmpty(title, description, level, capacity, startDate, endDate, activated)) {
                    Toast.makeText(CourseTeacherActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();

                } else if (!activated.equalsIgnoreCase("true") && !activated.equalsIgnoreCase("false")) {

                    Toast.makeText(CourseTeacherActivity.this, "Invalid Arguments in the Activated box", Toast.LENGTH_SHORT).show();

                } else if (LocalDate.parse(startDate).isEqual(LocalDate.parse(endDate)) || LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {

                    Toast.makeText(CourseTeacherActivity.this, "Invalid Dates", Toast.LENGTH_SHORT).show();

                } else {

                    // Realizar operaciones en segundo plano
                    Thread thread = new Thread(() -> {

                        int inscriptions = inscriptionService.getNumberOfStudentsInCourse(id);
                        Log.i("Alumnos matriculados", String.valueOf(inscriptions));

                        if (inscriptions > 0) {

                            runOnUiThread(() -> {
                                Toast.makeText(CourseTeacherActivity.this, "Course can´t be updated", Toast.LENGTH_SHORT).show();
                            });

                            //Vuelve a la vista del menu
                            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                            startActivity(viewMenuIntent);
                            finish();

                        } else {

                            Course c = new Course();

                            c.setId(id);
                            c.setTitle(title);
                            c.setDescription(description);
                            c.setLevel(level);
                            c.setCapacity(Integer.parseInt(capacity));
                            c.setStartDate(LocalDate.parse(startDate));
                            c.setEndDate(LocalDate.parse(endDate));
                            c.setTeacherId(teacherId);
                            c.setAcademyId(academyId);

                            if (activated.equalsIgnoreCase("true")) {

                                c.setActivated(Boolean.TRUE);

                            } else if (activated.equalsIgnoreCase("false")) {

                                c.setActivated(Boolean.FALSE);

                            } else {
                                Toast.makeText(CourseTeacherActivity.this, "Invalid Arguments in the Activated box", Toast.LENGTH_SHORT).show();
                            }

                            courseService.updateCourse(c);

                            Log.i("id course", "Course updated successfully");

                            runOnUiThread(() -> {
                                Toast.makeText(CourseTeacherActivity.this, "Course Updated", Toast.LENGTH_SHORT).show();
                            });

                            //Vuelve a la vista del menu
                            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                            startActivity(viewMenuIntent);
                            finish();
                        }

                    });

                    thread.start();
                    try {
                        thread.join();
                    } catch (Exception e) {
                        Log.i("error", e.getMessage());
                    }
                }
            }
        });

        btnDeleteCourseTeacher.setOnClickListener(v -> {

            if (id == 0) {

                btnDeleteCourseTeacher.setEnabled(false);
                Toast.makeText(CourseTeacherActivity.this, "No course to delete", Toast.LENGTH_SHORT).show();


            } else {

                Thread thread = new Thread(() -> {
                    Course c = courseService.getCourseById(id);

                    int inscriptions = inscriptionService.getNumberOfStudentsInCourse(c.getId());
                    Log.i("Alumnos matriculados", String.valueOf(inscriptions));

                    if (inscriptions > 0) {

                        runOnUiThread(() -> {
                            Toast.makeText(CourseTeacherActivity.this, "Course can´t be deleted", Toast.LENGTH_SHORT).show();
                        });

                    } else {

                        if (c != null) {
                            courseService.deleteCourse(c);
                            runOnUiThread(() -> {
                                Toast.makeText(CourseTeacherActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            // Manejar la situación en la que el Teacher es nulo
                            runOnUiThread(() -> {
                                Toast.makeText(CourseTeacherActivity.this, "Course not found", Toast.LENGTH_SHORT).show();
                            });
                        }

                    }
                });

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    Log.i("error", e.getMessage());
                }

                Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                startActivity(viewMenuIntent);
                finish();
            }
        });

        btnBackCourseTeacher.setOnClickListener(v -> {

            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
            startActivity(viewMenuIntent);
            finish();

        });


    }

    // Método para verificar si algún campo está vacío
    private boolean checkEmpty(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                return true; // Devuelve true si al menos un campo está vacío
            }
        }
        return false; // Devuelve false si todos los campos están llenos
    }
}