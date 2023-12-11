package es.iescarrillo.android.iacademy3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Academy;
import es.iescarrillo.android.iacademy3.models.Teacher;
import es.iescarrillo.android.iacademy3.models.UserAccount;
import es.iescarrillo.android.iacademy3.services.ManagerService;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.services.TeacherService;

public class TeacherActivity extends AppCompatActivity {

    private EditText etNameTeacher, etSurnameTeacher, etEmailTeacher,
            etDniTeacher, etPhoneTeacher, etAddressTeacher, etUsernameTeacher, etPasswordTeacher;

    private Button btnAddTeacher, btnDeleteTeacher, btnBackTeacher;

    private TeacherService teacherService;
    private StudentService studentService;
    private ManagerService managerService;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        etNameTeacher = findViewById(R.id.etNameTeacher);
        etSurnameTeacher = findViewById(R.id.etSurnameTeacher);
        etEmailTeacher = findViewById(R.id.etEmailTeacher);
        etDniTeacher = findViewById(R.id.etDniTeacher);
        etPhoneTeacher = findViewById(R.id.etPhoneTeacher);
        etAddressTeacher = findViewById(R.id.etAddressTeacher);
        etUsernameTeacher = findViewById(R.id.etUsernameTeacher);
        etPasswordTeacher = findViewById(R.id.etPasswordTeacher);

        btnAddTeacher = findViewById(R.id.btnAddTeacher);
        btnBackTeacher = findViewById(R.id.btnBackTeacher);
        btnDeleteTeacher = findViewById(R.id.btnDeleteTeacher);

        Intent viewTeacherActivityIntent = getIntent();

        teacherService = new TeacherService(getApplication());
        studentService = new StudentService(getApplication());
        managerService = new ManagerService(getApplication());

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long academy_id = sharedPreferences.getLong("academy_id", 0);

        //Traer información de la vista anterior
        long id = viewTeacherActivityIntent.getLongExtra("id", 0);
        etNameTeacher.setText(viewTeacherActivityIntent.getStringExtra("name"));
        etSurnameTeacher.setText(viewTeacherActivityIntent.getStringExtra("surname"));
        etDniTeacher.setText(viewTeacherActivityIntent.getStringExtra("dni"));
        etEmailTeacher.setText(viewTeacherActivityIntent.getStringExtra("email"));
        etAddressTeacher.setText(viewTeacherActivityIntent.getStringExtra("address"));
        etPhoneTeacher.setText(viewTeacherActivityIntent.getStringExtra("phone"));
        etUsernameTeacher.setText(viewTeacherActivityIntent.getStringExtra("username"));


        btnAddTeacher.setOnClickListener(v -> {

            if (id != 0) {

                Toast.makeText(TeacherActivity.this, "Disabled button", Toast.LENGTH_LONG).show();

                btnAddTeacher.setEnabled(false);

            } else {


                // Obtener los datos ingresados por el usuario
                String nameTeacher = etNameTeacher.getText().toString().trim();
                String surnameTeacher = etSurnameTeacher.getText().toString().trim();
                String emailTeacher = etEmailTeacher.getText().toString().trim();
                String dniTeacher = etDniTeacher.getText().toString().trim();
                String phoneTeacher = etPhoneTeacher.getText().toString().trim();
                String addressTeacher = etAddressTeacher.getText().toString().trim();
                String usernameTeacher = etUsernameTeacher.getText().toString().trim();
                String passwordTeacher = etPasswordTeacher.getText().toString().trim();

                // Validar que se hayan completado todos los campos
                if (nameTeacher.isEmpty() || surnameTeacher.isEmpty() || emailTeacher.isEmpty() ||
                        dniTeacher.isEmpty() || phoneTeacher.isEmpty() || addressTeacher.isEmpty() || usernameTeacher.isEmpty() || passwordTeacher.isEmpty()) {
                    Toast.makeText(TeacherActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();

                } else {

                    // Realizar operaciones en segundo plano
                    Thread thread = new Thread(() -> {
                        // Obtener el nombre de usuario
                        String username = etUsernameTeacher.getText().toString();

                        // Comprobar si el userName existe en la db
                        if (studentService.getStudentByUsername(username) != null ||
                                teacherService.getTeacherByUsername(username) != null ||
                                managerService.getManagerByUsername(username) != null) {
                            runOnUiThread(() -> {
                                Toast.makeText(TeacherActivity.this, "Username in use", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            // Continuar con el registro si el nombre de usuario no existe

                            Teacher t = new Teacher();
                            t.setName(etNameTeacher.getText().toString());
                            t.setSurname(etSurnameTeacher.getText().toString());
                            t.setEmail(etEmailTeacher.getText().toString());
                            t.setDni(etDniTeacher.getText().toString());
                            t.setPhone(etPhoneTeacher.getText().toString());
                            t.setAddress(etAddressTeacher.getText().toString());
                            t.setRol("Teacher");
                            t.setAcademyId(academy_id);

                            UserAccount u = new UserAccount();
                            u.setUsername(etUsernameTeacher.getText().toString());

                            String encryptPassword = BCrypt.hashpw(etPasswordTeacher.getText().toString(), BCrypt.gensalt(5));
                            u.setPassword(encryptPassword);

                            t.setUserAccount(u);

                            long idTeacher = teacherService.insertTeacher(t);
                            Log.i("id teacher", String.valueOf(idTeacher));

                            runOnUiThread(() -> {
                                Toast.makeText(TeacherActivity.this, "Teacher Registered", Toast.LENGTH_SHORT).show();
                            });

                            //Cuando se registre vuelve a la vista del login
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


        btnDeleteTeacher.setOnClickListener(v -> {

            Thread thread = new Thread(() -> {
                Teacher t3 = teacherService.getTeacherById(id);

                if (t3 != null) {
                    teacherService.deleteTeacher(t3);
                    runOnUiThread(() -> {
                        Toast.makeText(TeacherActivity.this, "Teacher Deleted", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Manejar la situación en la que el Teacher es nulo
                    runOnUiThread(() -> {
                        Toast.makeText(TeacherActivity.this, "Teacher not found", Toast.LENGTH_SHORT).show();
                    });
                }
            });

            thread.start();

            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
            startActivity(viewMenuIntent);
            finish();

        });

        btnBackTeacher.setOnClickListener(v -> {

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