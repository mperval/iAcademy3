package es.iescarrillo.android.iacademy3.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.Calendar;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Student;
import es.iescarrillo.android.iacademy3.models.UserAccount;
import es.iescarrillo.android.iacademy3.services.ManagerService;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.services.TeacherService;

public class StudentRegisterActivity extends AppCompatActivity {

    private EditText etNameStudent, etSurnameStudent, etEmailStudent, etDniStudent, etPhoneStudent,
            etFamilyPhoneStudent, etBirthDateStudent, etUsernameStudent, etPasswordStudent;
    private Button btnRegisterStudent, btnBackStudent;

    private StudentService studentService;
    private ManagerService managerService;
    private TeacherService teacherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        etNameStudent = findViewById(R.id.etNameStudent);
        etSurnameStudent = findViewById(R.id.etSurnameStudent);
        etEmailStudent = findViewById(R.id.etEmailStudent);
        etDniStudent = findViewById(R.id.etDniStudent);
        etPhoneStudent = findViewById(R.id.etPhoneStudent);
        etFamilyPhoneStudent = findViewById(R.id.etFamilyPhoneStudent);
        etBirthDateStudent = findViewById(R.id.etBirthDateStudent);
        etUsernameStudent = findViewById(R.id.etUsernameStudent);
        etPasswordStudent = findViewById(R.id.etPasswordStudent);
        btnRegisterStudent = findViewById(R.id.btnRegisterStudent);
        btnBackStudent = findViewById(R.id.btnBackStudent);

        etBirthDateStudent.setOnClickListener(view -> {
            // Obtengo la fecha actual para mostrarla por defecto en el DatePickerDialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Creo un DatePickerDialog para seleccionar la fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(StudentRegisterActivity.this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Formateo del día y mes para asegurarse de que tengan dos dígitos
                        String formattedMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                        String formattedDay = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        // Maneja la selección de la fecha
                        String selectedDate = year1 + "-" + formattedMonth + "-" + formattedDay;
                        etBirthDateStudent.setText(selectedDate); // Establezco la fecha seleccionada en el EditText
                    }, year, month, day);

            // Muestro el DatePickerDialog
            datePickerDialog.show();
        });

        studentService = new StudentService((getApplication()));
        managerService = new ManagerService(getApplication());
        teacherService = new TeacherService(getApplication());

        btnRegisterStudent.setOnClickListener(v -> {

            if (etNameStudent.getText().toString().trim().isEmpty() || etPhoneStudent.getText().toString().trim().isEmpty() ||
                    etEmailStudent.getText().toString().trim().isEmpty() || etNameStudent.getText().toString().trim().isEmpty() ||
                    etSurnameStudent.getText().toString().trim().isEmpty() || etFamilyPhoneStudent.getText().toString().trim().isEmpty()
                    || etBirthDateStudent.getText().toString().trim().isEmpty() || etUsernameStudent.getText().toString().trim().isEmpty() ||
                    etPasswordStudent.getText().toString().trim().isEmpty()){

                Toast.makeText(StudentRegisterActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();


            } else {

                // Realizar operaciones en segundo plano
                Thread thread = new Thread(() -> {
                    // Obtener el nombre de usuario
                    String username = etUsernameStudent.getText().toString();

                    // Comprobar si el userName existe en la db
                    if (studentService.getStudentByUsername(username) != null ||
                            teacherService.getTeacherByUsername(username) != null ||
                            managerService.getManagerByUsername(username) != null) {
                        runOnUiThread(() -> {
                            Toast.makeText(StudentRegisterActivity.this, "Username in use", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // Continuar con el registro si el nombre de usuario no existe

                        Student student = new Student();
                        student.setDni(etDniStudent.getText().toString());
                        student.setPhone(etPhoneStudent.getText().toString());
                        student.setEmail(etEmailStudent.getText().toString());
                        student.setName(etNameStudent.getText().toString());
                        student.setSurname(etSurnameStudent.getText().toString());
                        student.setFamilyPhone(etFamilyPhoneStudent.getText().toString());
                        student.setBirthday(LocalDate.parse(etBirthDateStudent.getText().toString()));
                        student.setRol("Student");

                        UserAccount userAccount = new UserAccount();
                        userAccount.setUsername(username);

                        String encryptPassword = BCrypt.hashpw(etPasswordStudent.getText().toString(), BCrypt.gensalt(5));
                        userAccount.setPassword(encryptPassword);

                        student.setUserAccount(userAccount);

                        long idStudent = studentService.insertStudent(student);
                        Log.i("id persona", String.valueOf(idStudent));

                        runOnUiThread(() -> {
                            Toast.makeText(StudentRegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                        });

                        //Cuando se registre vuelve a la vista del login
                        Intent viewMainIntent = new Intent(this, MainActivity.class);
                        startActivity(viewMainIntent);
                    }
                });

                thread.start();
                try{
                    thread.join();
                } catch (Exception e){
                    Log.i("error", e.getMessage());
                }
            }
        });

        btnBackStudent.setOnClickListener(v -> {
            Intent viewMainIntent = new Intent(this, MainActivity.class);
            startActivity(viewMainIntent);
        });

    }
}