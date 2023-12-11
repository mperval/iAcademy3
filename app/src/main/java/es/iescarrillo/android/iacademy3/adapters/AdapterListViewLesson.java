package es.iescarrillo.android.iacademy3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.iescarrillo.android.iacademy3.R;
import es.iescarrillo.android.iacademy3.models.IdCourseClassroomLessonInfo;

public class AdapterListViewLesson extends ArrayAdapter<IdCourseClassroomLessonInfo> {
        private Context context;
        private ArrayList<IdCourseClassroomLessonInfo> lessonInfoList;

        public AdapterListViewLesson(Context context, ArrayList<IdCourseClassroomLessonInfo> lessons) {
            super(context, 0, lessons);
            this.context = context;
            this.lessonInfoList = lessonInfoList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IdCourseClassroomLessonInfo lessonInfo = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_lesson_template, parent, false);
            }

            TextView tvTemplate1 = convertView.findViewById(R.id.tvTemplate1);
            TextView tvTemplate2 = convertView.findViewById(R.id.tvTemplate2);
            TextView tvTemplate3 = convertView.findViewById(R.id.tvTemplate3);

            if (lessonInfo != null) {
                tvTemplate1.setText(lessonInfo.getName());
                tvTemplate2.setText(lessonInfo.getTitle());
                tvTemplate3.setText(String.format("%s %s", lessonInfo.getLessonDate(), lessonInfo.getLessonHour()));
            }

            return convertView;
        }

}
