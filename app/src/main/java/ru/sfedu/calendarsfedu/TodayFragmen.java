package ru.sfedu.calendarsfedu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class TodayFragmen extends Fragment{



    public TodayFragmen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private List<Lesson> lessons;
    private RecyclerView mRecyclerView ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_today, container, false);
        // Inflate the layout for this fragment

        mRecyclerView  = (RecyclerView) view.findViewById(R.id.RecyclerView_today);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(LinearLayoutManager);

        initializeData();
        initializeAdapter();

        return view;
    }

    private void initializeData(){
        lessons = new ArrayList<>();
        lessons.add(new Lesson("Математика", "Г-301", "К.Э. Каибханов", "11:15", "12:40", "КТбо2-3"));
        lessons.add(new Lesson("Информатика", "Д-128", "С.С. Парфенова", "08:00", "09:35", "КТбо6-3"));
        lessons.add(new Lesson("ОАиП", "А-101", "А.С. Свиридов", "15:50", "17:25", "РТбо1-3"));
        lessons.add(new Lesson("Математика", "Г-301", "К.Э. Каибханов", "11:15", "12:40", "КТбо2-3"));
        lessons.add(new Lesson("Информатика", "Д-128", "С.С. Парфенова", "08:00", "09:35", "КТбо2-3"));
        lessons.add(new Lesson("ОАиП", "А-101", "А.С. Свиридов", "15:50", "17:25", "РТбо1-3"));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(lessons);
        mRecyclerView.setAdapter(adapter);
    }

}
