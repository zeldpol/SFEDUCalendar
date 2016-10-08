package ru.sfedu.calendarsfedu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;

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


public class RegActivity extends AppCompatActivity {

    private EditText Efirstname;
    private EditText Esurname;
    private EditText Eemail;
    private EditText Epassword;
    private EditText Egroup;
    private AppCompatButton btn_registration;
    private TextView link_login;

    private ProgressDialog dialog;

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
                    ShowProgress(true,"Регистрация...",RegActivity.this);
                    PostRegistration PostReg = new PostRegistration();
                    PostReg.execute();

                }

            }
        });

    }

    public boolean IsValid() {
        boolean res = true;

        if (!group.isEmpty()) {
            if (group.length() < MainActivity.USER_GROUP_MIN_LENGTH) {
                Egroup.setError("Группа должена содержать не менее " + Integer.toString(MainActivity.USER_GROUP_MIN_LENGTH) + " символов");
                focusView = Egroup;
                res = false;
            }

            if(group.length() > MainActivity.USER_GROUP_MAX_LENGTH)
            {
                Egroup.setError("Группа не может содержать более " + Integer.toString(MainActivity.USER_GROUP_MAX_LENGTH) + " символов");
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
        if (surname.length() < MainActivity.USER_FIRSTNAME_MIN_LENGTH) {
            Esurname.setError("Имя должено содержать не менее "+ Integer.toString(MainActivity.USER_FIRSTNAME_MIN_LENGTH) +" символов");
            focusView = Esurname;
            res = false;
        }
        if (firstname.length() < MainActivity.USER_SECONDNAME_MIN_LENGTH) {
            Efirstname.setError("Фамилия должена содержать не менее "+ Integer.toString(MainActivity.USER_SECONDNAME_MIN_LENGTH) +" символов");
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

            ShowProgress(false,"",RegActivity.this);
            if(!content.contains("\"success\":"))
            {
                MainActivity.ShowDialog(RegActivity.this, "Не удалось получить данные с сервера. Проверьте интернет соединение",5000);
                return;
            }


            if (content.contains("Error")) {

                MainActivity.ShowDialog(RegActivity.this, content,5000);
                return;
            }


            try {
                JSONObject reader = new JSONObject(content);

                if (content.contains("success\":true")) {

                    MainActivity.atoken = reader.getString("token");
                    MainActivity.ShowDialog(RegActivity.this, "Удачно, ваш токен господин: " + MainActivity.atoken,5000);
                    MainActivity.SaveToken();
                    // Удачная рега

                } else {

                    if (content.contains("User with such email or username already exist")) {
                        Eemail.setError("Пользователь с таким E-mail уже существует");
                        focusView = Eemail;
                        focusView.requestFocus();
                        return;
                    }


                    if (content.contains("Bad request")) {
                        MainActivity.ShowDialog(RegActivity.this, "Проверьте правильность введенных данных. "+ content,5000);
                        btn_registration.setError("Проверьте правильность введенных данных");
                        focusView = btn_registration;
                        focusView.requestFocus();
                        return;
                    }

                    MainActivity.ShowDialog(RegActivity.this, content,4000);

                }

            } catch (JSONException e) {
                content = "Error: " + e.getMessage();
                MainActivity.ShowDialog(RegActivity.this, content,5000);
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
