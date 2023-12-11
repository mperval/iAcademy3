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

import java.time.LocalDate;
import java.util.Calendar;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Manager;
import es.iescarrillo.android.iacademy3.models.UserAccount;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.models.Student;

public class UpdateStudent extends AppCompatActivity {

    private EditText etNameStudentUpdate, etSurnameStudentUpdate, etEmailStudentUpdate, etDniStudentUpdate, etPhoneStudentUpdate,
            etFamilyPhoneStudentUpdate, etBirthDateStudentUpdate;
    private Button btnStudentUpdate, btnBackStudentUpdate;

    private StudentService studentService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        etNameStudentUpdate = findViewById(R.id.etNameStudentUpdate);
        etSurnameStudentUpdate = findViewById(R.id.etSurnameStudentUpdate);
        etEmailStudentUpdate = findViewById(R.id.etEmailStudentUpdate);
        etDniStudentUpdate = findViewById(R.id.etDniStudentUpdate);
        etPhoneStudentUpdate = findViewById(R.id.etPhoneStudentUpdate);
        etFamilyPhoneStudentUpdate = findViewById(R.id.etFamilyPhoneStudentUpdate);
        etBirthDateStudentUpdate = findViewById(R.id.etBirthDateStudentUpdate);

        etBirthDateStudentUpdate.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateStudent.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        etBirthDateStudentUpdate.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });
        btnStudentUpdate = findViewById(R.id.btnStudentUpdate);
        btnBackStudentUpdate = findViewById(R.id.btnBackStudentUpdate);

        studentService = new StudentService(getApplication());

        //Variables globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long student_id = sharedPreferences.getLong("id", 0);

        //He tenido que hacer esto para que teacher sea visible desde el hilo del update
        final Student[] student = new Student[1];
        new Thread(() -> {
            student[0] = studentService.getStudentById(student_id);

            runOnUiThread(() -> {
                etNameStudentUpdate.setText(student[0].getName());
                etSurnameStudentUpdate.setText(student[0].getSurname());
                etDniStudentUpdate.setText(student[0].getDni());
                etEmailStudentUpdate.setText(student[0].getEmail());
                etPhoneStudentUpdate.setText(student[0].getPhone());
                etFamilyPhoneStudentUpdate.setText(student[0].getFamilyPhone());
                etBirthDateStudentUpdate.setText(student[0].getBirthday().toString());
            });
        }).start();

        btnStudentUpdate.setOnClickListener(v -> {

            String nameStudent = etNameStudentUpdate.getText().toString().trim();
            String surnameStudent = etSurnameStudentUpdate.getText().toString().trim();
            String dniStudent = etDniStudentUpdate.getText().toString().trim();
            String emailStudent = etDniStudentUpdate.getText().toString().trim();
            String phoneStudent = etPhoneStudentUpdate.getText().toString().trim();
            String familyPhone = etFamilyPhoneStudentUpdate.getText().toString().trim();
            String birthDateStudent = etBirthDateStudentUpdate.getText().toString().trim();

            // Verificar si algún campo está vacío
            if (checkEmpty(nameStudent, surnameStudent, dniStudent, emailStudent, phoneStudent, familyPhone, birthDateStudent)) {
                // Al menos un campo está vacío, muestra un mensaje o realiza la acción correspondiente
                Toast.makeText(UpdateStudent.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {
                    Student st = new Student();
                    st.setName(nameStudent);
                    st.setSurname(surnameStudent);
                    st.setDni(dniStudent);
                    st.setEmail(emailStudent);
                    st.setPhone(phoneStudent);
                    st.setFamilyPhone(familyPhone);
                    st.setBirthday(LocalDate.parse(birthDateStudent));
                    st.setRol("Student");
                    st.setId(student_id);

                    UserAccount u = new UserAccount();
                    u.setUsername(student[0].getUserAccount().getUsername());
                    u.setPassword(student[0].getUserAccount().getPassword());
                    st.setUserAccount(u);

                    studentService.updateStudent(st);

                    runOnUiThread(() -> {
                        Toast.makeText(UpdateStudent.this, "Student Updated", Toast.LENGTH_SHORT).show();
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

        btnBackStudentUpdate.setOnClickListener(v -> {

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