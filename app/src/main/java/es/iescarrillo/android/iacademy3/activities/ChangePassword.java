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

import java.time.LocalDate;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Manager;
import es.iescarrillo.android.iacademy3.models.Student;
import es.iescarrillo.android.iacademy3.models.Teacher;
import es.iescarrillo.android.iacademy3.models.UserAccount;
import es.iescarrillo.android.iacademy3.services.ManagerService;
import es.iescarrillo.android.iacademy3.services.StudentService;
import es.iescarrillo.android.iacademy3.services.TeacherService;


public class ChangePassword extends AppCompatActivity {
    private Button btnBackChanger, btnChangerPassword;
    private EditText etEnterPassword, etNewPassword, etRepitePassword;

    private SharedPreferences sharedPreferences;
    private ManagerService managerService;
    private TeacherService teacherService;
    private StudentService studentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnChangerPassword = findViewById(R.id.btnChangerPassword);
        btnBackChanger = findViewById(R.id.btnBackChanger);
        etEnterPassword = findViewById(R.id.etEnterPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etRepitePassword = findViewById(R.id.etRepitPassword);

        managerService = new ManagerService(getApplication());
        teacherService = new TeacherService(getApplication());
        studentService = new StudentService(getApplication());

        btnChangerPassword.setOnClickListener(v -> {
            sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
            String rol = sharedPreferences.getString("rol", "");
            long id = sharedPreferences.getLong("id", 0);
            Log.i(" id: ", String.valueOf(id));
            Log.i(" rol: ", rol);


            // Check user role
            if (rol.equals("Student")) {

                // Retrieve entered passwords
                String enterPassword = etEnterPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String repitePassword = etRepitePassword.getText().toString().trim();

                // Check if any of the password fields is empty
                if (checkEmpty(enterPassword, newPassword, repitePassword)) {
                    Toast.makeText(ChangePassword.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
                } else {
                    Thread thread = new Thread(() -> {
                        // Retrieve the username associated with the current student
                        String username = studentService.getStudentById(id).getUserAccount().getUsername();

                        // Check if the entered password matches the stored password
                        if (BCrypt.checkpw(enterPassword, studentService.getStudentByUsername(username).getUserAccount().getPassword())) {

                            // Check if the new password and repeated password match
                            if (newPassword.equals(repitePassword)) {

                                // Create a Manager object with updated information
                                Student s = new Student();
                                s.setDni(studentService.getStudentByUsername(username).getDni().toString());
                                s.setPhone(studentService.getStudentByUsername(username).getPhone().toString());
                                s.setId(id);
                                s.setName(studentService.getStudentByUsername(username).getName().toString());

                                String birthday = studentService.getStudentByUsername(username).getBirthday().toString();
                                s.setBirthday(LocalDate.parse(birthday));
                                s.setFamilyPhone(studentService.getStudentByUsername(username).getFamilyPhone().toString());
                                s.setSurname(studentService.getStudentByUsername(username).getSurname().toString());
                                s.setEmail(studentService.getStudentByUsername(username).getEmail().toString());
                                s.setRol(rol);
                                // Create a new UserAccount with the updated password
                                UserAccount u = new UserAccount();

                                u.setUsername(username);
                                String encryptPassword = BCrypt.hashpw(etNewPassword.getText().toString(), BCrypt.gensalt(5));
                                u.setPassword(encryptPassword);

                                // Set the UserAccount for the Student
                                s.setUserAccount(u);

                                // Update the student in the database
                                studentService.updateStudent(s);
                                // Display a success message
                                Log.i("id password", "Password updated successfully");
                                runOnUiThread(() -> {
                                    Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                // Display a toast message if the new password and repeated password do not match
                                runOnUiThread(() -> {
                                    Toast.makeText(ChangePassword.this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            // Display a toast message for incorrect entered password
                            runOnUiThread(() -> {
                                Toast.makeText(ChangePassword.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            });
                        }

                    });
                    // Start the thread for password update logic
                    thread.start();
                    try {
                        thread.join();
                    } catch (Exception e) {
                        Log.i("error", e.getMessage());
                    }
                }

            } else if (rol.equals("Teacher")) {

                // Retrieve entered passwords
                String enterPassword = etEnterPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String repitePassword = etRepitePassword.getText().toString().trim();

                // Check if any of the password fields is empty
                if (checkEmpty(enterPassword, newPassword, repitePassword)) {
                    Toast.makeText(ChangePassword.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
                } else {
                    Thread thread = new Thread(() -> {
                        // Retrieve the username associated with the current Teacher
                        String username = teacherService.getTeacherById(id).getUserAccount().getUsername();

                        // Check if the entered password matches the stored password
                        if (BCrypt.checkpw(enterPassword, teacherService.getTeacherByUsername(username).getUserAccount().getPassword())) {

                            // Check if the new password and repeated password match
                            if (newPassword.equals(repitePassword)) {

                                // Create a Manager object with updated information
                                Teacher t = new Teacher();
                                t.setDni(teacherService.getTeacherByUsername(username).getDni().toString());
                                t.setAddress(teacherService.getTeacherByUsername(username).getAddress().toString());
                                t.setEmail(teacherService.getTeacherByUsername(username).getEmail().toString());
                                t.setName(teacherService.getTeacherByUsername(username).getName().toString());
                                t.setAddress(teacherService.getTeacherByUsername(username).getSurname().toString());
                                t.setSurname(teacherService.getTeacherByUsername(username).getSurname().toString());
                                t.setAcademyId(teacherService.getTeacherByUsername(username).getAcademyId());
                                t.setPhone(teacherService.getTeacherByUsername(username).getPhone().toString());
                                t.setId(id);
                                t.setRol(rol);

                                // Create a new UserAccount with the updated password
                                UserAccount u = new UserAccount();

                                u.setUsername(username);
                                String encryptPassword = BCrypt.hashpw(etNewPassword.getText().toString(), BCrypt.gensalt(5));
                                u.setPassword(encryptPassword);

                                // Set the UserAccount for the Teacher
                                t.setUserAccount(u);

                                // Update the student in the database
                                teacherService.updateTeacher(t);
                                // Display a success message
                                Log.i("id password", "Password updated successfully");
                                runOnUiThread(() -> {
                                    Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                // Display a toast message if the new password and repeated password do not match
                                runOnUiThread(() -> {
                                    Toast.makeText(ChangePassword.this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            // Display a toast message for incorrect entered password
                            runOnUiThread(() -> {
                                Toast.makeText(ChangePassword.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            });
                        }

                    });
                    // Start the thread for password update logic
                    thread.start();
                    try {
                        thread.join();
                    } catch (Exception e) {
                        Log.i("error", e.getMessage());
                    }
                }

            } else {
                // Retrieve entered passwords
                String enterPassword = etEnterPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String repitePassword = etRepitePassword.getText().toString().trim();

                // Check if any of the password fields is empty
                if (checkEmpty(enterPassword, newPassword, repitePassword)) {
                    Toast.makeText(ChangePassword.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
                } else {
                    Thread thread = new Thread(() -> {
                        // Retrieve the username associated with the current manager
                        String username = managerService.getManagerById(id).getUserAccount().getUsername();

                        // Check if the entered password matches the stored password
                        if (BCrypt.checkpw(enterPassword, managerService.getManagerByUsername(username).getUserAccount().getPassword())) {

                            // Check if the new password and repeated password match
                            if (newPassword.equals(repitePassword)) {

                                // Create a Manager object with updated information
                                Manager m = new Manager();
                                m.setDni(managerService.getManagerByUsername(username).getDni().toString());
                                m.setPhone(managerService.getManagerByUsername(username).getPhone().toString());
                                m.setEmail(managerService.getManagerByUsername(username).getEmail().toString());
                                m.setName(managerService.getManagerByUsername(username).getName().toString());
                                m.setSurname(managerService.getManagerByUsername(username).getSurname().toString());
                                m.setRol(rol);
                                m.setId(id);

                                // Create a new UserAccount with the updated password
                                UserAccount u = new UserAccount();

                                u.setUsername(username);
                                String encryptPassword = BCrypt.hashpw(etNewPassword.getText().toString(), BCrypt.gensalt(5));
                                u.setPassword(encryptPassword);

                                // Set the UserAccount for the Manager
                                m.setUserAccount(u);

                                // Update the Manager in the database
                                managerService.updateManager(m);
                                // Display a success message
                                Log.i("id password", "Password updated successfully");
                                runOnUiThread(() -> {
                                    Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                // Display a toast message if the new password and repeated password do not match
                                runOnUiThread(() -> {
                                    Toast.makeText(ChangePassword.this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            // Display a toast message for incorrect entered password
                            runOnUiThread(() -> {
                                Toast.makeText(ChangePassword.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            });
                        }

                    });
                    // Start the thread for password update logic
                    thread.start();
                    try {
                        thread.join();
                    } catch (Exception e) {
                        Log.i("error", e.getMessage());
                    }
                }
            }

            Intent viewMainActivity = new Intent(this, MenuActivity.class);
            startActivity(viewMainActivity);
            finish();
        });

        btnBackChanger.setOnClickListener(v -> {

            Intent viewMainActivity = new Intent(this, MenuActivity.class);
            startActivity(viewMainActivity);
            finish();

        });
    }

    // After password update logic, navigate to the MenuActivity
    private boolean checkEmpty(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}