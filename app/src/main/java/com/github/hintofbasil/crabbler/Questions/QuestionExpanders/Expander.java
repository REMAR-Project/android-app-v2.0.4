package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.Questions.QuestionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface to expand a layout using a question JSON object
 */
public abstract class Expander {

    AppCompatActivity activity;
    SharedPreferences prefs;
    int realQuestionId;
    int definedQuestionId = -1;
    JSONObject questionJson;
    QuestionManager questionManager;

    public Expander(AppCompatActivity activity, JSONObject questionJson) {
        this.activity = activity;
        this.prefs = activity.getSharedPreferences(Keys.SAVED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        this.realQuestionId = activity.getIntent().getIntExtra(Keys.QUESTION_ID_KEY, 0);
        this.questionJson = questionJson;
        questionManager = QuestionManager.get();
        try {
            definedQuestionId = questionJson.getInt("questionNumber");
        } catch (JSONException|NullPointerException e) {
            Log.i("Expander", "No defined question id");
        }

    }

    public abstract void expandLayout() throws JSONException;

    protected abstract void setPreviousAnswer(JSONArray answer);

    public abstract JSONArray getSelectedAnswer();

    public void nextQuestion(int delay) {
        //TODO handle last question
        final Intent intent = new Intent(activity, QuestionActivity.class);
        intent.putExtra(Keys.QUESTION_ID_KEY, realQuestionId + 1);
        new CountDownTimer(delay, delay) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                try {
                    saveAnswer();
                    activity.startActivity(intent);
                    activity.finish();
                } catch (IOException|JSONException e) {
                    Log.e("Expander", "Unable to save answer\n" + Log.getStackTraceString(e));
                }

            }
        }.start();
    }

    protected JSONArray getCurrentAnswer() throws IOException, JSONException {
        return getAnswer(definedQuestionId);
    }

    protected JSONArray getAnswer(int id) throws IOException, JSONException {
        return questionManager.getAnswer(id);
    }

    private void saveAnswer() throws IOException, JSONException {
        JSONArray answer = getSelectedAnswer();
        questionManager.saveAnswer(definedQuestionId, answer);
    }

    protected Drawable getDrawable(String name) {
        if(name.contains(".")) {
            name = name.substring(0, name.lastIndexOf('.'));
        }
        int drawableId = activity.getResources().getIdentifier(name, "drawable", activity.getPackageName());
        return ContextCompat.getDrawable(activity, drawableId);
    }

    public void setPreviousAnswer() {
        try {
            setPreviousAnswer(getCurrentAnswer());
        } catch (IOException|JSONException e) {
            Log.d("Expander", "Unable to load answer");
        }
    }

    public void previousQuestion() {
        final Intent intent = new Intent(activity, QuestionActivity.class);
        intent.putExtra(Keys.QUESTION_ID_KEY, realQuestionId - 1);
        activity.startActivity(intent);
        activity.finish();
    }

    public String getQuestionString(String string) throws JSONException {
        String value = questionJson.getString(string);
        StringBuilder sb = new StringBuilder(value);
        Pattern pattern = Pattern.compile("\\(\\d+\\)");
        Matcher match = pattern.matcher(value);
        while(match.find()) {
            String found = match.group();
            // Remove brackets
            String number = found.substring(1, found.length()-1);
            try {
                int answer = getAnswer(Integer.parseInt(number)).getInt(0);
                sb.replace(match.start(), match.end(), String.valueOf(answer));
            } catch (IOException|JSONException e) {
                try {
                    String answer = getAnswer(Integer.parseInt(number)).getString(0);
                    sb.replace(match.start(), match.end(), answer);
                } catch (IOException|JSONException e1) {
                    Log.e("Expander", "Invalid question id in " + value);
                    return "";
                }
            }
        }
        return getStringResourceOrReturn(sb.toString());
    }

    public String getStringResourceOrReturn(String string) {
        int drawableId = activity.getResources().getIdentifier(string, "string", activity.getPackageName());
        if(drawableId>0) {
            return activity.getString(drawableId);
        } else {
            return string;
        }
    }
}
