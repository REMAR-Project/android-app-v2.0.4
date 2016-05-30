package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by will on 19/05/16.
 */
public class ListSelect extends Expander {

    Spinner listHolder;

    public ListSelect(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.expander_list_select);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        ImageView detailImage = (ImageView) activity.findViewById(R.id.detail_picture);
        listHolder = (Spinner) activity.findViewById(R.id.item_select);

        imageView.setImageDrawable(getDrawable(question.getString("questionPicture")));
        titleView.setText(question.getString("questionTitle"));
        detailImage.setImageDrawable(getDrawable(question.getString("detailPicture")));

        try {
            JSONArray jsonArray = readFile(question.getString("jsonInput"));
            String[] strings = new String[jsonArray.length()];
            for(int i=0; i<jsonArray.length(); i++) {
                String listItem = jsonArray.getString(i);
                strings[i] = listItem;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    activity.getBaseContext(),
                    android.R.layout.simple_list_item_1,
                    strings);
            listHolder.setAdapter(adapter);
            Log.i("ListSelect", "Successfully populated spinner");
        } catch (IOException e) {
            Log.e("ListSelect", "Failed to read file\n" + Log.getStackTraceString(e));
        }
    }

    @Override
    protected void setPreviousAnswer(String answer) {
        try {
            int position = Integer.parseInt(answer);
            listHolder.setSelection(position);
        } catch (NumberFormatException e) {
            Log.d("ListSelect", "No previous answer");
        }
    }

    @Override
    public String getSelectedAnswer() {
        int position = listHolder.getSelectedItemPosition();
        return String.valueOf(position);
    }

    private JSONArray readFile(String filename) throws IOException, JSONException {
        if(filename.endsWith(".json")) {
            filename = filename.substring(0, filename.length() - 5);
        }
        int resourceId = activity.getResources().getIdentifier(filename, "raw", activity.getPackageName());
        InputStream jsonInputStream = activity.getBaseContext().getResources().openRawResource(resourceId);
        byte[] buffer = new byte[2048];
        int length = jsonInputStream.read(buffer);
        String jsonString = new String(buffer).substring(0, length);
        return new JSONArray(jsonString);
    }
}
