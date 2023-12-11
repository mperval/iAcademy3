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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.adapters.AdapterTeacherSpinner;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.models.Teacher;
import es.iescarrillo.android.iacademy3.services.AcademyService;
import es.iescarrillo.android.iacademy3.services.CourseService;
import es.iescarrillo.android.iacademy3.services.InscriptionService;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.services.TeacherService;

//clase que muestra, inserta, borra y edita el curso.
public class CourseManagerActivity extends AppCompatActivity {
    private StudentService ss;
    private TeacherService ts;
    private InscriptionService is;
    private CourseService cs;
    private AcademyService as;
    private long idTeacher;
    private Button btnUpdateCourse, btnDeleteCourse, btnAddCourse, btnBackCourse;
    private EditText etTitleCourse, etDescriptionCourse, etLevelCourse, etCapacityCourse, etStartDateCourse, etEndDateCourse, etActivatedCourse;
    private Spinner spinnerTeachers;
    private AdapterTeacherSpinner adapterTeacherSpinner;

    // Declarar un CountDownLatch como variable de instancia
    private CountDownLatch adapterInitializedLatch = new CountDownLatch(1);

    private List<Teacher> teachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_course);

        btnUpdateCourse = findViewById(R.id.btnUpdateCourse);
        btnDeleteCourse = findViewById(R.id.btnDeleteCourse);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnBackCourse = findViewById(R.id.btnBackCourse);

        as = new AcademyService(getApplication());
        ss = new StudentService(getApplication());
        ts = new TeacherService(getApplication());
        cs = new CourseService(getApplication());
        is = new InscriptionService(getApplication());

        etTitleCourse = findViewById(R.id.etTitleCourse);
        etDescriptionCourse = findViewById(R.id.etDescriptionCourse);
        etLevelCourse = findViewById(R.id.etLevelCourse);
        etCapacityCourse = findViewById(R.id.etCapacityCourse);
        etStartDateCourse = findViewById(R.id.etStartDateCourse);
        etEndDateCourse = findViewById(R.id.etEndDateCourse);
        etActivatedCourse = findViewById(R.id.etActivatedCourse);
        spinnerTeachers = findViewById(R.id.spinnerTeachers);

        Intent intent = getIntent();
        etTitleCourse.setText(intent.getStringExtra("title"));
        etDescriptionCourse.setText(intent.getStringExtra("description"));
        etLevelCourse.setText(intent.getStringExtra("level"));
        etCapacityCourse.setText(intent.getStringExtra("capacity"));
        etStartDateCourse.setText(intent.getStringExtra("startDate"));
        etEndDateCourse.setText(intent.getStringExtra("endDate"));
        etActivatedCourse.setText(intent.getStringExtra("activated"));
        long courseId = intent.getLongExtra("id", 0);
        long teacherId = intent.getLongExtra("teacher_id", 0);
        Log.i("teacherID", String.valueOf(teacherId));

        //obtengo id de la academia.
        SharedPreferences sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long idAcademy = sharedPreferences.getLong("academy_id", 0);

        etStartDateCourse.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(CourseManagerActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        etStartDateCourse.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });
        etEndDateCourse.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(CourseManagerActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        etEndDateCourse.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });

        //Nos traemos los teachers de la academia
        new Thread(() -> {
            teachers = ts.getAcademyTeachers(idAcademy);
            runOnUiThread(() -> setupTeacherSpinner(teachers));
        }).start();

        // Para fijar el teacher en el spinner según id que traigo del anterior intent

        if (teacherId != 0) {
            new Thread(() -> {
                try {
                    // Esperar a que el adaptador esté inicializado
                    adapterInitializedLatch.await();

                    // Acceder al adaptador después de que esté inicializado
                    Teacher t = ts.getTeacherById(teacherId);

                    // Buscar el índice del profesor en la lista de profesores
                    int position = -1;
                    for (int i = 0; i < adapterTeacherSpinner.getCount(); i++) {
                        if (t.getId() == adapterTeacherSpinner.getItem(i).getId()) {
                            position = i;
                            break;
                        }
                    }

                    // Establecer la posición en el Spinner
                    if (position != -1) {
                        spinnerTeachers.setSelection(position);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }


        btnAddCourse.setOnClickListener(v -> {

            if (courseId != 0) {

                Toast.makeText(CourseManagerActivity.this, "Disabled button", Toast.LENGTH_LONG).show();

                btnAddCourse.setEnabled(false);

            } else if (teachers.size() == 0) {

                btnAddCourse.setEnabled(false);
                Toast.makeText(CourseManagerActivity.this, "Need to add teachers", Toast.LENGTH_LONG).show();

            } else {
                // Obtener los datos ingresados por el manager
                String titleCourse = etTitleCourse.getText().toString().trim();
                String descriptionCourse = etDescriptionCourse.getText().toString().trim();
                String levelCourse = etLevelCourse.getText().toString().trim();
                String capacityCourse = etCapacityCourse.getText().toString().trim();
                String startDateCourse = etStartDateCourse.getText().toString().trim();
                String endDateCourse = etEndDateCourse.getText().toString().trim();
                String activatedCourse = etActivatedCourse.getText().toString().trim();
                Teacher t = (Teacher) spinnerTeachers.getSelectedItem();
                Log.i("id_Academy", String.valueOf(idAcademy));
                Log.i("teacher_Id", String.valueOf(t.getId()));

                if (checkEmpty(titleCourse, descriptionCourse, levelCourse, capacityCourse, startDateCourse, endDateCourse, activatedCourse)) {
                    runOnUiThread(() -> Toast.makeText(CourseManagerActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show());
                } else {
                    new Thread(() -> {
                        boolean courseExists = cs.countCoursesByAcademyAndTitle(idAcademy, titleCourse);

                        runOnUiThread(() -> {
                            if (courseExists) {
                                Toast.makeText(CourseManagerActivity.this, "Course name in use", Toast.LENGTH_SHORT).show();

                            } else if (!activatedCourse.equalsIgnoreCase("true") && !activatedCourse.equalsIgnoreCase("false")) {
                                Toast.makeText(CourseManagerActivity.this, "Invalid Arguments in the Activated box", Toast.LENGTH_SHORT).show();

                            } else if (LocalDate.parse(startDateCourse).isEqual(LocalDate.parse(endDateCourse)) || LocalDate.parse(startDateCourse).isAfter(LocalDate.parse(endDateCourse))) {
                                Toast.makeText(CourseManagerActivity.this, "Invalid Dates", Toast.LENGTH_SHORT).show();


                            } else {
                                new Thread(() -> {

                                    Course course = new Course();

                                    course.setTitle(titleCourse);
                                    course.setDescription(descriptionCourse);
                                    course.setLevel(levelCourse);
                                    course.setCapacity(Integer.parseInt(capacityCourse));

                                    course.setStartDate(LocalDate.parse(startDateCourse));
                                    course.setEndDate(LocalDate.parse(endDateCourse));
                                    if (activatedCourse.equalsIgnoreCase("true")) {
                                        course.setActivated(Boolean.TRUE);
                                    } else if (activatedCourse.equalsIgnoreCase("false")) {
                                        course.setActivated(Boolean.FALSE);
                                    }

                                    course.setAcademyId(idAcademy);
                                    course.setTeacherId(t.getId());

                                    long idCourse = cs.insertCourse(course);
                                    Log.i("idCourse", String.valueOf(idCourse));

                                    runOnUiThread(() -> {
                                        Toast.makeText(CourseManagerActivity.this, "Course add", Toast.LENGTH_SHORT).show();
                                    });
                                    Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                                    startActivity(viewMenuIntent);
                                    finish();

                                }).start();
                            }
                        });
                    }).start();
                }
            }
        });

        btnUpdateCourse.setOnClickListener(v -> {

            if (courseId == 0) {
                btnUpdateCourse.setEnabled(false);
                Toast.makeText(CourseManagerActivity.this, "No course to update", Toast.LENGTH_SHORT).show();

            } else {

                // Obtener los datos de la bbdd
                String titleCourse = etTitleCourse.getText().toString().trim();
                String descriptionCourse = etDescriptionCourse.getText().toString().trim();
                String levelCourse = etLevelCourse.getText().toString().trim();
                String capacityCourse = etCapacityCourse.getText().toString().trim();
                String startDateCourse = etStartDateCourse.getText().toString().trim();
                String endDateCourse = etEndDateCourse.getText().toString().trim();
                String activatedCourse = etActivatedCourse.getText().toString().trim();
                Teacher t = (Teacher) spinnerTeachers.getSelectedItem();
                Log.i("idAcademy", String.valueOf(idAcademy));

                if (checkEmpty(titleCourse, descriptionCourse, levelCourse, capacityCourse, startDateCourse, endDateCourse, activatedCourse)) {
                    runOnUiThread(() -> Toast.makeText(CourseManagerActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show());

                } else if (!activatedCourse.equalsIgnoreCase("true") && !activatedCourse.equalsIgnoreCase("false")) {

                    Toast.makeText(CourseManagerActivity.this, "Invalid Arguments in the Activated box", Toast.LENGTH_SHORT).show();

                } else if (LocalDate.parse(startDateCourse).isEqual(LocalDate.parse(endDateCourse)) || LocalDate.parse(startDateCourse).isAfter(LocalDate.parse(endDateCourse))) {

                    Toast.makeText(CourseManagerActivity.this, "Invalid Dates", Toast.LENGTH_SHORT).show();

                } else {

                    // Realizar operaciones en segundo plano
                    Thread thread = new Thread(() -> {

                        int inscriptions = is.getNumberOfStudentsInCourse(courseId);
                        Log.i("Alumnos matriculados", String.valueOf(inscriptions));

                        if (inscriptions > 0) {

                            runOnUiThread(() -> {
                                Toast.makeText(CourseManagerActivity.this, "Course can´t be updated", Toast.LENGTH_SHORT).show();
                            });

                            //Vuelve a la vista del menu
                            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                            startActivity(viewMenuIntent);
                            finish();

                        } else {

                            Course course = new Course();

                            course.setTitle(titleCourse);
                            course.setDescription(descriptionCourse);
                            course.setLevel(levelCourse);
                            course.setCapacity(Integer.parseInt(capacityCourse));

                            course.setStartDate(LocalDate.parse(startDateCourse));
                            course.setEndDate(LocalDate.parse(endDateCourse));

                            if (activatedCourse.equalsIgnoreCase("true")) {

                                course.setActivated(Boolean.TRUE);
                            } else if (activatedCourse.equalsIgnoreCase("false")) {

                                course.setActivated(Boolean.FALSE);

                            } else {
                                Toast.makeText(CourseManagerActivity.this, "Invalid Arguments in the Activated box", Toast.LENGTH_SHORT).show();
                            }

                            course.setId(courseId);
                            course.setAcademyId(idAcademy);
                            course.setTeacherId(t.getId());

                            cs.updateCourse(course);

                            runOnUiThread(() -> {
                                Toast.makeText(CourseManagerActivity.this, "Course Updated", Toast.LENGTH_SHORT).show();
                            });
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

        btnDeleteCourse.setOnClickListener(v -> {

            if (courseId == 0) {

                btnDeleteCourse.setEnabled(false);
                Toast.makeText(CourseManagerActivity.this, "No course to delete", Toast.LENGTH_SHORT).show();


            } else {

                Thread thread = new Thread(() -> {
                    Course c = cs.getCourseById(courseId);

                    int inscriptions = is.getNumberOfStudentsInCourse(c.getId());
                    Log.i("Alumnos matriculados", String.valueOf(inscriptions));

                    if (inscriptions > 0) {

                        runOnUiThread(() -> {
                            Toast.makeText(CourseManagerActivity.this, "Course can´t be deleted", Toast.LENGTH_SHORT).show();
                        });

                    } else {

                        if (c != null) {
                            cs.deleteCourse(c);
                            runOnUiThread(() -> {
                                Toast.makeText(CourseManagerActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            // Manejar la situación en la que el Teacher es nulo
                            runOnUiThread(() -> {
                                Toast.makeText(CourseManagerActivity.this, "Course not found", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
                thread.start();

                Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                startActivity(viewMenuIntent);
                finish();
            }
        });

        btnBackCourse.setOnClickListener(v -> {
            Intent viewCourseManagerActivity = new Intent(this, MenuActivity.class);
            startActivity(viewCourseManagerActivity);
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

    private void setupTeacherSpinner(List<Teacher> teachers) {
        adapterTeacherSpinner = new AdapterTeacherSpinner(this, android.R.layout.simple_spinner_item, teachers);
        adapterTeacherSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeachers.setAdapter(adapterTeacherSpinner);

        // Indicar que el adaptador ha sido inicializado
        adapterInitializedLatch.countDown();
    }
}