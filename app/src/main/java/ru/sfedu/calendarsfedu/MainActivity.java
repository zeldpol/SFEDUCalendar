package ru.sfedu.calendarsfedu;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.SearchRecentSuggestions;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;


import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private ProgressDialog dialog;


    String query;
    SearchView searchView;
    Menu mMenu;
    public static String Data = "";
    public static String atoken = "";
    public static final String APP_TOKEN = "token";
    public static final String HOST = "http://46.101.100.248:8000/";
    public static final int USER_PASSWORD_MIN_LENGTH = 6;
    public static final int USER_EMAIL_MIN_LENGTH = 6;
    public static final int USER_GROUP_MIN_LENGTH = 7;
    public static final int USER_GROUP_MAX_LENGTH = 8;
    public static final int USER_FIRSTNAME_MIN_LENGTH = 2;
    public static final int USER_SECONDNAME_MIN_LENGTH = 2;
    public static final String APP_PREFERENCES = "mysettings";
    public static SharedPreferences mSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Календарь ЮФУ");

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // переключение табов
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

                if (position == 2) { // Если таб с расписанием делаем кнопку поиска видимой
                    if (mMenu != null)

                        mMenu.findItem(R.id.menu_search).setVisible(true);
                } else {
                    if (mMenu != null) {
                        CloseSearch();
                        mMenu.findItem(R.id.menu_search).setVisible(false);
                    }
                }


            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Intent intent = new Intent(MainActivity.this, RegActivity.class);

        MenuItem item = navigationView.getMenu().findItem(R.id.Calendar1);
        SwitchCompat switchCompat = (SwitchCompat) item.getActionView().findViewById(R.id.Calendar1);


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SaveToBd("КТБО2-3","{123123WQEQWEWQEWQEQEQEQWEWQE}");
                SaveToBd("КТБО2-4","{wqewqewqewqe}");


              /*  if (isChecked)
                    startActivity(intent);
                else
                    startActivity(intent);*/
            }
        });

        mDatabaseHelper = new DatabaseHelper(this, "mydatabase.db", null, 1);

        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        mSqLiteDatabase.delete("raspis",null,null);

    }
    private void ShowProgress(boolean show,String message,Context cont)
    {
        if(dialog==null)
            dialog = new ProgressDialog(cont);

        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        if(show)
            dialog.show();
        else
            dialog.dismiss();
    }

    public  void SaveToBd(String group ,String info)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.GROPE_COLUMN, group);
        values.put(DatabaseHelper.INFO_COLUMN, info);
      long DF=  mSqLiteDatabase.insert("raspis", null, values);

        Log.d("DBBBBBBBBBBBB","nomer " +DF);
    }
    public static void SaveToken() {
        if (!atoken.isEmpty()) {
            // Запоминаем данные
            SharedPreferences.Editor editor = mSettings.edit();
            Log.d("Save", atoken);
            editor.putString(APP_TOKEN, atoken);
            editor.apply();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains(APP_TOKEN)) {
            atoken = mSettings.getString(APP_TOKEN, null);

            if (atoken.isEmpty()) {

                Intent intentSet = new Intent(MainActivity.this, RegActivity.class);
                startActivity(intentSet);
            }
        } else {
            Intent intentSet = new Intent(MainActivity.this, RegActivity.class);
            startActivity(intentSet);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        getQuery(intent);

    }
    private void CloseSearch()
    {
        if (mMenu != null && searchView!= null) {
        searchView.setIconified(true); //
        searchView.clearFocus();     // ЗАКРЫВАЕМ ПОИСК

            (mMenu.findItem(R.id.menu_search)).collapseActionView(); //
        }
    }

    private void getQuery(Intent intent) {  // При поиске вызывается

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);

            GetJson JsonGetter = new GetJson();
            JsonGetter.execute(query);



            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this, SearchableProvider.AUTHORITY, SearchableProvider.MODE);
            searchRecentSuggestions.saveRecentQuery(query, null);  // Сохраняем историю





            /*Cursor cursor = mSqLiteDatabase.query("raspis", new String[] {DatabaseHelper.GROPE_COLUMN,
                            DatabaseHelper.INFO_COLUMN},
                    null, null,
                    null, null, null) ;

           if(cursor.moveToFirst()) {
               do {


                   String catName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROPE_COLUMN));
                   String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INFO_COLUMN));
                   Toast.makeText(MainActivity.this, catName + "  " + phoneNumber, Toast.LENGTH_SHORT).show();


               }while(cursor.moveToNext());
           }
            // не забываем закрывать курсор
            cursor.close();*/


            CloseSearch();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.SearchAutoComplete autoCompleteTextView = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);

        if (autoCompleteTextView != null) {
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.AllCaps();  // Все капсом только!
            autoCompleteTextView.setDropDownBackgroundResource(R.drawable.searhselect); //Цвет бэкграунда у выподающей подсказки
            autoCompleteTextView.setFilters(FilterArray);
        }

        searchView.setIconifiedByDefault(true);
        menu.findItem(R.id.menu_search).setVisible(false);  // Делаем кнопку поиска невидимой
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentSet = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intentSet);
        }
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        if (id == android.R.id.home)
            this.finish();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    public static void ShowDialog(Context cont, String message, int timeout) {

        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setTitle("");
        builder.setMessage(message);
        builder.setCancelable(true);

        final AlertDialog dlg = builder.create();

        dlg.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                dlg.dismiss();
                timer.cancel();

            }
        }, timeout);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MonthFragment(), "Календарь");
        adapter.addFragment(new TodayFragmen(), "Сегодня");
        adapter.addFragment(new WeekFragment(), "Расписание");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    class GetJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... path) {

            String content;

            try {

                content = getContent(MainActivity.HOST + "api/group/"+ URLEncoder.encode(path[0],"utf-8"));
            } catch (IOException ex) {
                content = "Error: " + ex.getMessage();

            }

            return content;
        }

        @Override
        protected void onPostExecute(String content) {


            if(!content.contains("\"success\":"))
            {
                MainActivity.ShowDialog(MainActivity.this, "Не удалось получить данные с сервера. Проверьте интернет соединение",5000);
                return;
            }


            if (content.contains("Error")) {

                MainActivity.ShowDialog(MainActivity.this, content,5000);
                return;
            }


            try {
                JSONObject reader = new JSONObject(content);

                if (content.contains("success\":true")) {

                    MainActivity.ShowDialog(MainActivity.this, content,5000);

                    // Удачный поиск
                } else {

                    if (content.contains("Schedule is unavailible for this group")) {
                        MainActivity.ShowDialog(MainActivity.this, "Группа не найдена!",5000);
                        return;
                    }


                    if (content.contains("Bad request")) {
                        MainActivity.ShowDialog(MainActivity.this, "Проверьте правильность введенных данных. "+ content,5000);
                        return;
                    }

                    MainActivity.ShowDialog(MainActivity.this, content,4000);

                }

            } catch (JSONException e) {
                content = "Error: " + e.getMessage();
                MainActivity.ShowDialog(MainActivity.this, content,5000);
            }
        }

        private String getContent(String path) throws IOException {

            BufferedReader reader = null;
            StringBuilder buf = new StringBuilder();
            try {

                URL url = new URL(path);

                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                c.setRequestMethod("GET");
                c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                c.setReadTimeout(10000);
                c.setDoOutput(false);
                c.connect();

                InputStream inputStream = null;

                try {
                    inputStream = c.getInputStream();
                } catch (IOException exception) {
                    try {
                        inputStream = c.getErrorStream();
                    }
                    catch(Exception e)
                    {
                        return ("Error: Нет соединения с сервером");
                    }
                }
                if(inputStream!=null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));


                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        buf.append(line + "\n");
                    }

                    return (buf.toString());
                }
                else
                    return ("Error: Нет соединения с сервером");

            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

}
