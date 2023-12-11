package es.iescarrillo.android.iacademy3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Academy;
import es.iescarrillo.android.iacademy3.models.Teacher;

public class AdapterTeacher extends ArrayAdapter<Teacher> {

    private Context context;
    private ArrayList<Teacher> teachers;
    public AdapterTeacher(Context context, ArrayList<Teacher> teacher) {

        super(context, 0, teacher);
        this.context = context;
        this.teachers = teachers;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Teacher teacher = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_template, parent, false);
        }
        TextView tvTemplate = convertView.findViewById(R.id.tvTemplate);

        // Asegúrate de que teacher no sea nulo antes de intentar acceder a sus propiedades
        if (teacher != null) {
            tvTemplate.setText(teacher.getName());
        }

        return convertView;
    }
}
