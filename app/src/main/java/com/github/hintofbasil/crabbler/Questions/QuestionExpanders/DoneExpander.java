package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostFactory;
import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 05/06/16.
 */
public class DoneExpander extends Expander {

    public DoneExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_done);

        CheckBox done = (CheckBox) activity.findViewById(R.id.chk_done);
        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    return;
                postAnswers();
            }
        });
    }

    @Override
    protected void setPreviousAnswer(String answer) {

    }

    @Override
    public String getSelectedAnswer() {
        return null;
    }

    private void postAnswers() {
        DataPostFactory dataPostFactory = new DataPostFactory(activity.getBaseContext());
        String answers = popAnswers();
        // Turns semi-colons into commas for processing
        answers = answers.replaceAll(";", ",");
        dataPostFactory.submitAnswers(answers);

        Toast.makeText(activity.getBaseContext(),
                activity.getString(R.string.thank_you_toast),
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(activity.getBaseContext(),
                QuestionActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private String popAnswers() {
        this.prefs = activity.getSharedPreferences(Keys.SAVED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String answers = prefs.getString(Keys.ANSWERS_KEY, null);
        prefs.edit().remove(Keys.ANSWERS_KEY).remove(Keys.QUESTION_ID_KEY).apply();
        return answers;
    }
}
