package ru.sfedu.calendarsfedu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;


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
    View focusView;

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



            }
        });

    }
    private boolean isEmailValid(String email) {

        return email.contains("@") && email.length() > 4 &&  email.contains(".");
    }
    private boolean isPasswordValid(String password) {

        return  password.length() > 4 ;
    }

    public boolean IsValid() {
        boolean res = true;

        if (!isPasswordValid(password) ) {
            Epassword.setError("Пароль должен содержать не менее 4 символов");
            focusView = Epassword;
            res = false;
        }

        if (!isEmailValid(email) ) {
            Eemail.setError("Некорректный E-mail");
            focusView = Eemail;
            res = false;
        }
        if (surname.length() < 1 ) {
            Esurname.setError("Поле не может быть пустым");
            focusView = Esurname;
            res = false;
        }
        if (firstname.length() < 1 ) {
            Efirstname.setError("Поле не может быть пустым");
            focusView = Efirstname;
            res = false;
        }

        return res;
    }



}
