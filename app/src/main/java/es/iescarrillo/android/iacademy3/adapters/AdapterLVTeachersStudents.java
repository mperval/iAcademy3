package es.iescarrillo.android.iacademy3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


import es.iescarrillo.android.iacademy3.R;

import es.iescarrillo.android.iacademy3.models.StudentCourseInfo;

public class AdapterLVTeachersStudents extends ArrayAdapter<StudentCourseInfo> {
    private Context context;
    private ArrayList<StudentCourseInfo> studentCourse;

    public AdapterLVTeachersStudents(Context context, ArrayList<StudentCourseInfo> studentsCourses) {
        super(context, 0, studentsCourses);
        this.context = context;
        this.studentCourse = studentCourse;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentCourseInfo studentCourseInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_template_teachers_students, parent, false);
        }

        TextView studentName = convertView.findViewById(R.id.studentNameTextView);
        TextView courseTitle = convertView.findViewById(R.id.courseTitleTextView);

        if(studentCourseInfo!=null){
            studentName.setText(studentCourseInfo.getName()+" "+studentCourseInfo.getSurname());
            courseTitle.setText(studentCourseInfo.getTitle());
        }

        return convertView;
    }


}

