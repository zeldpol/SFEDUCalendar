package ru.sfedu.calendarsfedu;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static ru.sfedu.calendarsfedu.MainActivity.Data;

/**
 * Created by ZeldPol on 12.10.2016.
 */

public class DateLessons extends AppCompatActivity {


    private List<Lesson> lessons;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.SToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Data);

        Context context = this;

        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        initializeData();
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

    private void initializeData(){
        lessons = new ArrayList<>();
        lessons.add(new Lesson("Математика", "Г-301", "К.Э. Каибханов", "11:15", "12:40", "КТбо2-3"));
        lessons.add(new Lesson("Информатика", "Д-128", "С.С. Парфенова", "08:00", "09:35", "КТбо2-3"));
        lessons.add(new Lesson("ОАиП", "А-101", "А.С. Свиридов", "15:50", "17:25", "КТбо2-3"));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(lessons);
        rv.setAdapter(adapter);
    }
}

