package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.ColorListAdapter;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by will on 25/05/16.
 */
public class YesNoExtraExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    CheckBox chkYes;
    CheckBox chkNo;
    CheckBox chkMaybe;
    LinearLayout hiddenInputContainer;
    ListView extraListview;
    String[] listStrings;
    int selectedListItem;
    JSONObject jsonArray = null;
    ColorListAdapter<String> adapter;
    TextView manualInfo;
    EditText manualText;

    Expander expander = this;

    public YesNoExtraExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_yes_no_extra);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        final TextView extraDetail = (TextView) activity.findViewById(R.id.extra_details);
        String state = "";

        chkYes = (CheckBox) activity.findViewById(R.id.chk_yes);
        chkNo = (CheckBox) activity.findViewById(R.id.chk_no);
        chkMaybe = (CheckBox) activity.findViewById(R.id.chk_maybe);

        hiddenInputContainer = (LinearLayout) activity.findViewById(R.id.extra_details_container);
        extraListview = (ListView) activity.findViewById(R.id.extra_listview);

        manualInfo = (TextView) activity.findViewById(R.id.manual_text);
        manualText = (EditText) activity.findViewById(R.id.manual_protected) ;

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));
        questionText.setText(getRichTextQuestionString("questionText"));
        extraDetail.setText(getRichTextQuestionString("extraDetailText"));

        chkYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkNo.setChecked(false);
                chkMaybe.setChecked(false);
                extraListview.setVisibility(View.VISIBLE);
                extraDetail.setVisibility(View.VISIBLE);
                expander.requiredAnswers = 2;
                enableDisableNext();
            }
        });

        chkNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkYes.setChecked(false);
                chkMaybe.setChecked(false);
                extraListview.setVisibility(View.GONE);
                extraDetail.setVisibility(View.GONE);
                expander.requiredAnswers = 1;
                enableDisableNext();
            }
        });

        chkMaybe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkYes.setChecked(false);
                chkNo.setChecked(false);
                hiddenInputContainer.setVisibility(View.GONE);
                expander.requiredAnswers = 1;
                enableDisableNext();
            }
        });

        try {
            jsonArray = readFileObject(getQuestionString("jsonInput"));
        } catch (IOException e) {
            Log.e("ChoiceSelectExpander", "Unable to parse json file" + Log.getStackTraceString(e));
        }

        try {
            extraListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //itemTextInput.setText(((TextView) view).getText());
                    if (adapter != null) {
                        adapter.removeDefault();
                    }
                    selectedListItem = position;
                    if(selectedListItem == listStrings.length-1) {
                        Log.d("test", "pass");
                        manualInfo.setVisibility(View.VISIBLE);
                        manualText.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("test", "fail");
                        manualInfo.setVisibility(View.GONE);
                        manualText.setVisibility(View.GONE);
                    }
                    //dontKnow.setChecked(false);
                    enableDisableNext();
                }
            });

            // Fix scrolling
            extraListview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            state = getQuestionString("state");

            JSONArray stateArray = jsonArray.getJSONArray(state);

            ArrayList<String> spinnerArray = new ArrayList<String>();

            for(int i = 0; i < stateArray.length(); i++) {
                spinnerArray.add(stateArray.getString(i));
            }

            int regionId = -1;
            listStrings = new String[stateArray.length()+1];
            for (int i = 0; i < stateArray.length(); i++) {
                String listItem = stateArray.getString(i);
                listStrings[i] = listItem;
                /*if (listItem.equals(selectedRegion)) {
                    regionId = i;
                }*/
            }
            listStrings[listStrings.length-1] = "Not in list";

            adapter = new ColorListAdapter<String>(
                    activity.getBaseContext(),
                    R.layout.list_background,
                    listStrings,
                    regionId,
                    -1);

            extraListview.setAdapter(adapter);

            setListViewHeightBasedOnChildren(extraListview, 4);

        } catch (JSONException e) {
            Log.d("YesNoExtra", "Failed to obtain a value for state, defaults to a empty string. " + e.getMessage());
        }
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            int i = answer.getInt(0);
            switch(i) {
                case 0:
                    chkYes.setChecked(true);
                    hiddenInputContainer.setVisibility(View.VISIBLE);
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
            adapter = new ColorListAdapter<String>(
                    activity.getBaseContext(),
                    R.layout.list_background,
                    listStrings,
                    answer.getInt(1),
                    -1);

            extraListview.setAdapter(adapter);
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

        int answer = -1;
        if(chkYes.isChecked()) {
            try {
                answer = selectedListItem;
            } catch (Exception e) {
                Log.d("YesNoExtra", "Failed to write selectedListItem to answer" + e.getStackTrace());
            }
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
                    hiddenInputContainer.setVisibility(View.VISIBLE);
                    expander.requiredAnswers = 2;
                    break;
                case 1:
                    chkNo.setChecked(true);
                    break;
                case 2:
                    chkMaybe.setChecked(true);
                    break;
            }
        }
    }

    private String readFile(String filename) throws IOException, JSONException {
        if(filename.endsWith(".json")) {
            filename = filename.substring(0, filename.length() - 5);
        }
        int resourceId = activity.getResources().getIdentifier(filename, "raw", activity.getPackageName());
        InputStream jsonInputStream = activity.getBaseContext().getResources().openRawResource(resourceId);
        byte[] buffer = new byte[65000];  // Large buffer required for region_countieses.json
        int length = jsonInputStream.read(buffer);
        return new String(buffer).substring(0, length);
    }

    private JSONArray readFileArray(String filename) throws IOException, JSONException {
        return new JSONArray(readFile(filename));
    }

    private JSONObject readFileObject(String filename) throws IOException, JSONException {
        return new JSONObject(readFile(filename));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, int min) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        if(listAdapter.getCount()>min)
        {
            for (int i = 0; i < min; i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }
        else
        {
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
