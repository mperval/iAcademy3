package es.iescarrillo.android.iacademy3.listView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.activities.MenuActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterAcademyAll;
import es.iescarrillo.android.iacademy3.models.Academy;
import es.iescarrillo.android.iacademy3.services.AcademyService;


public class ListViewStudentAcademyAll extends AppCompatActivity {

    private AdapterAcademyAll adapterAcademyAll;
    private List<Academy> academies;
    private Button btnBackAcademyAll;
    private AcademyService academyService;
    private ListView lvAcademyAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_student_academy_all);

        lvAcademyAll=findViewById(R.id.lvAllAcademies);
        btnBackAcademyAll=findViewById(R.id.btnBackAllAcademies);

        academyService=new AcademyService(getApplication());

        //Forma de traernos todas las academias
        Thread thread = new Thread(() -> {

            List<Academy> academyList = academyService.getAll();

            academies = new ArrayList<>(academyList);

            runOnUiThread(() -> {

                adapterAcademyAll = new AdapterAcademyAll((Context) this, (ArrayList<Academy>) academies);
                lvAcademyAll.setAdapter(adapterAcademyAll);
            });
        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        lvAcademyAll.setOnItemClickListener(((parent, view, position, id) -> {
            Academy academy = (Academy) parent.getItemAtPosition(position);

            Intent viewCourseAcademy = new Intent(this, ListViewStudentCourseAcademy.class);

            viewCourseAcademy.putExtra("id", academy.getId());
            viewCourseAcademy.putExtra("name", academy.getName());

            startActivity(viewCourseAcademy);
            finish();

        }));

        btnBackAcademyAll.setOnClickListener(v -> {
            Intent viewAcademyActivity = new Intent(this, MenuActivity.class);
            startActivity(viewAcademyActivity);
            finish();
        });


    }
}