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


public class ScrollingActivity extends AppCompatActivity {

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
        lessons.add(new Lesson("Математика", "Г-301", "К.Э. Каибханов", "11:15", "12:40"));
        lessons.add(new Lesson("Информатика", "Д-128", "С.С. Парфенова", "08:00", "09:35"));
        lessons.add(new Lesson("ОАиП", "А-101", "А.С. Свиридов", "15:50", "17:25"));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(lessons);
        rv.setAdapter(adapter);
    }
}

