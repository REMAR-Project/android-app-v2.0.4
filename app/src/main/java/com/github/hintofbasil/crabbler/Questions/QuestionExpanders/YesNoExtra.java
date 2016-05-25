package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
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

        final CheckBox chkYes = (CheckBox) activity.findViewById(R.id.chk_yes);
        final CheckBox chkNo = (CheckBox) activity.findViewById(R.id.chk_no);
        final CheckBox chkMaybe = (CheckBox) activity.findViewById(R.id.chk_maybe);

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

    }

    @Override
    public String getSelectedAnswer() {
        return null;
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
