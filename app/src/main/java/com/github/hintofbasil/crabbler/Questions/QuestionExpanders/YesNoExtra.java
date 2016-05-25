package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 25/05/16.
 */
public class YesNoExtra extends Expander {

    CheckBox chkYes;
    CheckBox chkNo;
    CheckBox chkMaybe;
    EditText hiddenInput;

    public YesNoExtra(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.activity_yes_no_extra);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView hiddenDetail = (TextView) activity.findViewById(R.id.hidden_detail);

        chkYes = (CheckBox) activity.findViewById(R.id.chk_yes);
        chkNo = (CheckBox) activity.findViewById(R.id.chk_no);
        chkMaybe = (CheckBox) activity.findViewById(R.id.chk_maybe);

        hiddenInput = (EditText) activity.findViewById(R.id.hidden_input);

        imageView.setImageDrawable(getDrawable(question.getString("questionPicture")));
        titleView.setText(question.getString("questionTitle"));
        questionText.setText(question.getString("questionText"));
        hiddenDetail.setText(question.getString("hiddenDetailText"));

        chkYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkNo.setChecked(false);
                chkMaybe.setChecked(false);
                showHidden(true);
            }
        });

        chkNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkYes.setChecked(false);
                chkMaybe.setChecked(false);
                showHidden(false);
            }
        });

        chkMaybe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkYes.setChecked(false);
                chkNo.setChecked(false);
                showHidden(false);
            }
        });
    }

    @Override
    protected void setPreviousAnswer(String answer) {
        String[] answers = answer.split(";", -1);
        try {
            int i = Integer.parseInt(answers[0]);
            switch(i) {
                case 0:
                    chkYes.setChecked(true);
                    showHidden(true);
                    break;
                case 1:
                    chkNo.setChecked(true);
                    break;
                case 2:
                    chkMaybe.setChecked(true);
                    break;
            }
        } catch (NumberFormatException e) {
            Log.d("YesNoExtra", "No previous selection");
        }
        if(answers.length > 1) {
            hiddenInput.setText(answers[1]);
        } else {
            Log.d("YesNoExtra", "No previous text");
        }
    }

    @Override
    public String getSelectedAnswer() {
        StringBuilder sb = new StringBuilder();
        if(chkYes.isChecked()) {
            sb.append('0');
        } else if(chkNo.isChecked()) {
            sb.append('1');
        } else if(chkMaybe.isChecked()) {
            sb.append('2');
        }
        sb.append(';');
        sb.append(hiddenInput.getText());
        return sb.toString();
    }

    private void showHidden(boolean show) {
        LinearLayout hiddenLayout = (LinearLayout) activity.findViewById(R.id.hidden_content);
        if(show) {
            hiddenLayout.setVisibility(View.VISIBLE);
        } else {
            hiddenLayout.setVisibility(View.INVISIBLE);
        }
    }
}
