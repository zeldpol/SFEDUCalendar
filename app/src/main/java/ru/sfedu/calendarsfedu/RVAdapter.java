package ru.sfedu.calendarsfedu;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView lessonName;
        TextView lessonauditorium;
        TextView lessonteacher;
        TextView lessonbegin;
        TextView lessonend;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            lessonName = (TextView)itemView.findViewById(R.id.lesson_name);
            lessonauditorium = (TextView)itemView.findViewById(R.id.lesson_audit);
            lessonteacher = (TextView)itemView.findViewById(R.id.lessson_teacher);
            lessonbegin = (TextView)itemView.findViewById(R.id.lesson_begin);
            lessonend = (TextView)itemView.findViewById(R.id.lesson_end);
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
        personViewHolder.lessonauditorium.setText(lessons.get(i).auditorium);
        personViewHolder.lessonteacher.setText(lessons.get(i).teacher);
        personViewHolder.lessonbegin.setText(lessons.get(i).begin);
        personViewHolder.lessonend.setText(lessons.get(i).end);

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }
}