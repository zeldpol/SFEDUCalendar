package ru.sfedu.calendarsfedu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class LoginActivity extends AppCompatActivity {
    private EditText Epassword;
    private EditText Eemail;
    private AppCompatButton btn_sign_in;
    private TextView link_reg;

    private View focusView;

    private String email;
    private String password;
    private ProgressDialog dialog;

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
        setContentView(R.layout.activity_login);

        Epassword = (EditText) findViewById(R.id.input_password_in);
        Eemail = (EditText) findViewById(R.id.input_email_in);
        btn_sign_in = (AppCompatButton) findViewById(R.id.btn_sign_in);
        link_reg = (TextView) findViewById(R.id.link_reg);




        link_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = Epassword.getText().toString();
                email = Eemail.getText().toString();




               if(!IsValid())
                   focusView.requestFocus();
                else
               {
                   ShowProgress(true,"Авторизация...",LoginActivity.this);
                   PostLogin PostLog = new PostLogin();
                   PostLog.execute();
                    // Проверки прошли
               }


            }
        });


    }

    private boolean IsValid()
    {
        boolean valid=true;

        if (!RegActivity.isPasswordValid(password) ) {
            Epassword.setError("Пароль должен содержать не менее "+Integer.toString(MainActivity.USER_PASSWORD_MIN_LENGTH)+" символов ");
            focusView = Epassword;
            valid = false;
        }

        if(!RegActivity.isEmailValid(email))
        {
            Eemail.setError("Некорректный E-mail: "+ email);
            focusView = Eemail;
            valid = false;
        }


        return valid;
    }


    class PostLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {

            String content;
            String param = null;
            try {
                param ="email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

            } catch(UnsupportedEncodingException ex) {
                content = "Error: "+ex.getMessage();
            }

            try{

                content = getContent(MainActivity.HOST+"api/users/login",param);
            }
            catch (IOException ex){
                content = "Error: "+ex.getMessage();
            }

            return content;
        }

        @Override
        protected void onPostExecute(String content) {

            ShowProgress(false,"",LoginActivity.this);

            if(content.contains("Error")) {
                Toast.makeText(getApplicationContext(), "Ошибка: " + content, LENGTH_LONG).show();
                return;
            }

            String success = "";
            try
            {
                JSONObject reader = new JSONObject(content);
                if (content.contains("success\":true")) {
                    MainActivity.atoken = reader.getString("token");
                    Toast.makeText(getApplicationContext(), "Удачно, ваш токен господин: " + MainActivity.atoken, LENGTH_LONG).show();
                    // Удачная авторизация
                }
                else
                {
                    if (content.contains("Wrong credentials")) {
                        Eemail.setError("Неправильный логин или пароль");
                        focusView = Eemail;
                        focusView.requestFocus();
                        return;
                    }

                    if (content.contains("Bad request")) {
                        Toast.makeText(getApplicationContext(), "Проверьте правильность введенных данных, ошибка: "+ content, LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(getApplicationContext(), "Ошибка: " + content, LENGTH_LONG).show();

                }


            }
            catch (JSONException e) {
                content = "Error: "+e.getMessage();
            }




        }

        private String getContent(String path,String urlParameters) throws IOException {
            BufferedReader reader=null;
            StringBuilder buf=new StringBuilder();
            try {
                URL url=new URL(path);
                HttpURLConnection c=(HttpURLConnection)url.openConnection();
                c.setRequestMethod("POST");
                c.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                c.setReadTimeout(10000);
                c.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream (
                        c.getOutputStream ());
                wr.writeBytes (urlParameters);
                wr.flush ();
                wr.close ();

                c.connect();



                InputStream inputStream = null;
                try
                {
                    inputStream = c.getInputStream();
                }
                catch(IOException exception)
                {
                    inputStream = c.getErrorStream();
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));


                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        buf.append(line + "\n");
                    }
                    return (buf.toString());



            }
            finally {
                if (reader != null) {
                    reader.close();

                }
            }
        }
    }



}
