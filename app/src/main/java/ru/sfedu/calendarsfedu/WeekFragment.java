package ru.sfedu.calendarsfedu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

import static ru.sfedu.calendarsfedu.MainActivity.WeekNumberNow;
import static ru.sfedu.calendarsfedu.MainActivity.adapterWeek;
import static ru.sfedu.calendarsfedu.MainActivity.isEven;
import static ru.sfedu.calendarsfedu.MainActivity.mRecyclerViewWeek;


public class WeekFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,RadioGroup.OnCheckedChangeListener {



    public WeekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    SwipeRefreshLayout swipeLayout;
    SegmentedGroup segmented2;
    public static RadioButton radioChetn;
    public static RadioButton radioNeChetn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_week, null);



        radioChetn = (RadioButton) view.findViewById(R.id.button21);
        radioNeChetn = (RadioButton) view.findViewById(R.id.button22);

        segmented2 = (SegmentedGroup) view.findViewById(R.id.segmented2);
        segmented2.setOnCheckedChangeListener(this);





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


    public static void SetTecWeek(int number)
    {

        if(radioChetn!=null && radioNeChetn!=null) {

            if(radioChetn.isChecked()==false && radioNeChetn.isChecked()==false) {

                Log.wtf("SetTecWeek","setChecked");
                if (isEven(WeekNumberNow) == 0) {
                    radioChetn.setChecked(true);
                } else {
                    radioNeChetn.setChecked(true);
                }
            }

            if (number == 0) {
                radioChetn.setText("Четная (тек.)");
                radioNeChetn.setText("Нечетная");

            } else {
                radioChetn.setText("Четная");
                radioNeChetn.setText("Нечетная (тек.)");
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.button21: {
                WeekNumberNow = 1;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("updateWeek", "123");
                startActivity(intent);

                break;
            }
            case R.id.button22: {

                WeekNumberNow = 2;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("updateWeek", "123");
                startActivity(intent);
                break;
            }
            default:
                // Nothing to do
        }
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
    public void newLeson()
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



