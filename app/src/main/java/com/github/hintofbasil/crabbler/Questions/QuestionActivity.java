package com.github.hintofbasil.crabbler.Questions;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.AboutUsActivity;
import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.AutoExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.ChoiceSelectExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.CommitExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.ModeChooseExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.MonthChoiceExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.ReturnExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.ThreeChoiceExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.UserDetailsExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DateRangeExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DateRangeSelectExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DayNightChoiceExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DoneExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.Expander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoChoiceExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoChoiceDateExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoPictureLayoutExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.YearChoiceExpander;
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
            if(QuestionManager.get() == null) {
                QuestionManager.init(this);
            }
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
                        expander = new ChoiceSelectExpander(this, questionJson);
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
                    case "Done":
                        expander = new DoneExpander(this, questionJson);
                        break;
                    case "MonthChoice":
                        expander = new MonthChoiceExpander(this, questionJson);
                        break;
                    case "YearChoice":
                        expander = new YearChoiceExpander(this, questionJson);
                        break;
                    case "ThreeChoice":
                        expander = new ThreeChoiceExpander(this, questionJson);
                        break;
                    case "Auto":
                        expander = new AutoExpander(this, questionJson);
                        break;
                    case "Commit":
                        expander = new CommitExpander(this, questionJson);
                        break;
                    case "Return":
                        expander = new ReturnExpander(this, questionJson);
                        break;
                    case "ModeChoose":
                        expander = new ModeChooseExpander(this, questionJson);
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
                qr.getJsonQuestion(questionId).getInt("questionNumber");
                pageOf.setText(String.format("%d/%d", questionId-1, definedQuestionCount-3));
            } catch (JSONException|NullPointerException e) {
                //pageOf.setVisibility(View.INVISIBLE);
                Log.i("QuestionActivity", "No question number.  Not displaying question of question");
            }

            //Disable previous
            if(questionId==0) {
                ImageView previousButton = (ImageView) findViewById(R.id.back_button);
                previousButton.setEnabled(false);
            }

            //Disable next
            if(questionId-1==realQuestionCount) {
                ImageView nextButton = (ImageView) findViewById(R.id.forward_button);
                nextButton.setEnabled(false);
            }

            /*if(questionId != 1) { // Only show menu button on first question
                LinearLayout menuButton = (LinearLayout) findViewById(R.id.toolbar_menu_button);
                menuButton.setVisibility(View.GONE);
            } else { //Hide images if menu button is present
                LinearLayout toolbarImage1 = (LinearLayout) findViewById(R.id.toolbar_image1);
                LinearLayout toolbarImage2 = (LinearLayout) findViewById(R.id.toolbar_image2);
                toolbarImage1.setVisibility(View.GONE);
                toolbarImage2.setVisibility(View.GONE);
            } */

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
                expander.enableDisableNext();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void previousQuestion(View view) {
        expander.previousQuestion();
    }

    public void nextQuestion(View view) {
        expander.nextQuestion(0, 1);
    }

    public void menuClick(View view) {
        Log.i("QuestionActivity", "Launching about us");
        Intent intent = new Intent(getBaseContext(),
                AboutUsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        if (expander.getClass().getSimpleName().equals("ModeChooseExpander"))
        {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
        else if (expander.getClass().getSimpleName().equals("DoneExpander"))
        {
            // No action on this page
        }
        else if (expander.getClass().getSimpleName().equals("AutoExpander"))
        {
            // No action on this page
        }
        else if (expander.getClass().getSimpleName().equals("ReturnExpander"))
        {
            SharedPreferences answerPrefs = this.getSharedPreferences(Keys.SAVED_PREFERENCES_KEY, Context.MODE_PRIVATE);
            answerPrefs.edit().clear().apply();
            Intent intent = new Intent(getBaseContext(), QuestionActivity.class);
            intent.putExtra(Keys.QUESTION_ID_KEY, 1);
            startActivity(intent);
            this.finish();
        }
        else
        {
            expander.previousQuestion();
            this.finish();
        }
    }
}
