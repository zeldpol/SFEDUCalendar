package ru.sfedu.calendarsfedu;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WeekFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public WeekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        lessons = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    private List<Lesson> lessons;
    private RecyclerView mRecyclerView ;
    private  RVAdapter adapter;
    TextView odd;
    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_week, container, false);
        // Inflate the layout for this fragment

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));


        mRecyclerView  = (RecyclerView) view.findViewById(R.id.RecyclerView_week);
        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(LinearLayoutManager);

        //initializeData();
        initializeAdapter();


        return view;

    }


    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.v("fling", "Flinged.");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }
    };


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


    public void ClearAdapter()
    {
        lessons.clear();
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    public void newLeson(List<Lesson> newleson)
    {
        lessons=newleson;
        initializeAdapter();
        adapter.notifyDataSetChanged();
    }

    private void initializeAdapter(){
        adapter = new RVAdapter(MainActivity.Mainlessons);
        mRecyclerView.setAdapter(adapter);
    }

}



