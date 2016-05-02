package com.github.hintofbasil.crabbler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class UserAgreement extends AppCompatActivity {

    TextView agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        agreement = (TextView) findViewById(R.id.agreement_text);
        agreement.setMovementMethod(new ScrollingMovementMethod());
    }
}
