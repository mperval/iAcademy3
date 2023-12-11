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

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Manager;
import es.iescarrillo.android.iacademy3.models.UserAccount;
import es.iescarrillo.android.iacademy3.services.ManagerService;

public class UpdateManager extends AppCompatActivity {

    private EditText etNameManagerUpdate, etSurnameManagerUpdate, etEmailManagerUpdate,
            etDniManagerUpdate, etPhoneManagerUpdate;

    private Button btnManagerUpdate, btnBack;

    private ManagerService managerService;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_manager);

        etNameManagerUpdate = findViewById(R.id.etNameManagerUpdate);
        etSurnameManagerUpdate = findViewById(R.id.etSurnameManagerUpdate);
        etEmailManagerUpdate = findViewById(R.id.etEmailManagerUpdate);
        etDniManagerUpdate = findViewById(R.id.etDniManagerUpdate);
        etPhoneManagerUpdate = findViewById(R.id.etPhoneManagerUpdate);

        btnManagerUpdate = findViewById(R.id.btnUpdateManagerUpdate);
        btnBack = findViewById(R.id.btnBackManagerUpdate);

        managerService = new ManagerService(getApplication());

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long manager_id = sharedPreferences.getLong("id", 0);

        //He tenido que hacer esto para que teacher sea visible desde el hilo del update
        final Manager[] manager = new Manager[1];
        new Thread(() -> {
            manager[0] = managerService.getManagerById(manager_id);

            runOnUiThread(() -> {
                etNameManagerUpdate.setText(manager[0].getName());
                etSurnameManagerUpdate.setText(manager[0].getSurname());
                etDniManagerUpdate.setText(manager[0].getDni());
                etEmailManagerUpdate.setText(manager[0].getEmail());
                etPhoneManagerUpdate.setText(manager[0].getPhone());
            });
        }).start();

        btnManagerUpdate.setOnClickListener(v -> {

            Log.i("botón update", manager[0].getName());

            // Obtener los datos ingresados por el usuario
            String nameManager = etNameManagerUpdate.getText().toString().trim();
            String surnameManager = etSurnameManagerUpdate.getText().toString().trim();
            String emailManager = etEmailManagerUpdate.getText().toString().trim();
            String dniManger = etDniManagerUpdate.getText().toString().trim();
            String phoneManager = etPhoneManagerUpdate.getText().toString().trim();

            // Verificar si algún campo está vacío
            if (checkEmpty(nameManager, surnameManager, emailManager, dniManger, phoneManager)) {
                // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                Toast.makeText(UpdateManager.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {
                    Manager m1 = new Manager();
                    m1.setName(nameManager);
                    m1.setSurname(surnameManager);
                    m1.setId(manager_id);
                    m1.setDni(dniManger);
                    m1.setPhone(phoneManager);
                    m1.setEmail(emailManager);
                    m1.setRol("Manager");

                    UserAccount u = new UserAccount();
                    u.setUsername(manager[0].getUserAccount().getUsername());
                    u.setPassword(manager[0].getUserAccount().getPassword());
                    m1.setUserAccount(u);

                    managerService.updateManager(m1);

                    runOnUiThread(() -> {
                        Toast.makeText(UpdateManager.this, "Manager Updated", Toast.LENGTH_SHORT).show();
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

        btnBack.setOnClickListener(v -> {


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