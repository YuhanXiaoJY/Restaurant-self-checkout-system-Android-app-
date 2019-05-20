package com.example.javaproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.security.AlgorithmConstraints;
import java.util.prefs.PreferenceChangeEvent;

public class SignupActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences("LogInfo", MODE_PRIVATE);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        sign_up = (Button) findViewById(R.id.sign_up);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                editor = pref.edit();

                if(password.equals("")) {
                    Toast.makeText(SignupActivity.this, "Empty password!", Toast.LENGTH_SHORT).show();
                    editor.clear();
                }
                editor.putString(account, password);
                editor.apply();
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.putExtra("Account", account);
                startActivity(intent);
                finish();
                }

            });

    }
}
