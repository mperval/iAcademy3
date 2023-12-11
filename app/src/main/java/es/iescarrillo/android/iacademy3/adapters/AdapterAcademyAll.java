package es.iescarrillo.android.iacademy3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Academy;

public class AdapterAcademyAll extends ArrayAdapter<Academy> {
    private Context context;
    private ArrayList<Academy> academies;
    public AdapterAcademyAll(Context context, ArrayList<Academy> academy) {

        super(context, 0, academy);
        this.context = context;
        this.academies = academies;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Academy academy = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_template, parent, false);
        }
        TextView tvTemplate = convertView.findViewById(R.id.tvTemplate);

        // Asegúrate de que academy no sea nulo antes de intentar acceder a sus propiedades
        if (academy != null) {
            tvTemplate.setText(academy.getName());
        }

        return convertView;
    }
}
