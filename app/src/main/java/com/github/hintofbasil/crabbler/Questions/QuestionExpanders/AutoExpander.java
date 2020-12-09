package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 05/05/16.
 */
public class AutoExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    private int currentAnswer = -1;
    RelativeLayout questionOneButton;
    private String version;

    public AutoExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_auto);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView choiceOneTitle = (TextView) activity.findViewById(R.id.choice_one_title);
        ImageView previousButton = (ImageView) activity.findViewById(R.id.back_button);
        ImageView nextButton = (ImageView) activity.findViewById(R.id.forward_button);
        TextView pageNumber = (TextView) activity.findViewById(R.id.page_of);
        questionOneButton = (RelativeLayout) activity.findViewById(R.id.question_one_button);
        ImageView choiceOneImage = (ImageView) activity.findViewById(R.id.choice_one_image);
        TextView backText = (TextView) activity.findViewById(R.id.backText);
        TextView forwardText = (TextView) activity.findViewById(R.id.forwardText);

        backText.setText("");
        forwardText.setText("");
        version = "";

        //choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicture")));

        //questionText.setText(getRichTextQuestionString("questionText"));
        //questionText.setMovementMethod(new ScrollingMovementMethod());

        //choiceOneTitle.setText(getRichTextQuestionString("choiceOneTitle"));
        version = getQuestionString("version");

        previousButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        pageNumber.setVisibility(View.GONE);

        nextQuestion(4000, 1);

        }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            Integer i = answer.getInt(0);
            switch(i) {
                case 0:
                    questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);
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
        array.put(version);
        return array;
    }
}
