package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
public class ListSelectExpander extends Expander {

    Spinner listHolder;
    EditText itemTextInput;

    public ListSelectExpander(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.expander_list_select);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        ImageView detailImage = (ImageView) activity.findViewById(R.id.detail_picture);
        listHolder = (Spinner) activity.findViewById(R.id.item_select);
        itemTextInput = (EditText) activity.findViewById(R.id.item_text_input);

        imageView.setImageDrawable(getDrawable(question.getString("questionPicture")));
        titleView.setText(question.getString("questionTitle"));
        try{
            detailImage.setImageDrawable(getDrawable(question.getString("detailPicture")));
        } catch (JSONException e) {
            detailImage.setVisibility(View.GONE);
        }
        try {
            boolean disableCustom = question.getBoolean("disableCustom");
            if(disableCustom == true) {
                itemTextInput.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            Log.d("ListSelectExpander", "disableCustom not specified in questions.json.  Enabled by default");
        }

        JSONArray jsonArray = null;

        try {
            jsonArray = readFileArray(question.getString("jsonInput"));
        } catch (JSONException e) {
            try {
                jsonArray = readFileObject(question.getString("jsonInput")).getJSONArray(question.getString("jsonKey"));
            } catch (JSONException|IOException e1) {
                Log.e("ListSelectExpander", "Unable to parse json file" + Log.getStackTraceString(e1));
            }
        } catch (IOException e) {
            Log.e("ListSelectExpander", "Unable to parse json file" + Log.getStackTraceString(e));
        }

        if(jsonArray != null) {
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
            Log.i("ListSelectExpander", "Successfully populated spinner");
        } else {
            Log.e("ListSelectExpander", "No questions to load");
        }
    }

    @Override
    protected void setPreviousAnswer(String answer) {
        try {
            int position = Integer.parseInt(answer);
            listHolder.setSelection(position);
        } catch (NumberFormatException e) {
            Log.d("ListSelectExpander", "No previous answer");
        }
    }

    @Override
    public String getSelectedAnswer() {
        int position = listHolder.getSelectedItemPosition();
        return String.valueOf(position);
    }

    private String readFile(String filename) throws IOException, JSONException {
        if(filename.endsWith(".json")) {
            filename = filename.substring(0, filename.length() - 5);
        }
        int resourceId = activity.getResources().getIdentifier(filename, "raw", activity.getPackageName());
        InputStream jsonInputStream = activity.getBaseContext().getResources().openRawResource(resourceId);
        byte[] buffer = new byte[2048];
        int length = jsonInputStream.read(buffer);
        return new String(buffer).substring(0, length);
    }

    private JSONArray readFileArray(String filename) throws IOException, JSONException {
        return new JSONArray(readFile(filename));
    }

    private JSONObject readFileObject(String filename) throws IOException, JSONException {
        return new JSONObject(readFile(filename));
    }
}
