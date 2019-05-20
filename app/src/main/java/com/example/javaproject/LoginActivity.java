package com.example.javaproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.security.AlgorithmConstraints;
import java.util.prefs.PreferenceChangeEvent;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    private Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);


        pref = getSharedPreferences("LogInfo", MODE_PRIVATE);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        login = (Button) findViewById(R.id.login);
        sign_up = (Button) findViewById(R.id.sign_up);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if(isRemember)
        {
            String account1 = pref.getString("account_default", "");
            String password1 = pref.getString("password", "");
            accountEdit.setText(account1);
            passwordEdit.setText(password1);
            rememberPass.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String truePassword = pref.getString(account, "");

                if(!account.equals("") && !truePassword.equals("") && truePassword.equals(password))   //account matches the password
                {
                    editor = pref.edit();
                    if(rememberPass.isChecked())
                    {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account_default", account);
                        editor.putString("password", password);
                    }
                    else
                        editor.clear();
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("Account", account);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, truePassword, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(LoginActivity.this, "Account or password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
