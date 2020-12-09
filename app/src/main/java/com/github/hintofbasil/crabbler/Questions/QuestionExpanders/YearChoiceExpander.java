package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.ColorListAdapter;
import com.github.hintofbasil.crabbler.GlobalVariables;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class YearChoiceExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;
    public static final int MIN_YEAR = 2016;

    ListView yearListView;

    int monthNo = -1;
    int yearNo = -1;

    ColorListAdapter<String> yearsAdapter;

    int currentYear;
    int currentMonth;

    public YearChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_year_choice);

        final ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView questionView = (TextView) activity.findViewById(R.id.question_text);
        yearListView = (ListView) activity.findViewById(R.id.year_list_view);

        titleView.setText(getRichTextQuestionString("questionTitle"));
        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        questionView.setText(getRichTextQuestionString("questionText"));

        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

        yearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if((position) > currentYear - 2016)
                {
                    disableDisableNext();
                    return;
                }
                yearNo = position;
                //month2ListView.setItemChecked(monthNo, true);
                // Colour is set as background on first selected.  Must override
                if(yearsAdapter != null) {
                    yearsAdapter.removeDefault();
                }
                enableDisableNext();
                //yearListView.setSelection(yearNo);
            }
        });

        yearsAdapter = new ColorListAdapter<String>(
                activity.getBaseContext(),
                R.layout.list_background,
                activity.getResources().getStringArray(R.array.years),
                yearNo,
                currentYear - 2016);
        yearListView.setAdapter(yearsAdapter);

        // Fix scrolling
        yearListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        /*try {
            switch (answer.getInt(0)) {
                case -1: // Indicates not filled in yet
                    break;
                default:
                    Log.d("TwoChoiceDate", "Invalid previous answer");
                    break;
            }
        } catch (JSONException e) {
            Log.i("TwoChoiceDateExpander", "Unable to parse answer (0)");
        }*/

        try {
            yearNo = answer.getInt(0);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|JSONException e) {
            Log.d("TwoChoiceDate", "No previous month");
        }

        /*yearsAdapter = new ColorListAdapter<String>(
                activity.getBaseContext(),
                R.layout.list_background,
                activity.getResources().getStringArray(R.array.years),
                yearNo,
                currentYear - 2016);
        yearListView.setAdapter(yearsAdapter);*/
        yearListView.setItemChecked(yearNo, true);
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(yearNo);
        return array;
    }
}
