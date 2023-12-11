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
import es.iescarrillo.android.iacademy3.models.LessonsOrdered;

public class AdapterLessonsOrdered extends ArrayAdapter<LessonsOrdered> {

    private Context context;
    private ArrayList<LessonsOrdered> lessonsOrdered;

    public AdapterLessonsOrdered(Context context, ArrayList<LessonsOrdered> lessonsOrdered1) {
        super(context, 0, lessonsOrdered1);
        this.context = context;
        this.lessonsOrdered = lessonsOrdered1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LessonsOrdered lessonsOrdered = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_template_lessons_ordered, parent, false);
        }

        TextView tvDateHour = convertView.findViewById(R.id.tvDateHour);
        TextView tvClassroom = convertView.findViewById(R.id.tvClassroom);

        Log.i(" DateTime: ", String.valueOf(lessonsOrdered.getLessonDate()) + String.valueOf(lessonsOrdered.getLessonHour()));
        Log.i(" Classroom: ", lessonsOrdered.getClassroomName());

        if (lessonsOrdered != null) {
            tvDateHour.setText("DateTime: " +lessonsOrdered.getLessonDate()+" / "+lessonsOrdered.getLessonHour());
            tvClassroom.setText("Classroom: " +lessonsOrdered.getClassroomName()+ " Course: "+lessonsOrdered.getTitle());
        }

        return convertView;
    }
}
