package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.AboutUsActivity;
import com.github.hintofbasil.crabbler.Questions.QuestionManager;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    LinearLayout privacyLink;
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
        privacyLink = (LinearLayout) activity.findViewById(R.id.privacyLink);

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
                /*choiceTwoButton.setBackgroundResource(R.drawable.background_rounded_green);
                choiceOneButton.setBackgroundResource(R.drawable.background_rounded_yellow);*/
                choiceOneButton.setSelected(true);
                choiceTwoButton.setSelected(false);
                nextQuestion(0, 1);
            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionManager.changeQuestionFile(R.raw.questions);
                chosenMode = 1;
                choiceOneButton.setSelected(false);
                choiceTwoButton.setSelected(true);
                /*choiceOneButton.setBackgroundResource(R.drawable.background_rounded_green);
                choiceTwoButton.setBackgroundResource(R.drawable.background_rounded_yellow);*/
                nextQuestion(0, 1);
            }
        });

        privacyLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder privacyPolicyDialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
                privacyPolicyDialog.setTitle(R.string.privacyPolicy);
                privacyPolicyDialog.setMessage(R.string.privacyPolicyContent);
                privacyPolicyDialog.setPositiveButton(android.R.string.ok, null);
                privacyPolicyDialog.create();
                privacyPolicyDialog.show();
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

    public void menuClick(View view) {
        Log.i("ModeChooseExpander", "Launching about us");
        Intent intent = new Intent(activity.getBaseContext(),
                AboutUsActivity.class);
        activity.startActivity(intent);
    }
}
