package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.Questions.QuestionManager;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by will on 13/05/16.
 */
public class ModeChooseExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    LinearLayout choiceOneButton;
    LinearLayout choiceTwoButton;
    TextView choiceOneTitle;
    TextView choiceTwoTitle;
    TextView choiceOneTitleTwo;
    TextView choiceTwoTitleTwo;
    boolean allowMultiple = false;
    int chosenMode;

    public ModeChooseExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_modechoose);
        //ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        //TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView description = (TextView) activity.findViewById(R.id.question_text);
        choiceOneButton = (LinearLayout) activity.findViewById(R.id.choice_one);
        choiceTwoButton = (LinearLayout) activity.findViewById(R.id.choice_two);
        choiceOneTitle = (TextView) activity.findViewById(R.id.choice_one_title);
        choiceTwoTitle = (TextView) activity.findViewById(R.id.choice_two_title);
        choiceOneTitleTwo = (TextView) activity.findViewById(R.id.choice_one_title_two);
        choiceTwoTitleTwo = (TextView) activity.findViewById(R.id.choice_two_title_two);
        //imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        //titleView.setText(getRichTextQuestionString("questionTitle"));
        description.setText(getRichTextQuestionString("description"));
        choiceOneTitle.setText(getRichTextQuestionString("choiceOneText"));
        choiceTwoTitle.setText(getRichTextQuestionString("choiceTwoText"));
        choiceOneTitleTwo.setText(getRichTextQuestionString("choiceOneTextTwo"));
        choiceTwoTitleTwo.setText(getRichTextQuestionString("choiceTwoTextTwo"));

        chosenMode = -1;

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
                QuestionManager.changeQuestionFile(R.raw.questions2);
                chosenMode = 0;
                choiceTwoButton.setBackgroundResource(R.color.questionBackground);
                choiceOneButton.setBackgroundResource(R.color.questionSelectedBackground);
                nextQuestion(0);
            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionManager.changeQuestionFile(R.raw.questions);
                chosenMode = 1;
                choiceOneButton.setBackgroundResource(R.color.questionBackground);
                choiceTwoButton.setBackgroundResource(R.color.questionSelectedBackground);
                nextQuestion(0);
            }
        });
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {

    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(chosenMode);
        return array;
    }
}
