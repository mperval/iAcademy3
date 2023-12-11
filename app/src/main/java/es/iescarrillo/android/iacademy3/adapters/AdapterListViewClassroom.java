package es.iescarrillo.android.iacademy3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Classroom;

public class AdapterListViewClassroom extends ArrayAdapter<Classroom> {

    private Context context;
    private ArrayList<Classroom> clasrooms;
    public AdapterListViewClassroom(Context context, ArrayList<Classroom> classroom) {

        super(context, 0, classroom);
        this.context = context;
        this.clasrooms = clasrooms;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Classroom classroom = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_template, parent, false);
        }
        TextView tvTemplate = convertView.findViewById(R.id.tvTemplate);

        // Asegúrate de que academy no sea nulo antes de intentar acceder a sus propiedades
        if (classroom != null) {
            tvTemplate.setText(classroom.getName());
        }

        return convertView;
    }

}
