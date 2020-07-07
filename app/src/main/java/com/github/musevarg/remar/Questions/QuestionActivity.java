package com.github.musevarg.remar.Questions;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.musevarg.remar.AboutUsActivity;
import com.github.musevarg.remar.Keys;
import com.github.musevarg.remar.Questions.QuestionExpanders.AutoExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.ChoiceSelectExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.CommitExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.ModeChooseExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.MonthChoiceExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.OneChoiceExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.ReturnExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.ThreeChoiceExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.UserDetailsExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.DateRangeExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.DateRangeSelectExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.DayNightChoiceExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.DoneExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.Expander;
import com.github.musevarg.remar.Questions.QuestionExpanders.TwoChoiceExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.TwoChoiceDateExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.TwoPictureLayoutExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.YearChoiceExpander;
import com.github.musevarg.remar.Questions.QuestionExpanders.YesNoExtraExpander;
import com.github.musevarg.remar.R;

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
                    case "OneChoice":
                        expander = new OneChoiceExpander(this, questionJson);
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
                pageOf.setText(String.format("%d/%d", questionId + 1, definedQuestionCount));
            } catch (JSONException|NullPointerException e) {
                pageOf.setVisibility(View.INVISIBLE);
                Log.i("QuestionActivity", "No question number.  Not displaying question of question");
            }

            //Disable previous
            if(questionId==0) {
                ImageView previousButton = (ImageView) findViewById(R.id.back_button);
                previousButton.setEnabled(false);
            }

            //Disable next
            if(questionId==realQuestionCount) {
                ImageView nextButton = (ImageView) findViewById(R.id.forward_button);
                nextButton.setEnabled(false);
            }

            if(questionId != 1) { // Only show menu button on first question
                LinearLayout menuButton = (LinearLayout) findViewById(R.id.toolbar_menu_button);
                menuButton.setVisibility(View.GONE);
            } else { //Hide images if menu button is present
                LinearLayout toolbarImages = (LinearLayout) findViewById(R.id.toolbar_images);
                toolbarImages.setVisibility(View.GONE);
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
            expander.enableDisableNext();
        }
    }

    public void previousQuestion(View view) {
        expander.previousQuestion();
    }

    public void nextQuestion(View view) {
        expander.nextQuestion(0);
    }

    public void menuClick(View view) {
        Log.i("QuestionActivity", "Launching about us");
        Intent intent = new Intent(getBaseContext(),
                AboutUsActivity.class);
        startActivity(intent);
    }
}
