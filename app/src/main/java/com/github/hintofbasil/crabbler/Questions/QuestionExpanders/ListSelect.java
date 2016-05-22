package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 19/05/16.
 */
public class ListSelect extends Expander {

    public ListSelect(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.activity_list_select);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        ImageView detailImage = (ImageView) activity.findViewById(R.id.detail_picture);

        imageView.setImageDrawable(getDrawable(question.getString("questionPicture")));
        titleView.setText(question.getString("questionTitle"));
        detailImage.setImageDrawable(getDrawable(question.getString("detailPicture")));
    }

    @Override
    protected void setPreviousAnswer(String answer) {

    }

    @Override
    public String getSelectedAnswer() {
        return null;
    }
}
