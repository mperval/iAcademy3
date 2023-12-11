package es.iescarrillo.android.iacademy3.listView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.activities.ClassroomActivity;
import es.iescarrillo.android.iacademy3.adapters.AdapterListViewClassroom;
import es.iescarrillo.android.iacademy3.models.Classroom;
import es.iescarrillo.android.iacademy3.services.ClassroomService;

public class ListViewClassroom extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private AdapterListViewClassroom adapterListViewClassroom;
    private List<Classroom> classrooms;
    private Button btnAddClassroom;
    private ListView lvClassroom;

    private ClassroomService classroomService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_classroom);

        lvClassroom = findViewById(R.id.lvClassroom);
        btnAddClassroom = findViewById(R.id.btnAddClassroom);

        classroomService = new ClassroomService(getApplication());

        //Varibles globales
        sharedPreferences = getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE);
        long academy_id = sharedPreferences.getLong("academy_id", 0);
        String rol = sharedPreferences.getString("rol", "");

        //Forma de traernos todas las academias
        Thread thread = new Thread(() -> {

            List<Classroom> classroomList = classroomService.getAcademyClassrooms(academy_id);

            classrooms = new ArrayList<>(classroomList);

            runOnUiThread(() -> {

                adapterListViewClassroom = new AdapterListViewClassroom((Context) this, (ArrayList<Classroom>) classrooms);
                lvClassroom.setAdapter(adapterListViewClassroom);
            });
        });

        thread.start();
        try {
            thread.join();//Esperar a que termine el hilo
        } catch (Exception e) {
            Log.e("error hilo", e.getMessage());
        }

        lvClassroom.setOnItemClickListener(((parent, view, position, id) -> {

            Classroom classroom = (Classroom) parent.getItemAtPosition(position);

            Intent viewClassroomActivity = new Intent(this, ClassroomActivity.class);
            viewClassroomActivity.putExtra("name", classroom.getName());
            viewClassroomActivity.putExtra("id", classroom.getId());
            viewClassroomActivity.putExtra("capacity", String.valueOf(classroom.getCapacity()));
            startActivity(viewClassroomActivity);


        }));

        btnAddClassroom.setOnClickListener(v -> {

            if (rol.equals("Teacher")) {
                btnAddClassroom.setEnabled(false);
                Toast.makeText(ListViewClassroom.this, "Denied access", Toast.LENGTH_LONG).show();
            } else {
                Intent viewAcademyActivity = new Intent(this, ClassroomActivity.class);
                startActivity(viewAcademyActivity);
                finish();

            }
        });


    }
}