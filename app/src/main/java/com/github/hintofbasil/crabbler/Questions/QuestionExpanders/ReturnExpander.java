package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 13/05/16.
 */
public class ReturnExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    TextView description;
    Button choiceOneButton;
    Button choiceTwoButton;
    boolean allowMultiple = false;

    public ReturnExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_return);
        //ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        //TextView titleView = (TextView) activity.findViewById(R.id.title);
        description = (TextView) activity.findViewById(R.id.description);
        choiceOneButton = (Button) activity.findViewById(R.id.choice_one);
        choiceTwoButton = (Button) activity.findViewById(R.id.choice_two);

        //imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        //titleView.setText(getRichTextQuestionString("questionTitle"));
        description.setText(getRichTextQuestionString("description"));
        description.setMovementMethod(LinkMovementMethod.getInstance());
        choiceOneButton.setText(getRichTextQuestionString("choiceOneText"));
        choiceTwoButton.setText(getRichTextQuestionString("choiceTwoText"));
        TextView t3 = activity.findViewById(R.id.doneContact);
        t3.setMovementMethod(LinkMovementMethod.getInstance());

        try {
            Boolean addImage = Boolean.parseBoolean(getQuestionString("hasImage"));

            if(addImage){
                try {
                    //imageView.setImageDrawable(getDrawable(getQuestionString("imageFile")));
                } catch (Exception e) {
                    Log.d("TwoChoice", "Failed to load image from imageFile, or could not find imagefile" + e.getStackTrace());
                }
            }
        } catch (Exception e) {
            Log.d("TwoChoice", "Could not find hasImage" + e.getStackTrace());
        }

        try {
            allowMultiple = questionJson.getBoolean("allowMultiple");
        } catch (JSONException e) {}

        choiceOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                activity.finish();
            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFirstQuestion();
            }
        });
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {

    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        //array.put(DateFormat.getDateTimeInstance().format(new Date()).toString());
        return array;
    }

    private void toFirstQuestion() {
        SharedPreferences answerPrefs = activity.getSharedPreferences(Keys.SAVED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        answerPrefs.edit().clear().apply();
        Intent intent = new Intent(activity.getBaseContext(),
                QuestionActivity.class);
        intent.putExtra(Keys.QUESTION_ID_KEY, 1);
        activity.startActivity(intent);
        activity.finish();
    }
}
