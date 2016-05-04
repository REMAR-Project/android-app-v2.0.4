package com.github.hintofbasil.crabbler.Questions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InputStream jsonInputStream = getBaseContext().getResources().openRawResource(R.raw.questions);
        byte[] buffer = new byte[1024];
        try {
            int length = jsonInputStream.read(buffer);
            String jsonString = new String(buffer).substring(0, length);
            int questionId = getIntent().getIntExtra(getString(R.string.question_id_key), 0);
            JSONObject questionJson = new JSONArray(jsonString).getJSONObject(questionId);

            switch(questionJson.getString("QuestionType")) {
                case "TwoPictureChoice":
                    setContentView(R.layout.two_picture_choice_layout);
                    break;
                default:
                    Log.e("QuestionActivity", "Inknown question type.");
                    return;
            }

        } catch (IOException|JSONException e) {
            Log.e("QuestionActivity", "Error reading questions");
            return;
        }
    }
}
