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
import es.iescarrillo.android.iacademy3.models.Classroom;
import es.iescarrillo.android.iacademy3.services.ClassroomService;

public class ClassroomActivity extends AppCompatActivity {
    private EditText etNameClassroom, etCapacityClassroom;
    private Button btnUpdateClassroom, btnAddClassroom, btnBackClassroom, btnDeleteClassroom;

    private ClassroomService classroomService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        etNameClassroom = findViewById(R.id.etNameClassroom);
        etCapacityClassroom = findViewById(R.id.etCapacityClassroom);
        btnUpdateClassroom = findViewById(R.id.btnUpdateClassroom);
        btnAddClassroom = findViewById(R.id.btnAddClassroom);
        btnBackClassroom = findViewById(R.id.btnBackClassroom);
        btnDeleteClassroom = findViewById(R.id.btnDeleteClassroom);

        classroomService = new ClassroomService(getApplication());

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long academy_id = sharedPreferences.getLong("academy_id", 0);
        String rol = sharedPreferences.getString("rol", "");

        if(rol.equals("Teacher")){
            btnAddClassroom.setEnabled(false);
            btnDeleteClassroom.setEnabled(false);
            btnUpdateClassroom.setEnabled(false);
        }

        Intent viewClassroomActivity = getIntent();

        //Traer información de la vista anterior
        etNameClassroom.setText(viewClassroomActivity.getStringExtra("name"));
        etCapacityClassroom.setText(viewClassroomActivity.getStringExtra("capacity"));
        long id = viewClassroomActivity.getLongExtra("id", 0);

        btnAddClassroom.setOnClickListener(v -> {

            if (id != 0) {

                Toast.makeText(ClassroomActivity.this, "Disabled button", Toast.LENGTH_LONG).show();

                btnAddClassroom.setEnabled(false);

            } else {

                // Obtener los datos ingresados por el usuario
                String nameClassroom = etNameClassroom.getText().toString().trim();
                String capacityClassroom = etCapacityClassroom.getText().toString().trim();

                // Verificar si algún campo está vacío
                if (checkEmpty(nameClassroom, capacityClassroom)) {
                    // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                    Toast.makeText(ClassroomActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
                } else {

                    // Realizar operaciones en segundo plano
                    Thread thread = new Thread(() -> {

                        // Comprobar si el nombre existe en la misma academia
                        if(classroomService.getClassroomByNameAndAcademyId(nameClassroom, academy_id)!=null){
                            runOnUiThread(() -> {
                                Toast.makeText(ClassroomActivity.this, "Classroom name in use", Toast.LENGTH_SHORT).show();
                            });
                        } else {

                            Classroom c = new Classroom();
                            c.setName(etNameClassroom.getText().toString());
                            c.setCapacity(Integer.valueOf(etCapacityClassroom.getText().toString()));
                            c.setAcademyId(academy_id);

                            long idClassroom = classroomService.insertClassroom(c);
                            Log.i("id classroom", String.valueOf(idClassroom));
                            Log.i("name", nameClassroom);
                            Log.i("capacityClassroom", capacityClassroom);
                            runOnUiThread(() -> {
                                Toast.makeText(ClassroomActivity.this, "Classroom Registered", Toast.LENGTH_SHORT).show();
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

        btnUpdateClassroom.setOnClickListener(v -> {

            // Obtener los datos ingresados por el usuario
            String nameClassroom = etNameClassroom.getText().toString().trim();
            String capacityClassroom = etCapacityClassroom.getText().toString().trim();

            // Verificar si algún campo está vacío
            if (checkEmpty(nameClassroom, capacityClassroom)) {
                // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                Toast.makeText(ClassroomActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {

                    // Comprobar si el name existe en la misma academia
                    if (!nameClassroom.equals(viewClassroomActivity.getStringExtra("name")) && classroomService.getClassroomByNameAndAcademyId(nameClassroom, academy_id)!= null) {
                        runOnUiThread(() -> {
                            Toast.makeText(ClassroomActivity.this, "Classroom name in use", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // Continuar con el registro si el nombre de usuario no existe

                        Classroom c = new Classroom();
                        c.setName(etNameClassroom.getText().toString());
                        c.setCapacity(Integer.valueOf(etCapacityClassroom.getText().toString()));
                        c.setId(id);
                        c.setAcademyId(academy_id);


                        classroomService.updateClassroom(c);
                        Log.i("id classroom", "Classroom updated successfully");

                        runOnUiThread(() -> {
                            Toast.makeText(ClassroomActivity.this, "Classroom Updated", Toast.LENGTH_SHORT).show();
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

        });


        btnDeleteClassroom.setOnClickListener(v -> {

            Thread thread = new Thread(() -> {
                Classroom c = classroomService.getClassroomById(id);

                if (c != null) {
                    classroomService.deleteClassroom(c);
                    runOnUiThread(() -> {
                        Toast.makeText(ClassroomActivity.this, "Classroom Deleted", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Manejar la situación en la que el Classroom es nulo
                    runOnUiThread(() -> {
                        Toast.makeText(ClassroomActivity.this, "Classroom not found", Toast.LENGTH_SHORT).show();
                    });
                }
            });

            thread.start();

            //Cuando se registre vuelve a la vista del login
            Intent viewMenuIntent = new Intent(this, MenuActivity.class);
            startActivity(viewMenuIntent);
            finish();

        });

        btnBackClassroom.setOnClickListener(v -> {
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