package ru.sfedu.calendarsfedu;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView lessonName;
        TextView lessonteacher;
        TextView lessonbegin;
        TextView lessonend;
        TextView lessongroup;
        TextView Number_par;
        LinearLayout Linr;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            lessonName = (TextView)itemView.findViewById(R.id.lesson_name);
            lessonteacher = (TextView)itemView.findViewById(R.id.lessson_teacher);
            lessonbegin = (TextView)itemView.findViewById(R.id.lesson_begin);
            lessonend = (TextView)itemView.findViewById(R.id.lesson_end);
            Number_par = (TextView)itemView.findViewById(R.id.Number_par);
            lessongroup = (TextView)itemView.findViewById(R.id.group);
            Linr = (LinearLayout)itemView.findViewById(R.id.linerbok);
        }
    }

    List<Lesson> lessons;

    RVAdapter(List<Lesson> persons){
        this.lessons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.lessonName.setText(lessons.get(i).name);
        personViewHolder.lessonteacher.setText(lessons.get(i).teacher);
        personViewHolder.lessonbegin.setText(lessons.get(i).begin);
        personViewHolder.lessonend.setText(lessons.get(i).end);
        personViewHolder.lessongroup.setText(lessons.get(i).group);
        personViewHolder.Number_par.setText(lessons.get(i).auditorium);
        personViewHolder.cv.setLayoutParams(new RelativeLayout.LayoutParams( CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));

        if(lessons.get(i).begin.equals("")) {
            personViewHolder.lessonName.setGravity(Gravity.CENTER );
            personViewHolder.lessonName.setTextColor(Color.BLUE);
            personViewHolder.lessonName.setTextSize(24);
            personViewHolder.lessonbegin.setLayoutParams(new LinearLayout.LayoutParams( 0, CardView.LayoutParams.WRAP_CONTENT, 0.9f ));
            personViewHolder.Number_par.setText("");
            personViewHolder.Linr.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }
}