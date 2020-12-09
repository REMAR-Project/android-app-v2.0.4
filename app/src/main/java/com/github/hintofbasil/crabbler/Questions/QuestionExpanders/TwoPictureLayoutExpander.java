package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 05/05/16.
 */
public class TwoPictureLayoutExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    private int currentAnswer = -1;
    LinearLayout questionOneButton;
    LinearLayout questionTwoButton;
    ImageView choiceOneImage;
    ImageView choiceTwoImage;

    public TwoPictureLayoutExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_two_picture_choice);
        final TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView choiceOneTitle = (TextView) activity.findViewById(R.id.choice_one_title);
        TextView choiceTwoTitle = (TextView) activity.findViewById(R.id.choice_two_title);
        questionOneButton = (LinearLayout) activity.findViewById(R.id.question_one_button);
        questionTwoButton = (LinearLayout) activity.findViewById(R.id.question_two_button);
        choiceOneImage = (ImageView) activity.findViewById(R.id.choice_one_image);
        choiceTwoImage = (ImageView) activity.findViewById(R.id.choice_two_image);
        TextView backText = (TextView) activity.findViewById(R.id.backText);
        TextView forwardText = (TextView) activity.findViewById(R.id.forwardText);

        choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicture")));
        choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicture")));

        questionText.setText(getRichTextQuestionString("questionText"));
        questionText.setMovementMethod(new ScrollingMovementMethod());

        /* This piece of code below makes sure that the crab species texts have the same size
        *
        *  The current design uses an AutoFitTextView and it is not possible to return the modified
        *  text size programmatically, this will only return the default text size
        *  I had to get a little creative here and calculate the length difference between the two strings
        *  then add the relevant blank spaces to the string so both strings appear to be the same size
         */

        String title1 = "", title2 = "";

        if (getRichTextQuestionString("choiceOneTitle").length() < getRichTextQuestionString("choiceTwoTitle").length())
        {
            int diff = getRichTextQuestionString("choiceTwoTitle").length() - getRichTextQuestionString("choiceOneTitle").length();
            // If diff is not even, add a space to the longest string so it has the same text size than the shortest string with added spaces
            if(diff%2!=0){
                diff++;
                title2 += getRichTextQuestionString("choiceTwoTitle") + " ";
            }
            else
            {
                title2 += getRichTextQuestionString("choiceTwoTitle");
            }

            // Add spaces to each side of the shortest string
            for (int i=0; i<(diff/2)+1; i++)
            {
                title1 += " ";
            }
            title1 += getRichTextQuestionString("choiceOneTitle");
            for (int i=0; i<(diff/2)+1; i++)
            {
                title1 += " ";
            }
        }
        else
        {
            int diff = getRichTextQuestionString("choiceOneTitle").length() - getRichTextQuestionString("choiceTwoTitle").length();
            // If diff is not even, add a space to the longest string so it has the same text size than the shortest string with added spaces
            if(diff%2!=0){
                diff++;
                title1 += getRichTextQuestionString("choiceOneTitle") + " ";
            }
            else
            {
                title1 += getRichTextQuestionString("choiceOneTitle");
            }

            // Add spaces to each side of the shortest string
            for (int i=0; i<(diff/2)+1; i++)
            {
                title2 += " ";
            }
            title2 += getRichTextQuestionString("choiceTwoTitle");
            for (int i=0; i<(diff/2)+1; i++)
            {
                title2 += " ";
            }
        }

        /* End of text resize, we can set the text then */

        choiceOneTitle.setText(title1);
        choiceTwoTitle.setText(title2);

        Log.d("TwoPictureLayout", "t1:" + title1 + " / t2: " + title2);


        backText.setText(getRichTextQuestionString("backText"));
        forwardText.setText(getRichTextQuestionString("forwardText"));

        questionOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAnswer = 0;
                /*questionTwoButton.setBackgroundResource(R.color.questionBackground);
                questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);*/
                questionOneButton.setSelected(true);
                questionTwoButton.setSelected(false);
                try
                {
                    choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicTwo")));
                    choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicture")));
                } catch (JSONException e) {}

                enableDisableNext();
                nextQuestion(0, 1);
            }
        });

        questionTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAnswer = 1;
                /*questionOneButton.setBackgroundResource(R.color.questionBackground);
                questionTwoButton.setBackgroundResource(R.color.questionSelectedBackground);*/
                questionOneButton.setSelected(false);
                questionTwoButton.setSelected(true);
                try
                {
                    choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicture")));
                    choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicTwo")));
                } catch (JSONException e) {}

                enableDisableNext();
                nextQuestion(0, 1);
            }
        });    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            Integer i = answer.getInt(0);
            switch(i) {
                case 0:
                    //questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);
                    questionOneButton.setSelected(true);
                    choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicTwo")));
                    break;
                case 1:
                    //questionTwoButton.setBackgroundResource(R.color.questionSelectedBackground);
                    questionTwoButton.setSelected(true);
                    choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicTwo")));
                    break;
                default:
                    Log.d("TwoPictureLayoutExpande", "Invalid previous answer");
            }
            currentAnswer = i;
        } catch (JSONException e) {
            Log.d("QuestionActivity", "No previous answer");
            return;
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(currentAnswer);
        return array;
    }
}
