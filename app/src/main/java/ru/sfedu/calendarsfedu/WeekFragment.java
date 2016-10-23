package ru.sfedu.calendarsfedu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import static ru.sfedu.calendarsfedu.MainActivity.adapterWeek;
import static ru.sfedu.calendarsfedu.MainActivity.mRecyclerViewWeek;


public class WeekFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



    public WeekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    SwipeRefreshLayout swipeLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_week, null);


        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        mRecyclerViewWeek  = (RecyclerView) view.findViewById(R.id.RecyclerView_week);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewWeek.setLayoutManager(LinearLayoutManager);
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
    public void newLeson(List<Lesson> newleson)
    {
        initializeAdapter();
        adapterWeek.notifyDataSetChanged();
    }

    private void initializeAdapter(){
        adapterWeek = new RVAdapter(MainActivity.Mainlessons);
        if(mRecyclerViewWeek!=null)
        mRecyclerViewWeek.setAdapter(adapterWeek);
    }
}



