package es.iescarrillo.android.iacademy3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Manager;
import es.iescarrillo.android.iacademy3.models.UserAccount;
import es.iescarrillo.android.iacademy3.services.ManagerService;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.services.TeacherService;

public class ManagerRegisterActivity extends AppCompatActivity {
    private EditText etNameManager, etSurnameManager, etEmailManager,
            etDniManager, etPhoneManager, etUsernameManager, etPasswordManager;
    private Button btnRegister, btnBack;


    private ManagerService managerService;
    private StudentService studentService;
    private TeacherService teacherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_manager);

        etNameManager = findViewById(R.id.etNameManager);
        etSurnameManager = findViewById(R.id.etSurnameManager);
        etEmailManager = findViewById(R.id.etEmailManager);
        etDniManager = findViewById(R.id.etDniManager);
        etPhoneManager = findViewById(R.id.etPhoneManager);
        etUsernameManager = findViewById(R.id.etUsernameManager);
        etPasswordManager = findViewById(R.id.etPasswordManager);
        btnRegister = findViewById(R.id.btnRegisterManager);
        btnBack = findViewById(R.id.btnBackManager);


        managerService = new ManagerService(getApplication());
        studentService = new StudentService(getApplication());
        teacherService = new TeacherService(getApplication());

        btnRegister.setOnClickListener(v -> {
            //registrar manager

            // Obtener los datos ingresados por el usuario
            String nameManager = etNameManager.getText().toString().trim();
            String surnameManager = etSurnameManager.getText().toString().trim();
            String emailManager = etEmailManager.getText().toString().trim();
            String dniManger = etDniManager.getText().toString().trim();
            String phoneManager = etPhoneManager.getText().toString().trim();
            String usernameManager = etUsernameManager.getText().toString().trim();
            String passwordManager = etPasswordManager.getText().toString().trim();

            // Validar que se hayan completado todos los campos
            if (nameManager.isEmpty() || surnameManager.isEmpty() || emailManager.isEmpty() ||
                    dniManger.isEmpty() || phoneManager.isEmpty() || usernameManager.isEmpty() || passwordManager.isEmpty()) {
                Toast.makeText(ManagerRegisterActivity.this, "Completa los campos", Toast.LENGTH_SHORT).show();
            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {

                    // Obtener el nombre de usuario
                    String username = etUsernameManager.getText().toString();

                    // Comprobar si el userName existe en la db
                    if (studentService.getStudentByUsername(username) != null ||
                            teacherService.getTeacherByUsername(username) != null ||
                            managerService.getManagerByUsername(username) != null) {
                        runOnUiThread(() -> {
                            Toast.makeText(ManagerRegisterActivity.this, "Username in use", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // Continuar con el registro si el nombre de usuario no existe

                        Manager m = new Manager();
                        m.setName(etNameManager.getText().toString());
                        m.setSurname(etSurnameManager.getText().toString());
                        m.setEmail(etEmailManager.getText().toString());
                        m.setDni(etDniManager.getText().toString());
                        m.setPhone(etPhoneManager.getText().toString());
                        m.setRol("Manager");

                        UserAccount u = new UserAccount();
                        u.setUsername(username);

                        String encryptPassword = BCrypt.hashpw(etPasswordManager.getText().toString(), BCrypt.gensalt(5));
                        u.setPassword(encryptPassword);

                        m.setUserAccount(u);

                        long idManager = managerService.insertManager(m);
                        Log.i("id manager", String.valueOf(idManager));

                        runOnUiThread(() -> {
                            Toast.makeText(ManagerRegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                        });

                        //Cuando se registre vuelve a la vista del login
                        Intent viewMainIntent = new Intent(this, MainActivity.class);
                        startActivity(viewMainIntent);
                    }

                });

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    Log.i("error", e.getMessage());
                }

            }

        });

        btnBack.setOnClickListener(v -> {

            Intent viewMainIntent = new Intent(this, MainActivity.class);
            startActivity(viewMainIntent);

        });


    }
}