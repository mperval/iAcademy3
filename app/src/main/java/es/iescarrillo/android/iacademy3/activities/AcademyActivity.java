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

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Academy;
import es.iescarrillo.android.iacademy3.services.AcademyService;

public class AcademyActivity extends AppCompatActivity {

    EditText etNameAcademy, etDescriptionAcademy, etCountryAcademy,
            etStateAcademy, etCityAcademy, etAddressAcademy, etWebAcademy,
            etEmailAcademy, etPhoneAcademy;
    Button btnBackAcademy, btnAddAcademy, btnUpdateAcademy;
    private AcademyService academyService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy);

        etNameAcademy = findViewById(R.id.etNameAcademy);
        etDescriptionAcademy = findViewById(R.id.etDescriptionAcademy);
        etCountryAcademy = findViewById(R.id.etCountryAcademy);
        etStateAcademy = findViewById(R.id.etStateAcademy);
        etCityAcademy = findViewById(R.id.etCityAcademy);
        etAddressAcademy = findViewById(R.id.etAddressAcademy);
        etWebAcademy = findViewById(R.id.etWebAcademy);
        etEmailAcademy = findViewById(R.id.etEmailAcademy);
        etPhoneAcademy = findViewById(R.id.etPhoneAcademy);

        Intent viewAcademyActivityIntent = getIntent();

        btnBackAcademy = findViewById(R.id.btnBackAcademy);
        btnAddAcademy = findViewById(R.id.btnAddAcademy);
        btnUpdateAcademy = findViewById(R.id.btnUpdateAcademy);

        academyService = new AcademyService(getApplication());

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        Boolean login = sharedPreferences.getBoolean("login", false);
        long manager_id = sharedPreferences.getLong("id", 0);
        long academy_id = sharedPreferences.getLong("academy_id", 0);
        Log.i(" login: ", String.valueOf(login));
        Log.i(" manager_id: ", String.valueOf(manager_id));
        Log.i("academy_id: ", String.valueOf(academy_id));

        //Si existe academía cargamos sus datos
        new Thread(() -> {
            Academy academy = academyService.getAcademyManagerById(manager_id);

            if (academy != null) {

            runOnUiThread(() -> {
                etNameAcademy.setText(academy.getName());
                etPhoneAcademy.setText(academy.getPhone());
                etEmailAcademy.setText(academy.getPhone());
                etWebAcademy.setText(academy.getWeb());
                etAddressAcademy.setText(academy.getAddress());
                etCityAcademy.setText(academy.getCity());
                etStateAcademy.setText(academy.getState());
                etCountryAcademy.setText(academy.getCountry());
                etDescriptionAcademy.setText(academy.getDescription());
            });

            }

        }).start();

        btnAddAcademy.setOnClickListener(v -> {

            if (academy_id != 0) {

                Toast.makeText(AcademyActivity.this, "Disabled button", Toast.LENGTH_LONG).show();

                btnAddAcademy.setEnabled(false);

            } else {

                // Obtener los datos ingresados por el usuario
                String nameAcademy = etNameAcademy.getText().toString().trim();
                String descriptionAcademy = etDescriptionAcademy.getText().toString().trim();
                String countryAcademy = etCountryAcademy.getText().toString().trim();
                String stateAcademy = etStateAcademy.getText().toString().trim();
                String cityAcademy = etCityAcademy.getText().toString().trim();
                String addressAcademy = etAddressAcademy.getText().toString().trim();
                String webAcademy = etWebAcademy.getText().toString().trim();
                String emailAcademy = etEmailAcademy.getText().toString().trim();
                String phoneAcademy = etPhoneAcademy.getText().toString().trim();

                // Verificar si algún campo está vacío
                if (checkEmpty(nameAcademy, descriptionAcademy, countryAcademy, stateAcademy, cityAcademy, addressAcademy, webAcademy, emailAcademy, phoneAcademy)) {
                    // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                    Toast.makeText(AcademyActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
                } else {

                    // Realizar operaciones en segundo plano
                    Thread thread = new Thread(() -> {

                        // Obtener el nombre de usuario
                        String name = nameAcademy;

                        // Comprobar si el userName existe en la db
                        if (academyService.getNameAcademy(name) != null) {
                            runOnUiThread(() -> {
                                Toast.makeText(AcademyActivity.this, "Academy name in use", Toast.LENGTH_SHORT).show();
                            });

                        } else {
                            // Continuar con el registro si el nombre de usuario no existe

                            Academy a = new Academy();
                            a.setName(etNameAcademy.getText().toString());
                            a.setDescription(etDescriptionAcademy.getText().toString());
                            a.setCountry(etCountryAcademy.getText().toString());
                            a.setState(etStateAcademy.getText().toString());
                            a.setCity(etCityAcademy.getText().toString());
                            a.setAddress(etAddressAcademy.getText().toString());
                            a.setWeb(etWebAcademy.getText().toString());
                            a.setEmail(etEmailAcademy.getText().toString());
                            a.setPhone(etPhoneAcademy.getText().toString());

                            a.setManagerId(manager_id);

                            long id_Academy = academyService.insertAcademy(a);
                            Log.i("id academy", String.valueOf(id_Academy));

                            runOnUiThread(() -> {
                                Toast.makeText(AcademyActivity.this, "Academy Registered", Toast.LENGTH_SHORT).show();
                            });

                            //Cuando se registre vuelve a la vista del login
                            Intent viewMainIntent = new Intent(this, MenuActivity.class);
                            startActivity(viewMainIntent);
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


        btnUpdateAcademy.setOnClickListener(v -> {

            // Obtener los datos ingresados por el usuario
            String nameAcademy = etNameAcademy.getText().toString().trim();
            String descriptionAcademy = etDescriptionAcademy.getText().toString().trim();
            String countryAcademy = etCountryAcademy.getText().toString().trim();
            String stateAcademy = etStateAcademy.getText().toString().trim();
            String cityAcademy = etCityAcademy.getText().toString().trim();
            String addressAcademy = etAddressAcademy.getText().toString().trim();
            String webAcademy = etWebAcademy.getText().toString().trim();
            String emailAcademy = etEmailAcademy.getText().toString().trim();
            String phoneAcademy = etPhoneAcademy.getText().toString().trim();

            // Verificar si algún campo está vacío
            if (checkEmpty(nameAcademy, descriptionAcademy, countryAcademy, stateAcademy, cityAcademy, addressAcademy, webAcademy, emailAcademy, phoneAcademy)) {
                // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                Toast.makeText(AcademyActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {

                    Academy a2 = new Academy();
                    a2.setId(academy_id);
                    a2.setName(nameAcademy);
                    a2.setDescription(descriptionAcademy);
                    a2.setCountry(countryAcademy);
                    a2.setState(stateAcademy);
                    a2.setCity(cityAcademy);
                    a2.setAddress(addressAcademy);
                    a2.setWeb(webAcademy);
                    a2.setEmail(emailAcademy);
                    a2.setPhone(phoneAcademy);
                    a2.setManagerId(manager_id);

                    academyService.updateAcademy(a2);
                    Log.i("id academy", "Academy updated successfully");

                    runOnUiThread(() -> {
                        Toast.makeText(AcademyActivity.this, "Academy Updated", Toast.LENGTH_SHORT).show();
                    });

                    //Cuando se registre vuelve a la vista del menu
                    Intent viewMainIntent = new Intent(this, MenuActivity.class);
                    startActivity(viewMainIntent);
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


        btnBackAcademy.setOnClickListener(v -> {

            //Cuando se registre vuelve a la vista del menu
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