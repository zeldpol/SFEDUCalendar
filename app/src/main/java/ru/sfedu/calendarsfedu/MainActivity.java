package ru.sfedu.calendarsfedu;

import android.os.Bundle;


import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.CompoundButton;

import android.widget.Toast;
import static android.widget.Toast.LENGTH_SHORT;

import com.squareup.timessquare.CalendarPickerView;

import java.nio.channels.AlreadyBoundException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CalendarPickerView calendarPickerView;

    public static  String atoken="";
    public static final  String HOST="http://46.101.100.248:8000/";
    public static final int USER_PASSWORD_MIN_LENGTH =6;
    public static final int USER_EMAIL_MIN_LENGTH =6;
    public static final int USER_GROUP_MIN_LENGTH = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//Create min date
        Date today = new Date();
        //Create max date
        Calendar YearNow = Calendar.getInstance();

        Calendar nextYear = Calendar.getInstance();

        //Add a year from Now
        nextYear.add(Calendar.YEAR, 1);
        YearNow.add(Calendar.YEAR, -1);


        //Find the calendar inside your view
        calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);


        //Init the calendar with Date Range ( from date -> to date )
        calendarPickerView.init(YearNow.getTime(), nextYear.getTime()).withSelectedDate(today);

        calendarPickerView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {


                Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                startActivity(intent);


                /*DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");

                String dateNow = df.format(calendarPickerView.getSelectedDate().getTime());

                String toast = "Selected: " + dateNow;
                Toast.makeText(getApplicationContext(), toast, LENGTH_SHORT).show();*/


                return false;
            }
        });

        MenuItem item = navigationView.getMenu().findItem(R.id.Calendar1);
        SwitchCompat switchCompat = (SwitchCompat) item.getActionView().findViewById(R.id.Calendar1);


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                    Toast.makeText(getApplicationContext(), "ВКЛ", LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "ВЫКЛ", LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, RegActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      /*  if (id == R.id.nav_camera) {
            // Handle the camera action
        }*/



        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }
}
