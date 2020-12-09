package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostFactory;
import com.github.hintofbasil.crabbler.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by will on 05/06/16.
 */
public class DoneExpander extends Expander {

    public static final int SEND_DELAY = 4000;

    public DoneExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, 0);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_done);

        Float density = activity.getBaseContext().getResources().getDisplayMetrics().density;
        if (density < 3)
        {
            TextView t1 = activity.findViewById(R.id.text1_done);
            TextView t2 = activity.findViewById(R.id.text2_done);
            t1.setTextSize(18);
            t2.setTextSize(18);
        }

        //ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        //TextView titleView = (TextView) activity.findViewById(R.id.title);

        //imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        //titleView.setText(getRichTextQuestionString("questionTitle"));

        new CountDownTimer(SEND_DELAY, SEND_DELAY) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                toFirstQuestion();

            }
        }.start();

        postAnswers();

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

            /*Toast.makeText(activity.getBaseContext(),
                    activity.getString(R.string.thank_you_toast),
                    Toast.LENGTH_LONG).show();*/

        View v = activity.findViewById(R.id.doneScrollView);
        Snackbar snackBar = Snackbar.make(v, activity.getString(R.string.thank_you_toast), 3000);
        snackBar.show();

    }

    private void toFirstQuestion() {
        /*Intent intent = new Intent(activity.getBaseContext(),
                ReturnExpander.class);
        activity.startActivity(intent);
        activity.finish();*/
        nextQuestion(0, 1);
    }
}
