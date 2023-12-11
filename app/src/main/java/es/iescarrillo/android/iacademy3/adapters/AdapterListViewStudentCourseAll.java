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
import es.iescarrillo.android.iacademy3.models.AllCourses;
import es.iescarrillo.android.iacademy3.models.Course;

public class AdapterListViewStudentCourseAll extends ArrayAdapter<AllCourses>{
    private ArrayList<AllCourses> allCourses;
    private Context context;
    public AdapterListViewStudentCourseAll (Context context, ArrayList<AllCourses> allCoursesArrayList){
        super(context, 0, allCoursesArrayList);
        this.allCourses = allCourses;
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllCourses allCourses = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_template, parent, false);
        }
        TextView tvTemplate = convertView.findViewById(R.id.tvTemplate);

        if(allCourses != null){

                tvTemplate.setText(allCourses.getName()+ ": " +allCourses.getTitle());

        }
        return convertView;
    }
    }
