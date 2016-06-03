package com.github.hintofbasil.crabbler.Questions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.AboutUsActivity;
import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DateRangeExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DateRangeSelectExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.Expander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.ListSelectExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoChoiceExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoChoiceDateExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoPictureLayoutExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.YesNoExtraExpander;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class QuestionActivity extends AppCompatActivity {

    Expander expander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            int questionId = getIntent().getIntExtra(Keys.QUESTION_ID_KEY, 0);
            QuestionReader qr = new QuestionReader();
            JSONObject questionJson = qr.getJsonQuestion(this, questionId);

            switch(questionJson.getString("questionType")) {
                case "TwoPictureChoice":
                    expander = new TwoPictureLayoutExpander(this);
                    break;
                case "TwoChoiceDate":
                    expander = new TwoChoiceDateExpander(this);
                    break;
                case "TwoChoice":
                    expander = new TwoChoiceExpander(this);
                    break;
                case "DateRange":
                    expander = new DateRangeExpander(this);
                    break;
                case "DateRangeSelect":
                    expander = new DateRangeSelectExpander(this);
                    break;
                case "ListSelect":
                    expander = new ListSelectExpander(this);
                    break;
                case "YesNoExtra":
                    expander = new YesNoExtraExpander(this);
                    break;
                default:
                    Log.e("QuestionActivity", "Unknown question type.");
                    return;
            }
            expander.expandLayout(questionJson);

            TextView pageOf = (TextView) findViewById(R.id.page_of);
            int questionCount = qr.getJsonQuestions(this).length();
            pageOf.setText(String.format("%d/%d", questionId+1, questionCount));

        } catch (IOException|JSONException e) {
            Log.e("QuestionActivity", "Error reading questions\n" + Log.getStackTraceString(e));
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(expander!=null) {
            expander.setPreviousAnswer();
        }
    }

    public void previousQuestion(View view) {
        expander.previousQuestion();
    }

    public void nextQuestion(View view) {
        expander.nextQuestion(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us:
                Log.i("QuestionActivity", "Launching about us");
                Intent intent = new Intent(getBaseContext(),
                        AboutUsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
