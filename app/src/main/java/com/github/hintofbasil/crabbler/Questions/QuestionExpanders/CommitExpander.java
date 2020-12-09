package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.GlobalVariables;
import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by will on 13/05/16.
 */
public class CommitExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    Button choiceOneButton;
    Button choiceTwoButton;
    Button choiceThreeButton;
    boolean allowMultiple = false;

    public CommitExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_commit);
        //ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        //TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView description = (TextView) activity.findViewById(R.id.description);
        choiceOneButton = (Button) activity.findViewById(R.id.choice_one);
        choiceTwoButton = (Button) activity.findViewById(R.id.choice_two);
        choiceThreeButton = (Button) activity.findViewById(R.id.choice_three);

        //imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        //titleView.setText(getRichTextQuestionString("questionTitle"));
        description.setText(getRichTextQuestionString("description"));
        description.setMovementMethod(LinkMovementMethod.getInstance());
        choiceOneButton.setText(getRichTextQuestionString("choiceOneText"));
        choiceTwoButton.setText(getRichTextQuestionString("choiceTwoText"));

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
                GlobalVariables.appTest = false;
                nextQuestion(1, 1);
            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
            previousQuestion();
            }
        });

        choiceThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.appTest = true;
                AlertDialog.Builder appTestAlertDialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);

                appTestAlertDialog.setTitle(R.string.testWarning);
                appTestAlertDialog.setMessage(R.string.testWarningContent);
                appTestAlertDialog.setPositiveButton(R.string.testWarningAccept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nextQuestion(1, 2);
                            };
                        });
                appTestAlertDialog.setNegativeButton(R.string.testWarningBack, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            };
                        });

                appTestAlertDialog.create();
                appTestAlertDialog.show();

            }
        });
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {

    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        array.put(simpleDateFormat.format(new Date()));
        return array;
    }
}
