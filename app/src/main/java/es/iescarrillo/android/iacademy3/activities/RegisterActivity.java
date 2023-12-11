package es.iescarrillo.android.iacademy3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.iescarrillo.android.iacademy3.R;


public class RegisterActivity extends AppCompatActivity {

    EditText etPinRegister;
    Button btnStudentRegister, btnManagerRegister;
    private String pin = "4321";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etPinRegister = findViewById(R.id.etPinRegister);


        //aqui va el boton de student
        Button btnStudentRegister = findViewById(R.id.btnStudentRegister);
        btnStudentRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentRegisterActivity.class);

            startActivity(intent);
        });

        //aqui va el boton de manager
        Button btnManagerRegister = findViewById(R.id.btnManagerRegister);
        btnManagerRegister.setOnClickListener(v -> {
            String pinRegister = etPinRegister.getText().toString().trim();

            // Validar que se hayan completado todos los campos

            if (etPinRegister.getText().toString().equals(pin)){
                Intent intent = new Intent(this, ManagerRegisterActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(RegisterActivity.this, "PIN needed", Toast.LENGTH_SHORT).show();
            }

        });






    }


}