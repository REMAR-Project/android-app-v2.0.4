package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;

import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.Questions.QuestionManager;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    protected int requiredAnswers;

    public Expander(AppCompatActivity activity, JSONObject questionJson, int requiredAnswers) {
        this.activity = activity;
        this.prefs = activity.getSharedPreferences(Keys.SAVED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        this.realQuestionId = activity.getIntent().getIntExtra(Keys.QUESTION_ID_KEY, 0);
        this.questionJson = questionJson;
        this.requiredAnswers = requiredAnswers;
        questionManager = QuestionManager.get();
        try {
            definedQuestionId = questionJson.getInt("questionNumber");
        } catch (JSONException|NullPointerException e) {
            Log.i("Expander", "No defined question id");
        }
        try {
            boolean optional = questionJson.getBoolean("optional");
            if(optional) {
                this.requiredAnswers = 0;
            }
        } catch (JSONException|NullPointerException e) {
            Log.i("Expander", "No optional argument given");
        }

    }

    public abstract void expandLayout() throws JSONException;

    protected abstract void setPreviousAnswer(JSONArray answer);

    public abstract JSONArray getSelectedAnswer();

    public void nextQuestion(int delay) {
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
        return getAnswer(realQuestionId);
    }

    protected JSONArray getAnswer(int id) throws IOException, JSONException {
        return questionManager.getAnswer(id);
    }

    private void saveAnswer() throws IOException, JSONException {
        JSONArray answer = getSelectedAnswer();
        questionManager.saveAnswer(realQuestionId, answer);
        if(questionJson.has("cacheAnswer")) {
            setCachedAnswer(answer);
        }
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
            JSONArray def = getCachedAnswer();
            if(def!=null) {
                try {
                    applyCachedAnswer(def);
                } catch (JSONException e1) {
                    setPreviousAnswer(new JSONArray()); // Allow populating of lists
                    Log.d("Expander", "Unable to load answer");
                }
            } else {
                setPreviousAnswer(new JSONArray()); // Allow populating of lists
                Log.d("Expander", "Unable to load answer");
            }
        }
    }

    public void previousQuestion() {
        final Intent intent = new Intent(activity, QuestionActivity.class);
        intent.putExtra(Keys.QUESTION_ID_KEY, realQuestionId - 1);
        try
        {
            saveAnswer();
        }
        catch (IOException|JSONException e) {
            Log.e("Expander", "Unable to save answer\n" + Log.getStackTraceString(e));
        }

        activity.startActivity(intent);
        activity.finish();
    }

    public String getQuestionString(String string) throws JSONException {
        String value = questionJson.getString(string);
        StringBuilder sb = new StringBuilder(value);
        Pattern pattern = Pattern.compile("\\((\\-?)(\\d+):(\\d+)?\\)");
        Matcher match = pattern.matcher(value);
        if(match.find()) {
            String opr = match.group(1);
            String number = match.group(2);
            int subQuestionId = 0;
            try {
                String s = match.group(3);
                subQuestionId = Integer.parseInt(s);
            } catch (NumberFormatException|IllegalStateException e) {
                Log.i("Expander", "Unable to get sub question id.  Defaulting to 0");
            }
            try {
                int answerId = Integer.parseInt(number);
                if(opr.equals("-")) {
                    answerId = realQuestionId - answerId;
                }
                int answer = getAnswer(answerId).getInt(subQuestionId);
                sb.replace(match.start(), match.end(), String.valueOf(answer));
            } catch (IOException|JSONException e) {
                try {
                    int answerId = Integer.parseInt(number);
                    if(opr.equals("-")) {
                        answerId = realQuestionId - answerId;
                    }
                    String answer = getAnswer(answerId).getString(0);
                    sb.replace(match.start(), match.end(), answer);
                } catch (IOException|JSONException e1) {
                    Log.e("Expander", "Invalid question id in " + value);
                    return "";
                }
            }
        }
        try {
            Integer.parseInt(sb.toString()); // If number then skip
            return sb.toString();
        } catch(NumberFormatException e) {
            return getStringResourceOrReturn(sb.toString());
        }
    }

    protected Spanned getRichTextQuestionString(String string) throws JSONException {
        string = string.replaceAll("\\n", "\n");
        return Html.fromHtml(getQuestionString(string));
    }

    public String getStringResourceOrReturn(String string) {
        int resourceId = activity.getResources().getIdentifier(string, "string", activity.getPackageName());
        if(resourceId>0) {
            return activity.getString(resourceId);
        } else {
            return string;
        }
    }

    protected boolean isComplete() {
        int count = 0;
        try {
            JSONArray answers = getSelectedAnswer();
            if(answers != null) {
                for (int i = 0; i < answers.length(); i++) {
                    Object answer = answers.get(i);
                    if ((answer instanceof Integer && ((Integer) answer) == -1)
                            || (answer instanceof String && ((String) answer).isEmpty())
                            || (answer instanceof Boolean && !((Boolean) answer).booleanValue()) ) {
                        continue; // Skip invalid results
                    }
                    count++;
                }
            }
        } catch (JSONException e) {
            Log.e("Expander", "Unable to get selected questions");
        }
        return count >= requiredAnswers;
    }

    public void enableDisableNext() {
        ImageView next = (ImageView) activity.findViewById(R.id.forward_button);
        if(next != null) {
            if(isComplete()) {
                next.setEnabled(true);
            } else {
                next.setEnabled(false);
            }
        } else {
            Log.d("Expander", "No next button found");
        }
    }

    public void disableDisableNext()
    {
        ImageView next = (ImageView) activity.findViewById(R.id.forward_button);
        if(next != null) {
            if(isComplete()) {
                next.setEnabled(false);
            } else {
                next.setEnabled(false);
            }
        } else {
            Log.d("Expander", "No next button found");
        }
    }

    public void forceDisableDisableNext()
    {
        ImageView next = (ImageView) activity.findViewById(R.id.forward_button);
        if(next != null) {
            next.setEnabled(false);
        } else {
            Log.d("Expander", "No next button found");
        }
    }

    public JSONArray getCachedAnswer() {
        String jsonString = prefs.getString(Keys.ANSWER_CACHE, "{}");
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray answer = json.getJSONArray(String.valueOf(definedQuestionId));
            return answer;
        } catch(JSONException e) {}
        return null;
    }

    public void setCachedAnswer(JSONArray answer) {
        String jsonString = prefs.getString(Keys.ANSWER_CACHE, "{}");
        try {
            JSONObject json = new JSONObject(jsonString);
            json.put(String.valueOf(definedQuestionId), answer);
            prefs.edit().putString(Keys.ANSWER_CACHE, json.toString()).apply();
        } catch(JSONException e) {
            Log.e("QuestionManager", "Unable to save default answer: " + definedQuestionId + " " + answer);
        }
    }

    /**
     * Calls setAnswer(answer).
     * Can be overridden to provide alternative logic for cached answers.
     */
    protected void applyCachedAnswer(JSONArray answer) throws JSONException {
        if(answer!=null) {
            setPreviousAnswer(answer);
        }
    }
}
