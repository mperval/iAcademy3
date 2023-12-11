package es.iescarrillo.android.iacademy3.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.services.AcademyService;
import es.iescarrillo.android.iacademy3.services.InscriptionService;
import es.iescarrillo.android.iacademy3.services.ManagerService;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.services.TeacherService;

public class MainActivity extends AppCompatActivity {
    private ManagerService managerService;
    private StudentService studentService;
    private TeacherService teacherService;
    private AcademyService academyService;
    private InscriptionService inscriptionService;
    private EditText etEnterUser, etEnterPasswordMain;
    private Button btnSignUp, btnLogin;

    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        managerService = new ManagerService(getApplication());
        studentService = new StudentService(getApplication());
        teacherService = new TeacherService(getApplication());
        academyService = new AcademyService(getApplication());
        inscriptionService = new InscriptionService(getApplication());

        etEnterUser = findViewById(R.id.etEnterUser);
        etEnterPasswordMain = findViewById(R.id.etEnterPasswordMain);
        btnLogin = findViewById(R.id.btnLogInMain);
        btnSignUp = findViewById(R.id.btnSignUpMain);


        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);

            startActivity(intent);
        });


        btnLogin.setOnClickListener(v -> {

            if (etEnterUser.getText().toString().isEmpty() || etEnterPasswordMain.getText().toString().isEmpty()) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
                });
            } else {
                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {
                    // Obtener el nombre de usuario y la contraseña
                    String username = etEnterUser.getText().toString();
                    String password = etEnterPasswordMain.getText().toString();

                    if (studentService.getStudentByUsername(username) != null) {

                        if (BCrypt.checkpw(password,
                                studentService.getStudentByUsername(username).getUserAccount().getPassword())) {

                            // Actualizar las variables de sesión
                            sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor =  sharedPreferences.edit();
                            editor.putBoolean("login", true);
                            editor.putLong("id", studentService.getStudentByUsername(username).getId());
                            editor.putString("rol", studentService.getStudentByUsername(username).getRol());
                            editor.apply();

                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Correct Login", Toast.LENGTH_SHORT).show();
                                Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                                startActivity(viewMenuIntent);
                            });

                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            });
                        }

                    } else if (managerService.getManagerByUsername(username) != null) {

                        if (BCrypt.checkpw(password,
                                managerService.getManagerByUsername(username).getUserAccount().getPassword())) {

                            sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("login", true);
                            editor.putLong("id", managerService.getManagerByUsername(username).getId());
                            editor.putString("rol", managerService.getManagerByUsername(username).getRol());
                            editor.apply();

                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Correct Login", Toast.LENGTH_SHORT).show();
                                Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                                startActivity(viewMenuIntent);
                            });

                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            });
                        }

                    } else if (teacherService.getTeacherByUsername(username) != null) {

                        if (BCrypt.checkpw(password,
                                teacherService.getTeacherByUsername(username).getUserAccount().getPassword())) {

                            sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("login", true);
                            editor.putLong("id", teacherService.getTeacherByUsername(username).getId());
                            editor.putString("rol", teacherService.getTeacherByUsername(username).getRol());
                            editor.apply();

                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Correct Login", Toast.LENGTH_SHORT).show();
                                Intent viewMenuIntent = new Intent(this, MenuActivity.class);
                                startActivity(viewMenuIntent);
                            });

                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            });
                        }

                    } else {
                        runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Username doesn´t exist", Toast.LENGTH_SHORT).show();
                        });

                    }

                });

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    Log.i("error", e.getMessage());
                }

                etEnterUser.setText("");
                etEnterPasswordMain.setText("");
            }
        });
    }
}