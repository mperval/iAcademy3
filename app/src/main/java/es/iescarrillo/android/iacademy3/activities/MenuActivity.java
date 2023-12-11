package es.iescarrillo.android.iacademy3.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.listView.ListViewClassroom;
import es.iescarrillo.android.iacademy3.listView.ListViewCourseManager;
import es.iescarrillo.android.iacademy3.listView.ListViewCourseTeacher;
import es.iescarrillo.android.iacademy3.listView.ListViewLesson;
import es.iescarrillo.android.iacademy3.listView.ListViewStudentAcademyAll;
import es.iescarrillo.android.iacademy3.listView.ListViewStudentCourseAll;
import es.iescarrillo.android.iacademy3.listView.ListViewStudentInscription;
import es.iescarrillo.android.iacademy3.listView.ListViewTeacherAcademy;
import es.iescarrillo.android.iacademy3.listView.ListViewTeachersStudents;
import es.iescarrillo.android.iacademy3.models.Academy;
import es.iescarrillo.android.iacademy3.services.AcademyService;
import es.iescarrillo.android.iacademy3.services.ClassroomService;
import es.iescarrillo.android.iacademy3.services.CourseService;
import es.iescarrillo.android.iacademy3.services.ManagerService;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.services.TeacherService;


public class MenuActivity extends AppCompatActivity {

    private Button btnCourse, btnTeacher, btnAcademy, btnClassroom, btnStudent, btnLesson, btnLogOut, btnProfile, btnPasswordActivity, btnViewRegisteredCourses;
    private SharedPreferences sharedPreferences;
    private AcademyService academyService;
    private TeacherService teacherService;
    private StudentService studentService;
    private ManagerService managerService;
    private ClassroomService classroomService;
    private CourseService courseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnCourse = findViewById(R.id.btnCourseMenuActivity);
        btnTeacher = findViewById(R.id.btnTeacherMenuActivity);
        btnAcademy = findViewById(R.id.btnAcademyMenuActivity);
        btnClassroom = findViewById(R.id.btnClassroomMenuActivity);
        btnStudent = findViewById(R.id.btnStudentMenuActivity);
        btnLesson = findViewById(R.id.btnLessonMenuActivity);
        btnLogOut = findViewById(R.id.btnLogOutMenuActivity);
        btnPasswordActivity = findViewById(R.id.btnPasswordActivity);
        btnProfile = findViewById(R.id.btnProfileMenuActivity);
        btnViewRegisteredCourses = findViewById(R.id.btnViewRegisteredCourses);

        academyService = new AcademyService(getApplication());
        teacherService = new TeacherService(getApplication());
        studentService = new StudentService(getApplication());
        managerService = new ManagerService(getApplication());
        classroomService = new ClassroomService(getApplication());
        courseService = new CourseService(getApplication());


        //Varibles globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        String rol = sharedPreferences.getString("rol", "");
        long id = sharedPreferences.getLong("id", 0);
        Boolean login = sharedPreferences.getBoolean("login", false);
        Log.i(" rol: ", rol);
        Log.i(" id: ", String.valueOf(id));
        Log.i(" login: ", String.valueOf(login));

        //Variable global para conseguir informacion
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Comprobación si existe alguna academia insertada. Sin academia insertada no puede hacerse nada
        Thread thread = new Thread(() -> {

            if (rol.equals("Manager") && academyService.getAcademyManagerById(id) == null) {

                runOnUiThread(() -> {
                    btnTeacher.setEnabled(false);
                    btnClassroom.setEnabled(false);
                    btnLesson.setEnabled(false);
                    btnCourse.setEnabled(false);

                    Toast.makeText(MenuActivity.this, "There is no academy registered", Toast.LENGTH_LONG).show();
                });
            }

        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }


        //Comprobamos que no haya cursos con capacidad completa y que puedan seguir inscribiéndose alumnos
        new Thread(() -> {
            try {
                courseService.updateAllCoursesActivation();
                // Puedes agregar lógica adicional después de la actualización aquí si es necesario
            } catch (Exception e) {
                Log.e("MenuActivity", "Error updating courses activation: " + e.getMessage());
            }
        }).start();


        btnCourse.setOnClickListener(v -> {

            if (rol.equals("Student")) {

                Intent viewListViewStudentCourseAll = new Intent(MenuActivity.this, ListViewStudentCourseAll.class);
                startActivity(viewListViewStudentCourseAll);
                finish();

            } else if (rol.equals("Teacher")) {

                // Ejecutar en un hilo secundario
                new Thread(() -> {

                    long academyTeacherId = teacherService.getAcadamyIdByTeacherId(id);
                    Log.i(" academyId: ", String.valueOf(academyTeacherId));

                    // Ejecutar en el hilo principal para actualizar la UI
                    runOnUiThread(() -> {
                        // Manejar el resultado aquí
                        if (academyTeacherId != 0) {
                            editor.putLong("academy_id", academyTeacherId);
                            editor.apply();
                            Intent viewListViewCourseTeacher = new Intent(MenuActivity.this, ListViewCourseTeacher.class);
                            startActivity(viewListViewCourseTeacher);
                            finish();
                        } else {
                            // Manejar el caso en el que academyManagerId es 0 o cualquier valor predeterminado
                            Toast.makeText(MenuActivity.this, "Error obtaining Academy Manager Id", Toast.LENGTH_LONG).show();
                        }
                    });
                }).start();
            } else { //Manager
                // Ejecutar en un hilo secundario
                new Thread(() -> {

                    long academyManagerId = academyService.getAcademyManagerById(id).getId();

                    // Ejecutar en el hilo principal para actualizar la UI
                    runOnUiThread(() -> {
                        // Manejar el resultado aquí
                        if (academyManagerId != 0) {
                            editor.putLong("academy_id", academyManagerId);
                            editor.apply();
                            Intent viewListViewCourse = new Intent(MenuActivity.this, ListViewCourseManager.class);
                            startActivity(viewListViewCourse);
                            finish();
                        } else {
                            // Manejar el caso en el que academyManagerId es 0 o cualquier valor predeterminado
                            Toast.makeText(MenuActivity.this, "Error obtaining Academy Manager Id", Toast.LENGTH_LONG).show();
                        }
                    });
                }).start();
            }

        });

        //boton lesson
        btnLesson.setOnClickListener(v -> {

            if (rol.equals("Student")) {

                Toast.makeText(MenuActivity.this, "Denied access", Toast.LENGTH_LONG).show();

                btnLesson.setEnabled(false);

            } else if (rol.equals("Teacher")) {

                // Ejecutar en un hilo secundario
               new Thread(() -> {

                    long academyId = teacherService.getAcadamyIdByTeacherId(id);

                    // Ejecutar en el hilo principal para actualizar la UI
                    runOnUiThread(() -> {
                        // Manejar el resultado aquí
                        if (academyId != 0) {
                            editor.putLong("academy_id", academyId);
                            editor.apply();
                            Intent viewListViewLesson = new Intent(MenuActivity.this, ListViewLesson.class);
                            startActivity(viewListViewLesson);
                            finish();
                        } else {
                            // Manejar el caso en el que academyManagerId es 0 o cualquier valor predeterminado
                            Toast.makeText(MenuActivity.this, "Error obtaining Academy Manager Id", Toast.LENGTH_LONG).show();
                        }
                    });
                }).start();

            } else {

                Toast.makeText(MenuActivity.this, "Denied access", Toast.LENGTH_LONG).show();

                btnCourse.setEnabled(false);

            }

        });

        btnStudent.setOnClickListener(v -> {

            if (rol.equals("Student")) {

                Toast.makeText(MenuActivity.this, "Denied access", Toast.LENGTH_LONG).show();

                btnStudent.setEnabled(false);

            } else if (rol.equals("Teacher")) {

                Intent viewLVTeachersStudents = new Intent(this, ListViewTeachersStudents.class);

                startActivity(viewLVTeachersStudents);

                finish();

            } else {

                Toast.makeText(MenuActivity.this, "Denied access", Toast.LENGTH_LONG).show();

                btnStudent.setEnabled(false);

            }

        });

        btnAcademy.setOnClickListener(v -> {

            if (rol.equals("Student")) {

                Intent AcademyAll = new Intent(MenuActivity.this, ListViewStudentAcademyAll.class);
                startActivity(AcademyAll);
                finish();

            } else if (rol.equals("Teacher")) {

                Toast.makeText(MenuActivity.this, "Denied access", Toast.LENGTH_LONG).show();

                btnAcademy.setEnabled(false);

            } else { //Manager

                // Ejecutar en un hilo secundario
                new Thread(() -> {

                    Academy academyManager = academyService.getAcademyManagerById(id);

                    // Ejecutar en el hilo principal para actualizar la UI
                    runOnUiThread(() -> {
                        // Manejar el resultado aquí
                        if (academyManager != null && academyManager.getId()!=0) {
                            long academyManagerId = academyManager.getId();

                            Log.i("academy_id menu: ", String.valueOf(academyManagerId));

                            editor.putLong("academy_id", academyManagerId);
                            editor.apply();
                            Intent viewAcademyActivity = new Intent(MenuActivity.this, AcademyActivity.class);
                            startActivity(viewAcademyActivity);
                            finish();
                        } else {
                            // Manejar el caso en el que academyManager es null o su id es 0
                            Toast.makeText(MenuActivity.this, "No Academy Manager found. Redirecting to Insert Academy.", Toast.LENGTH_LONG).show();
                            Intent insertAcademyIntent = new Intent(MenuActivity.this, AcademyActivity.class);
                            startActivity(insertAcademyIntent);
                            finish();
                        }
                    });
                }).start();
            }

        });

        btnClassroom.setOnClickListener(v -> {

            if (rol.equals("Student")) {

                Toast.makeText(MenuActivity.this, "Denied access", Toast.LENGTH_LONG).show();

                btnClassroom.setEnabled(false);

            } else if (rol.equals("Teacher")) {

                // Ejecutar en un hilo secundario
                new Thread(() -> {

                    long academyId = teacherService.getAcadamyIdByTeacherId(id);

                    // Ejecutar en el hilo principal para actualizar la UI
                    runOnUiThread(() -> {
                        // Manejar el resultado aquí
                        if (academyId != 0) {
                            editor.putLong("academy_id", academyId);
                            editor.apply();
                            Intent viewListViewClassroom = new Intent(MenuActivity.this, ListViewClassroom.class);
                            startActivity(viewListViewClassroom);
                            finish();
                        } else {
                            // Manejar el caso en el que academyManagerId es 0 o cualquier valor predeterminado
                            Toast.makeText(MenuActivity.this, "Error obtaining Academy Id", Toast.LENGTH_LONG).show();
                        }
                    });
                }).start();

            } else {

                // Ejecutar en un hilo secundario
                new Thread(() -> {

                    long academyManagerId = academyService.getAcademyManagerById(id).getId();

                    // Ejecutar en el hilo principal para actualizar la UI
                    runOnUiThread(() -> {
                        // Manejar el resultado aquí
                        if (academyManagerId != 0) {
                            editor.putLong("academy_id", academyManagerId);
                            editor.apply();
                            Intent viewListViewClassroom = new Intent(MenuActivity.this, ListViewClassroom.class);
                            startActivity(viewListViewClassroom);
                            finish();
                        } else {
                            // Manejar el caso en el que academyManagerId es 0 o cualquier valor predeterminado
                            Toast.makeText(MenuActivity.this, "Error obtaining Academy Manager Id", Toast.LENGTH_LONG).show();
                        }
                    });
                }).start();

            }

        });

        btnTeacher.setOnClickListener(v -> {

            if (rol.equals("Student")) {

                Toast.makeText(MenuActivity.this, "Denied access", Toast.LENGTH_LONG).show();

                btnTeacher.setEnabled(false);

            } else if (rol.equals("Teacher")) {

                Toast.makeText(MenuActivity.this, "Denied access", Toast.LENGTH_LONG).show();

                btnTeacher.setEnabled(false);

            } else {

                // Ejecutar en un hilo secundario
                new Thread(() -> {

                    long academyManagerId = academyService.getAcademyManagerById(id).getId();

                    // Ejecutar en el hilo principal para actualizar la UI
                    runOnUiThread(() -> {
                        // Manejar el resultado aquí
                        if (academyManagerId != 0) {
                            editor.putLong("academy_id", academyManagerId);
                            editor.apply();
                            Intent viewListViewTeacher = new Intent(MenuActivity.this, ListViewTeacherAcademy.class);
                            startActivity(viewListViewTeacher);
                            finish();
                        } else {
                            // Manejar el caso en el que academyManagerId es 0 o cualquier valor predeterminado
                            Toast.makeText(MenuActivity.this, "Error obtaining Academy Manager Id", Toast.LENGTH_LONG).show();
                        }
                    });
                }).start();

            }

        });

        btnProfile.setOnClickListener(v -> {

            if (rol.equals("Student")) {

                Intent viewStudentUpdateActivity = new Intent(this, UpdateStudent.class);
                startActivity(viewStudentUpdateActivity);
                finish();

            } else if (rol.equals("Teacher")) {

                Intent viewTeacherUpdateActivity = new Intent(this, UpdateTeacher.class);
                startActivity(viewTeacherUpdateActivity);
                finish();

            } else { //Manager

                Intent viewManagerUpdateActivity = new Intent(this, UpdateManager.class);
                startActivity(viewManagerUpdateActivity);
                finish();

            }

        });

        btnLogOut.setOnClickListener(v -> {

            sharedPreferences.edit().clear().apply();

            sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
            String rol2 = sharedPreferences.getString("rol", "");
            long id2 = sharedPreferences.getLong("id", 0);
            Boolean login2 = sharedPreferences.getBoolean("login", false);
            Log.i(" rol: ", rol2);
            Log.i(" id: ", String.valueOf(id2));
            Log.i(" login: ", String.valueOf(login2));

            Intent viewMainActivity = new Intent(this, MainActivity.class);

            startActivity(viewMainActivity);

            finish();

        });

        btnPasswordActivity.setOnClickListener(v -> {

            Intent viewMainActivity = new Intent(this, ChangePassword.class);

            startActivity(viewMainActivity);

            finish();

        });


        if (rol.equals("Student")) {

            btnViewRegisteredCourses.setOnClickListener(v -> {

                // Ejecutar en un hilo secundario
                new Thread(() -> {

                    long studentId = studentService.getStudentById(id).getId();
                    Log.i(" studentId: ", String.valueOf(studentId));

                    // Ejecutar en el hilo principal para actualizar la UI
                    runOnUiThread(() -> {
                        // Manejar el resultado aquí
                        if (studentId != 0) {
                            editor.putLong("studentId", studentId);
                            editor.apply();
                            Intent viewStudentStudentInscription = new Intent(this, ListViewStudentInscription.class);
                            startActivity(viewStudentStudentInscription);
                            finish();
                        } else {
                            // Manejar el caso en el que academyManagerId es 0 o cualquier valor predeterminado
                            Toast.makeText(MenuActivity.this, "Error obtaining Academy Student Id", Toast.LENGTH_LONG).show();
                        }
                    });
                }).start();

            });

        } else if (rol.equals("Teacher")) {

            btnViewRegisteredCourses.setVisibility(View.INVISIBLE);

        } else { //Manager

            btnViewRegisteredCourses.setVisibility(View.INVISIBLE);

        }


    }


}




