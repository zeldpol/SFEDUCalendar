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
import android.view.ViewDebug;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private ProgressDialog dialog;
    private Fragment weekFrag;
    ViewPagerAdapter adapter;

    String query;
    SearchView searchView;
    Menu mMenu;
    public static String Data = "";
    public static String atoken = "";
    public static final String APP_TOKEN = "token";
    public static final String APP_GROUPE = "grupe";
    public String lasgroupe;

    public static final String HOST = "http://46.101.100.248:8000/";
    public static final int USER_PASSWORD_MIN_LENGTH = 6;
    public static final int USER_EMAIL_MIN_LENGTH = 6;
    public static final int USER_GROUP_MIN_LENGTH = 7;
    public static final int USER_GROUP_MAX_LENGTH = 8;
    public static final int USER_FIRSTNAME_MIN_LENGTH = 2;
    public static final int USER_SECONDNAME_MIN_LENGTH = 2;
    public static final String APP_PREFERENCES = "mysettings";
    public static SharedPreferences mSettings;
    public static List<Lesson> Mainlessons;
    public static List<Lesson> Todaylessons;
    private boolean emtypair= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Календарь ЮФУ");

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Mainlessons = new ArrayList<>();
        Todaylessons = new ArrayList<>();
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



              /*  if (isChecked)
                    startActivity(intent);
                else
                    startActivity(intent);*/
            }
        });

        mDatabaseHelper = new DatabaseHelper(this, "mydatabase.db", null, 1);

        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        //   mSqLiteDatabase.delete("raspis", null, null);

    }

    private void ShowProgress(boolean show, String message, Context cont) {
        if (dialog == null)
            dialog = new ProgressDialog(cont);

        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        if (show)
            dialog.show();
        else
            dialog.dismiss();
    }

    public void SaveToBd(String group, String info) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.GROPE_COLUMN, group);
        values.put(DatabaseHelper.INFO_COLUMN, info);
        long DF = mSqLiteDatabase.insert("raspis", null, values);

        Log.d("DBBBBBBBBBBBB", "nomer " + DF);
    }

    public void UpdateBd(String group, String info) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.GROPE_COLUMN, group);
        values.put(DatabaseHelper.INFO_COLUMN, info);

        long DF = mSqLiteDatabase.update("raspis", values, "groupe = ?",
                new String[]{group});
        if (DF == 0)
            SaveToBd(group, info);

        Log.d("BDD", "Update " + DF);
    }

    public String FindInBd(String what) {
        Cursor cursor = mSqLiteDatabase.query("raspis", new String[]{DatabaseHelper.GROPE_COLUMN,
                        DatabaseHelper.INFO_COLUMN},
                null, null,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {


                String GROPE = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROPE_COLUMN));
                String INFO = cursor.getString(cursor.getColumnIndex(DatabaseHelper.INFO_COLUMN));
                if (GROPE.equals(what)) {
                    return INFO;
                }


            } while (cursor.moveToNext());
        }
        // не забываем закрывать курсор
        cursor.close();
        return "";
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

    public static void SaveLastGroupe(String groupe) {
        if (!groupe.isEmpty()) {
            // Запоминаем данные
            SharedPreferences.Editor editor = mSettings.edit();
            Log.d("Save", groupe);
            editor.putString(APP_GROUPE, groupe);
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
        if (mSettings.contains(APP_GROUPE)) {
            lasgroupe = mSettings.getString(APP_GROUPE, null);
            if (!lasgroupe.isEmpty()) {
                Mainlessons = ParsJson(FindInBd(lasgroupe), 1);
            }
        }

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
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        getQuery(intent);

    }

    private void CloseSearch() {
        if (mMenu != null && searchView != null) {
            searchView.setIconified(true); //
            searchView.clearFocus();     // ЗАКРЫВАЕМ ПОИСК

            (mMenu.findItem(R.id.menu_search)).collapseActionView(); //
        }
    }

    private void getQuery(Intent intent) {  // При поиске вызывается

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            query = intent.getStringExtra(SearchManager.QUERY);

            GetJson JsonGetter = new GetJson();
            JsonGetter.execute(query);
            SaveLastGroupe(query);
            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this, SearchableProvider.AUTHORITY, SearchableProvider.MODE);
            searchRecentSuggestions.saveRecentQuery(query, null);  // Сохраняем историю
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
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


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


    private String GetLesonName(String text) {
        if (text.length() < 2)
            return "";
        Pattern p;
        String res = "";
        Matcher m;
        if (text.contains("Физ-ра")) {
            p = Pattern.compile("[.]\\s([^)]+\\()");
            m = p.matcher(text);
            if (m.find())
                res = m.group(1);

            return res.replaceFirst("\\s\\(", "");
        } else {




            p = Pattern.compile("([А-Я][.][А-Я][.])\\s(([^)]+\\())");
            m = p.matcher(text);
            if (m.find())
                res = m.group(2);
            if(res.equals(""))
                return text;
            return res.replaceFirst("\\s\\(", "");

        }


    }

    private String GetLesonType(String text) {
        if (text.length() < 2)
            return "";
        String res2 = "";
        Pattern p2 = Pattern.compile("\\(\\S+\\)");
        Matcher m2 = p2.matcher(text);
        if (m2.find())
            res2 = m2.group();
        if(GetLesonName(text).contains(res2)) {

            return "";
        }

        return res2;

    }
    private String[] GetTime(String text) {

        String[] Darray = new String[]{"","",""};


        if (text.length() < 2)
            return Darray;
        Pattern p2 = Pattern.compile("(.*\\d\\:\\d+)\\-(\\d+\\:\\d+)");
        Matcher m2 = p2.matcher(text);
        if (m2.find()) {
            Darray[0] = m2.group(1);
            Darray[1] = m2.group(2);
        }
        return Darray;

    }

    private String GetTichers(String text) {

        if (text.length() < 2)
            return "";


        if(text.contains("СПОРТЗАЛ"))
            return "СПОРТЗАЛ";

        String res = "";
        Pattern p2 = Pattern.compile("(([А-Я]\\-[0-9]{1,4}))");
        Matcher m2 = p2.matcher(text);

        Pattern p = Pattern.compile("([А-Я][а-я]{1,20}\\s[А-Я]\\.[А-Я]\\.)");
        Matcher m = p.matcher(text);
        while (m.find() && m2.find())
            res += m.group() + "\n" + m2.group() + "\n";

        if (res.length() == 0)
            return "";

        return res.substring(0, res.length() - 1);

    }

    public void SetWeek(List<Lesson> lessons) {
        WeekFragment fragment = (WeekFragment) adapter.mFragmentList.get(2);
        fragment.newLeson(lessons);
    }
    public void SetToday() {
        TodayFragmen fragment = (TodayFragmen) adapter.mFragmentList.get(1);
        fragment.newLeson(Todaylessons);
    }


    List<Lesson> ParsJson(String json, int weekNumber) {
        List<Lesson> lessons;
        lessons = new ArrayList<>();
        String[] TimeA = new String[3];

        GregorianCalendar newCal = new GregorianCalendar( );
        int DAYOFWEEK = newCal.get( Calendar.DAY_OF_WEEK );
        if (json.length() < 3)
            return lessons;

        try {
            JSONObject reader = new JSONObject(json);
            JSONObject payload = new JSONObject();
            JSONObject schedule = new JSONObject();

            String tday[] =
                    {
                            "monday",
                            "tuesday",
                            "wednesday",
                            "thursday",
                            "friday",
                            "saturday"

                    };
            JSONObject week = new JSONObject();


            String time;
            String event;
            int inedT = 0;
            payload = reader.getJSONObject("payload");
            schedule = payload.getJSONObject("schedule");

            if (weekNumber == 1)
                week = schedule.getJSONObject("first");
            else
                week = schedule.getJSONObject("second");
            int parcount = 0;
            for (int i = 0; i < 6; i++) {
                // Log.wtf("JSON", week.getJSONObject(tday[i]).getString("day"));


                lessons.add(new Lesson(week.getJSONObject(tday[i]).getString("day"), "", "", "", "", ""));
                parcount = week.getJSONObject(tday[i]).getJSONArray("data").length();
                for (int j = 0; j < parcount; j++) {



                    if (week.getJSONObject(tday[i]).getJSONArray("data").getJSONObject(j).length() == 3) {
                        event = week.getJSONObject(tday[i]).getJSONArray("data").getJSONObject(j).getString("event");
                        time = week.getJSONObject(tday[i]).getJSONArray("data").getJSONObject(j).getString("time");
                        TimeA = GetTime(time);
                        lessons.add(new Lesson(GetLesonName(event) + "\n" + GetLesonType(event), Integer.toString(j+1), GetTichers(event), TimeA[0], TimeA[1], lasgroupe));

                        if(DAYOFWEEK -2== i)
                            Todaylessons.add(new Lesson(GetLesonName(event) + "\n" + GetLesonType(event), Integer.toString(j+1), GetTichers(event), TimeA[0], TimeA[1], lasgroupe));

                    } else {
                        event = "";
                        if(emtypair) {
                            time = week.getJSONObject(tday[i]).getJSONArray("data").getJSONObject(j).getString("time");
                            TimeA = GetTime(time);
                            lessons.add(new Lesson("", Integer.toString(j + 1), "", TimeA[0], TimeA[1], ""));

                            if(DAYOFWEEK== i-2)
                                Todaylessons.add(new Lesson("", Integer.toString(j + 1), "", TimeA[0], TimeA[1], ""));

                        }

                    }



                }
            }

        } catch (JSONException e) {
            MainActivity.ShowDialog(MainActivity.this, "Error JSON parsing: " + e.getMessage(), 5000);
        }

        return lessons;
    }


    class GetJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... path) {

            String content;

            try {

                content = getContent(MainActivity.HOST + "api/group/" + URLEncoder.encode(path[0], "utf-8"));
            } catch (IOException ex) {
                content = "Error: " + ex.getMessage();

            }

            return content;
        }

        @Override
        protected void onPostExecute(String content) {


            if (!content.contains("\"success\":")) {
                Mainlessons = ParsJson(FindInBd(query), 1);
                SetWeek(Mainlessons);
                SetToday();
                MainActivity.ShowDialog(MainActivity.this, "Не удалось получить данные с сервера. Проверьте интернет соединение", 5000);
                return;
            }


            if (content.contains("Error")) {

                MainActivity.ShowDialog(MainActivity.this, content, 5000);
                return;
            }

            if (content.contains("success\":true")) {

                //  MainActivity.ShowDialog(MainActivity.this, content,5000);


                UpdateBd(query, content);
                Mainlessons = ParsJson(FindInBd(query), 1);
                SetWeek(Mainlessons);
                SetToday();


                // Удачный поиск
            } else {

                if (content.contains("Schedule is unavailible for this group")) {
                    MainActivity.ShowDialog(MainActivity.this, "Группа не найдена!", 5000);
                    return;
                }


                if (content.contains("Bad request")) {
                    MainActivity.ShowDialog(MainActivity.this, "Проверьте правильность введенных данных. " + content, 5000);
                    return;
                }

                MainActivity.ShowDialog(MainActivity.this, content, 4000);

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
                    } catch (Exception e) {
                        return ("Error: Нет соединения с сервером");
                    }
                }
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));


                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        buf.append(line + "\n");
                    }

                    return (buf.toString());
                } else
                    return ("Error: Нет соединения с сервером");

            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

}
