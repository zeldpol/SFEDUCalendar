package ru.sfedu.calendarsfedu;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.Toast.LENGTH_LONG;


public class RegActivity extends AppCompatActivity {

    private EditText Efirstname;
    private EditText Esurname;
    private EditText Eemail;
    private EditText Epassword;
    private EditText Egroup;
    private AppCompatButton btn_registration;
    private TextView link_login;

    private String firstname;
    private String surname;
    private String email;
    private String password;
    private String group;
    private View focusView;

    public static boolean isEmailValid(String email) {

        return email.contains("@") && email.length() >= MainActivity.USER_EMAIL_MIN_LENGTH && email.contains(".");
    }

    public static boolean isPasswordValid(String password) {

        return password.length() >= MainActivity.USER_PASSWORD_MIN_LENGTH;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        Efirstname = (EditText) findViewById(R.id.input_firstname);
        Esurname = (EditText) findViewById(R.id.input_surname);
        Eemail = (EditText) findViewById(R.id.input_email);
        Epassword = (EditText) findViewById(R.id.input_password);
        Egroup = (EditText) findViewById(R.id.input_group);
        btn_registration = (AppCompatButton) findViewById(R.id.btn_registration);
        link_login = (TextView) findViewById(R.id.link_login);

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = Efirstname.getText().toString();
                surname = Esurname.getText().toString();
                email = Eemail.getText().toString();
                password = Epassword.getText().toString();
                group = Egroup.getText().toString();

                if (!IsValid())
                    focusView.requestFocus();
                else {
                    // Проверки прошли

                    PostRegistration PostReg = new PostRegistration();
                    PostReg.execute();

                }

            }
        });

    }

    public boolean IsValid() {
        boolean res = true;

        if (!group.isEmpty()) {
            if (group.length() < 7) {
                Egroup.setError("Группа должена содержать не менее " + Integer.toString(MainActivity.USER_GROUP_MIN_LENGTH) + " символов");
                focusView = Egroup;
                res = false;
            }
        }

        if (!isPasswordValid(password)) {
            Epassword.setError("Пароль должен содержать не менее " + Integer.toString(MainActivity.USER_PASSWORD_MIN_LENGTH) + " символов");
            focusView = Epassword;
            res = false;
        }

        if (!isEmailValid(email)) {
            Eemail.setError("Некорректный E-mail");
            focusView = Eemail;
            res = false;
        }
        if (surname.length() < 1) {
            Esurname.setError("Поле не может быть пустым");
            focusView = Esurname;
            res = false;
        }
        if (firstname.length() < 1) {
            Efirstname.setError("Поле не может быть пустым");
            focusView = Efirstname;
            res = false;
        }

        return res;
    }

    class PostRegistration extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... path) {

            String content;

            String param = null;

            try {
                param = "firstname=" + URLEncoder.encode(firstname, "UTF-8") +
                        "&surname=" + URLEncoder.encode(surname, "UTF-8") +
                        "&email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");


                if(!group.isEmpty())
                    param += "&group=" + URLEncoder.encode(group, "UTF-8");


            } catch (UnsupportedEncodingException ex) {
                content = "Error: " + ex.getMessage();
            }


            try {

                content = getContent(MainActivity.HOST + "api/users/register", param);
            } catch (IOException ex) {
                content = "Error: " + ex.getMessage();

            }

            return content;
        }

        @Override
        protected void onPostExecute(String content) {

            if (content.contains("Error")) {
                Toast.makeText(getApplicationContext(), "Ошибка: " + content, LENGTH_LONG).show();
                return;
            }


            try {
                JSONObject reader = new JSONObject(content);

                if (content.contains("success\":true")) {

                    MainActivity.atoken = reader.getString("token");
                    Toast.makeText(getApplicationContext(), "Удачно, ваш токен господин: " + MainActivity.atoken, LENGTH_LONG).show();
                    // Удачная рега

                } else {

                    if (content.contains("User with such email or username already exist")) {
                        Eemail.setError("Пользователь с таким E-mail уже существует");
                        focusView = Eemail;
                        focusView.requestFocus();
                        return;
                    }


                    if (content.contains("Bad request")) {

                        btn_registration.setError("Проверьте правильность введенных данных");
                        focusView = btn_registration;
                        focusView.requestFocus();
                        return;
                    }


                    Toast.makeText(getApplicationContext(), "Ошибка: " + content, LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                content = "Error: " + e.getMessage();
                Toast.makeText(getApplicationContext(), "Ошибка: " + content, LENGTH_LONG).show();
            }
        }

        private String getContent(String path, String urlParameters) throws IOException {

            BufferedReader reader = null;
            StringBuilder buf = new StringBuilder();
            try {

                URL url = new URL(path);

                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                c.setRequestMethod("POST");
                c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                c.setReadTimeout(10000);
                c.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(c.getOutputStream());

                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                c.connect();

                InputStream inputStream = null;

                try {
                    inputStream = c.getInputStream();
                } catch (IOException exception) {
                    inputStream = c.getErrorStream();
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));


                String line = null;

                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }

                return (buf.toString());
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

}
