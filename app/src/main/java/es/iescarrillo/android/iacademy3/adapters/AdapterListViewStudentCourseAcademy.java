package es.iescarrillo.android.iacademy3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.Course;
import es.iescarrillo.android.iacademy3.models.Teacher;

public class AdapterListViewStudentCourseAcademy extends ArrayAdapter<Course> {
        private ArrayList<Course> courses;
        private Context context;

        public AdapterListViewStudentCourseAcademy(Context context, ArrayList<Course> course) {

            super(context, 0, course);
            this.context = context;
            this.courses = courses;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Course course = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_template, parent, false);
            }
            TextView tvTemplate = convertView.findViewById(R.id.tvTemplate);

            // Asegúrate de que course no sea nulo antes de intentar acceder a sus propiedades
            if (course != null) {
                tvTemplate.setText(course.getTitle());
            }


            return convertView;
        }
    }
