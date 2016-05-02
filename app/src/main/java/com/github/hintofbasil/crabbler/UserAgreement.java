package com.github.hintofbasil.crabbler;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserAgreement extends AppCompatActivity {

    TextView agreement;
    Button acceptButton;
    Button declineButton;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        if(sharedPrefs.getBoolean(getString(R.string.agreement_accepted_key), false)) {
            //Skip if already accepted
            launchNextActivity();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        agreement = (TextView) findViewById(R.id.agreement_text);
        agreement.setMovementMethod(new ScrollingMovementMethod());

        acceptButton = (Button) findViewById(R.id.accept_button);
        declineButton = (Button) findViewById(R.id.decline_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefs.edit().putBoolean(getString(R.string.agreement_accepted_key), true).commit();
                launchNextActivity();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void launchNextActivity() {

    }
}
