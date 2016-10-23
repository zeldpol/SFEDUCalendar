package ru.sfedu.calendarsfedu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

        Toast.makeText(ScrollingActivity.this,Integer.toString(MainDate.getDay()), Toast.LENGTH_LONG).show();
        getSupportActionBar().setTitle(intent.getStringExtra("data"));

        Context context = this;

        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        initializeAdapter();
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

    public void newLeson(List<Lesson> newleson)
    {
        initializeAdapter();
        adapterMonth.notifyDataSetChanged();
    }

    private void initializeAdapter(){
        adapterMonth = new RVAdapter(Monthlessons);
        rv.setAdapter(adapterMonth);
    }
}

