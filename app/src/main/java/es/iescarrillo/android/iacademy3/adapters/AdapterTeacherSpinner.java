package es.iescarrillo.android.iacademy3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.iescarrillo.android.iacademy3.models.Teacher;

public class AdapterTeacherSpinner extends ArrayAdapter<Teacher> {
    private LayoutInflater inflater;
    private List<Teacher> teachers;

    public AdapterTeacherSpinner(Context context, int resource, List<Teacher> teachers) {
        super(context, resource, teachers);
        this.inflater = LayoutInflater.from(context);
        this.teachers = teachers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(teachers.get(position).getUserAccount().getUsername());
        return view;
    }

}

