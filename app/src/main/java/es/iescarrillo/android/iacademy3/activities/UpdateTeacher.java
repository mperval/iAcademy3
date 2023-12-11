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
import es.iescarrillo.android.iacademy3.models.Teacher;
import es.iescarrillo.android.iacademy3.models.UserAccount;
import es.iescarrillo.android.iacademy3.services.ManagerService;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.services.TeacherService;

public class UpdateTeacher extends AppCompatActivity {

    private EditText etNameTeacherUpdate, etSurnameTeacherUpdate, etEmailTeacherUpdate,
            etDniTeacherUpdate, etPhoneTeacherUpdate, etAddressTeacherUpdate;
    private Button btnUpdateTeacherUpdate, btnBackTeacherUpdate;
    private TeacherService teacherService;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        etNameTeacherUpdate = findViewById(R.id.etNameTeacherUpdate);
        etSurnameTeacherUpdate = findViewById(R.id.etSurnameTeacherUpdate);
        etEmailTeacherUpdate = findViewById(R.id.etEmailTeacherUpdate);
        etDniTeacherUpdate = findViewById(R.id.etDniTeacherUpdate);
        etPhoneTeacherUpdate = findViewById(R.id.etPhoneTeacherUpdate);
        etAddressTeacherUpdate = findViewById(R.id.etAddressTeacherUpdate);

        btnBackTeacherUpdate = findViewById(R.id.btnBackTeacherUpdate);
        btnUpdateTeacherUpdate = findViewById(R.id.btnUpdateTeacherUpdate);

        teacherService = new TeacherService(getApplication());

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long teacher_id = sharedPreferences.getLong("id", 0);

        //He tenido que hacer esto para que teacher sea visible desde el hilo del update
        final Teacher[] teacher = new Teacher[1];
        new Thread(() -> {
            teacher[0] = teacherService.getTeacherById(teacher_id);

            runOnUiThread(() -> {
                etNameTeacherUpdate.setText(teacher[0].getName());
                etSurnameTeacherUpdate.setText(teacher[0].getSurname());
                etDniTeacherUpdate.setText(teacher[0].getDni());
                etEmailTeacherUpdate.setText(teacher[0].getEmail());
                etAddressTeacherUpdate.setText(teacher[0].getAddress());
                etPhoneTeacherUpdate.setText(teacher[0].getPhone());
            });
        }).start();

        btnUpdateTeacherUpdate.setOnClickListener(v -> {

            // Obtener los datos ingresados por el usuario
            String nameTeacher = etNameTeacherUpdate.getText().toString().trim();
            String surnameTeacher = etSurnameTeacherUpdate.getText().toString().trim();
            String emailTeacher = etEmailTeacherUpdate.getText().toString().trim();
            String dniTeacher = etDniTeacherUpdate.getText().toString().trim();
            String phoneTeacher = etPhoneTeacherUpdate.getText().toString().trim();
            String addressTeacher = etAddressTeacherUpdate.getText().toString().trim();


            // Verificar si algún campo está vacío
            if (checkEmpty(nameTeacher, surnameTeacher, emailTeacher, dniTeacher, phoneTeacher, addressTeacher)) {
                // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                Toast.makeText(UpdateTeacher.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {

                        Teacher t2 = new Teacher();
                        t2.setId(teacher_id);
                        t2.setName(nameTeacher);
                        t2.setSurname(surnameTeacher);
                        t2.setEmail(emailTeacher);
                        t2.setDni(dniTeacher);
                        t2.setPhone(phoneTeacher);
                        t2.setAddress(addressTeacher);
                        t2.setRol("Teacher");
                        t2.setAcademyId(teacher[0].getAcademyId()); // Utiliza la variable final
                        UserAccount u = new UserAccount();
                        u.setUsername(teacher[0].getUserAccount().getUsername());
                        u.setPassword(teacher[0].getUserAccount().getPassword()); // Utiliza la variable final
                        t2.setUserAccount(u);

                        teacherService.updateTeacher(t2);

                        runOnUiThread(() -> {
                            Toast.makeText(UpdateTeacher.this, "Teacher Updated", Toast.LENGTH_SHORT).show();
                        });

                        //Cuando se actualice vuelve a la vista del menu
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

        btnBackTeacherUpdate.setOnClickListener(v -> {


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