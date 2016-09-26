package ru.sfedu.calendarsfedu;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;
import java.util.Date;


import com.squareup.timessquare.CalendarPickerView;




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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewer = (View) inflater.inflate(R.layout.fragment_month, container,
                false);
        //Create min date
        Date today = new Date();
        //Create max date
        Calendar YearNow = Calendar.getInstance();

        Calendar nextYear = Calendar.getInstance();

        //Add a year from Now
        nextYear.add(Calendar.YEAR, 1);
        YearNow.add(Calendar.YEAR, -1);


        final CalendarPickerView calendar = (CalendarPickerView) viewer.findViewById(R.id.calendar_view);
        calendar.init(YearNow.getTime(), nextYear.getTime()).withSelectedDate(today);

        return viewer;

    }



}



