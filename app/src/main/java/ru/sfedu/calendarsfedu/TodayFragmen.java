package ru.sfedu.calendarsfedu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TodayFragmen extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



    public TodayFragmen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private List<Lesson> lessons;
    private RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_today, container, false);
        // Inflate the layout for this fragment

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        mRecyclerView  = (RecyclerView) view.findViewById(R.id.RecyclerView_today);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(LinearLayoutManager);

        initializeData();
        initializeAdapter();

        return view;
    }

    public void onRefresh() {
        // говорим о том, что собираемся начать
        Toast.makeText(getActivity(), R.string.refresh_started, Toast.LENGTH_SHORT).show();
        // начинаем показывать прогресс
        swipeLayout.setRefreshing(true);
        // ждем 3 секунды и прячем прогресс
        swipeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                // говорим о том, что собираемся закончить
                Toast.makeText(getActivity(), R.string.refresh_finished, Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }

    private void initializeData(){
        lessons = new ArrayList<>();
        lessons.add(new Lesson("Математика", "Г-301", "К.Э. Каибханов\nК.Э. Каибханов\nК.Э. Каибханов\n", "11:15", "12:40", "КТбо2-3"));
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
