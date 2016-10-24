package ru.sfedu.calendarsfedu;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import com.squareup.timessquare.CalendarPickerView;

import static android.widget.Toast.LENGTH_SHORT;
import static ru.sfedu.calendarsfedu.MainActivity.MainDate;


public class MonthFragment extends Fragment{



    public MonthFragment() {
        // Required empty public constructor

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static final String TAG = "CalendarFragment";
    View viewer;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewer = (View) inflater.inflate(R.layout.fragment_month, container,
                false);
        //Create min date
        Date today = new Date();
        //Create max date
        Calendar YearNow = Calendar.getInstance();

        Calendar nextYear = Calendar.getInstance();

        //Add a year from Now
        nextYear.add(Calendar.MONTH, 6);
        YearNow.add(Calendar.MONTH, -3);




        final CalendarPickerView calendar = (CalendarPickerView) viewer.findViewById(R.id.calendar_view);
        calendar.init(YearNow.getTime(), nextYear.getTime()).withSelectedDate(today);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {

            @Override
            public void onDateUnselected(Date date) {

            }

            @Override
            public void onDateSelected(Date date) {
                MainDate = date;

                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                String  data = df.format(calendar.getSelectedDate().getTime());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("start", "click");
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });


        return viewer;

    }



}



