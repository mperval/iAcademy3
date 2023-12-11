package es.iescarrillo.android.iacademy3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.CourseAndAcademy;

public class AdapterStudentInscription extends ArrayAdapter<CourseAndAcademy> {

    private Context context;
    private ArrayList<CourseAndAcademy> courseAndAcademyList;

    public AdapterStudentInscription(Context context, ArrayList<CourseAndAcademy> courseAndAcademies) {
        super(context, 0, courseAndAcademies);
        this.context = context;
        this.courseAndAcademyList = courseAndAcademies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseAndAcademy courseAndAcademy = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_view_student_inscription_template, parent, false);
        }

        TextView tvTemplateNameCourse = convertView.findViewById(R.id.tvTemplateNameCourse);
        TextView tvTemplateNameAcademy = convertView.findViewById(R.id.tvTemplateNameAcademy);

        Log.i(" Name Academy: ", courseAndAcademy.getName());
        Log.i(" Title Course: ", courseAndAcademy.getTitle());

        if (courseAndAcademy != null) {
            tvTemplateNameCourse.setText("Course: " +courseAndAcademy.getTitle());
            tvTemplateNameAcademy.setText("Academy: " +courseAndAcademy.getName());
        }

        return convertView;
    }

}
