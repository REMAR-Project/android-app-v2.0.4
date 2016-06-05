package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 05/06/16.
 */
public class DoneExpander extends Expander {

    public DoneExpander(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.expander_done);
    }

    @Override
    protected void setPreviousAnswer(String answer) {

    }

    @Override
    public String getSelectedAnswer() {
        return null;
    }
}
