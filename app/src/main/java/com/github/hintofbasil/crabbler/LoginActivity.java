package com.github.hintofbasil.crabbler;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private SharedPreferences toSendPrefs;
    private SharedPreferences savedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toSendPrefs = getSharedPreferences(getString(R.string.to_send_preferences_key), Context.MODE_PRIVATE);
        savedPrefs = getSharedPreferences(getString(R.string.saved_preferences_key), Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateUserHash();
            }
        });
    }

    private void calculateUserHash() {
        String firstName = getData(R.id.first_name_input);
        String surName = getData(R.id.surname_input);
        String password = getData(R.id.password_input);
        try {
            //TODO decide hashing algorithm.  Probably BCrypt
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //TODO decide string format
            String toHash = firstName + surName + password;
            byte[] bytes = toHash.getBytes();
            md.update(bytes);
            String hash = new String(Base64.encode(md.digest(), Base64.URL_SAFE));
            toSendPrefs.edit().putString(getString(R.string.url_login), toHash).apply();
            savedPrefs.edit().putString(getString(R.string.preference_session_id), hash).apply();
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(getBaseContext(),
                    getString(R.string.internal_error),
                    Toast.LENGTH_LONG).show();
        }
    }

    private String getData(int resourceId) {
        EditText editText = (EditText) findViewById(resourceId);
        return editText.getText().toString();
    }
}
