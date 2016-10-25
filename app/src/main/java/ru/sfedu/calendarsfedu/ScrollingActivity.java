package ru.sfedu.calendarsfedu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static ru.sfedu.calendarsfedu.MainActivity.MainDate;
import static ru.sfedu.calendarsfedu.MainActivity.Monthlessons;
import static ru.sfedu.calendarsfedu.MainActivity.adapterMonth;
import static ru.sfedu.calendarsfedu.MainActivity.adapterToday;
import static ru.sfedu.calendarsfedu.MainActivity.rv;


public class ScrollingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.SToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String chetn="";
        if(intent.getStringExtra("chetn")!=null)
        {
            Log.wtf("Month",intent.getStringExtra("chetn"));
            if(intent.getStringExtra("chetn").equals("1"))
                chetn = "Нечетная неделя ";
            else
                chetn = "Четная неделя ";
        }


        if(intent.getStringExtra("data")!=null)
        getSupportActionBar().setTitle(chetn+intent.getStringExtra("data").toString());

        Context context = this;

        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        initializeAdapterMonth();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void newLesonMonth()
    {
        initializeAdapterMonth();
        adapterMonth.notifyDataSetChanged();
    }

    public static void initializeAdapterMonth(){
        adapterMonth = new RVAdapter(Monthlessons);
        if(rv!=null)
        rv.setAdapter(adapterMonth);
    }
}

