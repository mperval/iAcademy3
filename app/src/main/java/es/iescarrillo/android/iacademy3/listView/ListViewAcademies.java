package es.iescarrillo.android.iacademy3.listView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.activities.AcademyActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterAcademyAll;
import es.iescarrillo.android.iacademy3.models.Academy;
import es.iescarrillo.android.iacademy3.services.AcademyService;

public class ListViewAcademies extends AppCompatActivity {

    /*
    NO ESTÁ EN USO, SUSCEPTIBLE DE SER BORRADA
     */

    private SharedPreferences sharedPreferences;
    private AdapterAcademyAll adapterAcademyAll;
    private List<Academy> academies;
    private Button btnAddAcademy;
    private ListView lvAcademyManager;

    private AcademyService academyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_academy);

        //Varibles globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        String rol = sharedPreferences.getString("rol", "");
        long id2 = sharedPreferences.getLong("id", 0);
        Boolean login = sharedPreferences.getBoolean("login", false);
        Log.i(" rol: ", rol);
        Log.i(" id: ", String.valueOf(id2));
        Log.i(" login: ", String.valueOf(login));

        lvAcademyManager = findViewById(R.id.lvAcademyManager);
        btnAddAcademy = findViewById(R.id.btnAddAcademy);

        academyService = new AcademyService(getApplication());


        //Forma de traernos todas las academias
        Thread thread = new Thread(() -> {

            List<Academy> academyList = academyService.getAll();

            academies = new ArrayList<>(academyList);

            runOnUiThread(() -> {

                adapterAcademyAll = new AdapterAcademyAll((Context) this, (ArrayList<Academy>) academies);
                lvAcademyManager.setAdapter(adapterAcademyAll);
            });
        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        lvAcademyManager.setOnItemClickListener(((parent, view, position, id) -> {
            Academy academy = (Academy) parent.getItemAtPosition(position);

            Intent viewAcademyActivity = new Intent(this, AcademyActivity.class);
            viewAcademyActivity.putExtra("id", academy.getId());
            viewAcademyActivity.putExtra("name", academy.getName());
            viewAcademyActivity.putExtra("description", academy.getDescription());
            viewAcademyActivity.putExtra("country", academy.getCountry());
            viewAcademyActivity.putExtra("state", academy.getState());
            viewAcademyActivity.putExtra("city", academy.getCity());
            viewAcademyActivity.putExtra("address", academy.getAddress());
            viewAcademyActivity.putExtra("web", academy.getWeb());
            viewAcademyActivity.putExtra("email", academy.getEmail());
            viewAcademyActivity.putExtra("phone", academy.getPhone());

            startActivity(viewAcademyActivity);

        }));

        btnAddAcademy.setOnClickListener(v -> {
            Intent viewAcademyActivity = new Intent(this, AcademyActivity.class);
            startActivity(viewAcademyActivity);
            finish();
        });

    }
}