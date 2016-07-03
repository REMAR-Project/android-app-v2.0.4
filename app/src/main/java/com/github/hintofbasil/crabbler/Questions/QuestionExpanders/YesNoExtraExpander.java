package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 25/05/16.
 */
public class YesNoExtraExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 2;

    CheckBox chkYes;
    CheckBox chkNo;
    CheckBox chkMaybe;
    EditText hiddenInput;

    public YesNoExtraExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_yes_no_extra);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView extraDetail = (TextView) activity.findViewById(R.id.extra_details);

        chkYes = (CheckBox) activity.findViewById(R.id.chk_yes);
        chkNo = (CheckBox) activity.findViewById(R.id.chk_no);
        chkMaybe = (CheckBox) activity.findViewById(R.id.chk_maybe);

        hiddenInput = (EditText) activity.findViewById(R.id.extra_input);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));
        questionText.setText(getRichTextQuestionString("questionText"));
        extraDetail.setText(getRichTextQuestionString("extraDetailText"));

        chkYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkNo.setChecked(false);
                chkMaybe.setChecked(false);
                enableDisableNext();
            }
        });

        chkNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkYes.setChecked(false);
                chkMaybe.setChecked(false);
                enableDisableNext();
            }
        });

        chkMaybe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkYes.setChecked(false);
                chkNo.setChecked(false);
                enableDisableNext();
            }
        });

        hiddenInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableDisableNext();
            }
        });
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            int i = answer.getInt(0);
            switch(i) {
                case 0:
                    chkYes.setChecked(true);
                    break;
                case 1:
                    chkNo.setChecked(true);
                    break;
                case 2:
                    chkMaybe.setChecked(true);
                    break;
                case -1:
                    break;
                default:
                    Log.e("YesNoExtraExpander", "Invalid previous answer");
            }
        } catch (JSONException e) {
            Log.d("YesNoExtra", "No previous selection");
        }
        try {
            hiddenInput.setText(answer.getString(1));
        } catch (JSONException e) {
            Log.d("YesNoExtra", "No previous text");
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        if(chkYes.isChecked()) {
            array.put(0);
        } else if(chkNo.isChecked()) {
            array.put(1);
        } else if(chkMaybe.isChecked()) {
            array.put(2);
        } else {
            array.put(-1);
        }
        String answer = hiddenInput.getText().toString();
        if(answer.isEmpty() && hiddenInput.getHint() != null) {
            answer = hiddenInput.getHint().toString();
        }
        array.put(answer);
        return array;
    }

    @Override
    protected void applyCachedAnswer(JSONArray answer) throws JSONException {
        if(answer.length() > 0) {
            switch (answer.getInt(0)) {
                case 0:
                    chkYes.setChecked(true);
                    break;
                case 1:
                    chkNo.setChecked(true);
                    break;
                case 2:
                    chkMaybe.setChecked(true);
                    break;
            }
        }
        if(answer.length() > 1) {
            hiddenInput.setHint(answer.getString(1));
        }
    }
}
