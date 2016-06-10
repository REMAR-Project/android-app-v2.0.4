package com.github.hintofbasil.crabbler.Questions;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.AboutUsActivity;
import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.UserDetailsExpander;
import com.github.hintofbasil.crabbler.Settings.SettingsActivity;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DateRangeExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DateRangeSelectExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DayNightChoiceExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DoneExpander;
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
            QuestionManager qr = QuestionManager.get();
            JSONObject questionJson = null;

            // TODO make done an excludeFromCount question
            if(questionId==qr.getRealQuestionCount()) {
                expander = new DoneExpander(this, null);
            } else {
                questionJson = qr.getJsonQuestion(questionId);
                switch (questionJson.getString("questionType")) {
                    case "TwoPictureChoice":
                        expander = new TwoPictureLayoutExpander(this, questionJson);
                        break;
                    case "TwoChoiceDate":
                        expander = new TwoChoiceDateExpander(this, questionJson);
                        break;
                    case "TwoChoice":
                        expander = new TwoChoiceExpander(this, questionJson);
                        break;
                    case "DateRange":
                        expander = new DateRangeExpander(this, questionJson);
                        break;
                    case "DateRangeSelect":
                        expander = new DateRangeSelectExpander(this, questionJson);
                        break;
                    case "ListSelect":
                        expander = new ListSelectExpander(this, questionJson);
                        break;
                    case "YesNoExtra":
                        expander = new YesNoExtraExpander(this, questionJson);
                        break;
                    case "DayNightChoice":
                        expander = new DayNightChoiceExpander(this, questionJson);
                        break;
                    case "UserDetails":
                        expander = new UserDetailsExpander(this, questionJson);
                        break;
                    default:
                        Log.e("QuestionActivity", "Unknown question type.");
                        return;
                }
            }
            expander.expandLayout();

            try {
                questionJson.getBoolean("hideBackNext");
                ImageView previousButton = (ImageView) findViewById(R.id.back_button);
                ImageView nextButton = (ImageView) findViewById(R.id.forward_button);
                previousButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);
            } catch (JSONException|NullPointerException e) {

            }

            TextView pageOf = (TextView) findViewById(R.id.page_of);
            int realQuestionCount = qr.getRealQuestionCount();
            int definedQuestionCount = qr.getQuestionCount();
            try {
                int definedQuestionId = qr.getJsonQuestion(questionId).getInt("questionNumber");
                pageOf.setText(String.format("%d/%d", definedQuestionId + 1, definedQuestionCount));
            } catch (JSONException|NullPointerException e) {
                pageOf.setVisibility(View.INVISIBLE);
                Log.i("QuestionActivity", "No question number.  Not displaying question of question");
            }

            //Disable previous
            if(questionId==0) {
                ImageView previousButton = (ImageView) findViewById(R.id.back_button);
                previousButton.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    previousButton.setImageDrawable(getDrawable(R.drawable.previous_disabled));
                } else {
                    previousButton.setImageDrawable(getResources().getDrawable(R.drawable.previous_disabled));
                }
            }

            //Disable next
            if(questionId==realQuestionCount) {
                ImageView nextButton = (ImageView) findViewById(R.id.forward_button);
                nextButton.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    nextButton.setImageDrawable(getDrawable(R.drawable.next_disabled));
                } else {
                    nextButton.setImageDrawable(getResources().getDrawable(R.drawable.next_disabled));
                }
            }

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
            case R.id.settings:
                Log.i("QuestionActivity", "Launching settings");
                Intent intent1 = new Intent(getBaseContext(),
                        SettingsActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
