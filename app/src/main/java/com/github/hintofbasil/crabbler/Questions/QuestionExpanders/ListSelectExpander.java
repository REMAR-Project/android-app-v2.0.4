package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.ColorListAdapter;
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

    private static final int REQUIRED_ANSWERS = 1;

    ListView listHolder;
    EditText itemTextInput;
    String[] listStrings;

    JSONArray jsonArray = null;
    ColorListAdapter<String> adapter;

    public ListSelectExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_list_select);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        ImageView detailImage = (ImageView) activity.findViewById(R.id.detail_picture);
        listHolder = (ListView) activity.findViewById(R.id.item_select);
        itemTextInput = (EditText) activity.findViewById(R.id.item_text_input);
        TextView descriptionView = (TextView) activity.findViewById(R.id.description);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));
        descriptionView.setText(getRichTextQuestionString("description"));

        try{
            detailImage.setImageDrawable(getDrawable(getQuestionString("detailPicture")));
        } catch (JSONException e) {
            detailImage.setVisibility(View.GONE);
        }
        try {
            boolean disableCustom = Boolean.parseBoolean(getQuestionString("disableCustom"));
            if(disableCustom == true) {
                itemTextInput.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            Log.d("ListSelectExpander", "disableCustom not specified in questions.json.  Enabled by default");
        }

        listHolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemTextInput.setText(((TextView) view).getText());
                if (adapter != null) {
                    adapter.removeDefault();
                }
                enableDisableNext();
            }
        });

        // Fix scrolling
        listHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        itemTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String answer = itemTextInput.getText().toString();
                setListTo(listHolder, answer);
                enableDisableNext();
            }
        });

        try {
            jsonArray = readFileArray(getQuestionString("jsonInput"));
        } catch (JSONException e) {
            try {
                jsonArray = readFileObject(getQuestionString("jsonInput")).getJSONArray(getQuestionString("jsonKey"));
            } catch (JSONException|IOException e1) {
                Log.e("ListSelectExpander", "Unable to parse json file" + Log.getStackTraceString(e1));
            }
        } catch (IOException e) {
            Log.e("ListSelectExpander", "Unable to parse json file" + Log.getStackTraceString(e));
        }
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        String region = "";
        try {
            region = answer.getString(0);
        } catch (JSONException e) {
            Log.i("ListSelectExpander", "Unable to parse previous answer");
        }
        try {
            if (jsonArray != null) {
                populateLists(region);
                itemTextInput.setText(region);
                Log.i("ListSelectExpander", "Successfully populated list");
            } else {
                Log.e("ListSelectExpander", "No questions to load");
            }
        } catch (JSONException e) {
            Log.e("ListSelectExpander", "Unable to populate list\n" + Log.getStackTraceString(e));
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(itemTextInput.getText().toString());
        return array;
    }

    private String readFile(String filename) throws IOException, JSONException {
        if(filename.endsWith(".json")) {
            filename = filename.substring(0, filename.length() - 5);
        }
        int resourceId = activity.getResources().getIdentifier(filename, "raw", activity.getPackageName());
        InputStream jsonInputStream = activity.getBaseContext().getResources().openRawResource(resourceId);
        byte[] buffer = new byte[65000];  // Large buffer required for region_cities.json
        int length = jsonInputStream.read(buffer);
        return new String(buffer).substring(0, length);
    }

    private JSONArray readFileArray(String filename) throws IOException, JSONException {
        return new JSONArray(readFile(filename));
    }

    private JSONObject readFileObject(String filename) throws IOException, JSONException {
        return new JSONObject(readFile(filename));
    }

    private void setListTo(ListView list, String text) {
        for(int i=0;i<listStrings.length;i++) {
            if(listStrings[i].equals(text)) {
                list.setSelection(i);
                return;
            }
        }
    }

    @Override
    protected void applyCachedAnswer(JSONArray answer) throws JSONException {
        if(answer!=null && answer.length() > 0) {
            String region = answer.getString(0);
            itemTextInput.setHint(region);
            populateLists(region);
        }
    }

    private void populateLists(String selectedRegion) throws JSONException {
        int regionId = -1;
        listStrings = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            String listItem = jsonArray.getString(i);
            listStrings[i] = listItem;
            if (listItem.equals(selectedRegion)) {
                regionId = i;
            }
        }

        adapter = new ColorListAdapter<String>(
                activity.getBaseContext(),
                R.layout.list_background,
                listStrings,
                regionId);
        listHolder.setAdapter(adapter);
    }
}
