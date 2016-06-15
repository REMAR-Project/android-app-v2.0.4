package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostFactory;
import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 05/06/16.
 */
public class DoneExpander extends Expander {

    public DoneExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_done);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getQuestionString("questionTitle"));

    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {

    }

    @Override
    public JSONArray getSelectedAnswer() {
        return null;
    }

    private void postAnswers() {
        DataPostFactory dataPostFactory = new DataPostFactory(activity.getBaseContext());
        dataPostFactory.submitAnswers();

        Toast.makeText(activity.getBaseContext(),
                activity.getString(R.string.thank_you_toast),
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(activity.getBaseContext(),
                QuestionActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
