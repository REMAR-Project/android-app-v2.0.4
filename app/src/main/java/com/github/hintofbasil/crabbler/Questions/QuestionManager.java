package com.github.hintofbasil.crabbler.Questions;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by will on 05/05/16.
 */
public class QuestionManager {

    private static QuestionManager questionManager = null;

    JSONArray cache;
    Context context;
    SharedPreferences answerPrefs;
    int[] loopCounter;

    private QuestionManager(Context context) {
        this.context = context;
        answerPrefs = context.getSharedPreferences(Keys.SAVED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static void init(Context context) throws IOException, JSONException {
        questionManager = new QuestionManager(context);
        questionManager.init();
    }

    public void init() throws IOException, JSONException {
        loopCounter = new int[getNumberOfLoops()];
        for(int i=0; i< loopCounter.length; i++) {
            loopCounter[i] = 1;
        }
    }

    public static QuestionManager get() {
        return questionManager;
    }

    private JSONArray readJSON() throws IOException, JSONException {
        if(cache == null) {
            InputStream jsonInputStream = context.getResources().openRawResource(R.raw.questions);
            byte[] buffer = new byte[4096];
            int length = jsonInputStream.read(buffer);
            String jsonString = new String(buffer).substring(0, length);
            cache = new JSONArray(jsonString);
        }
        return cache;
    }

    private JSONObject findQuestionInArray(int id, JSONArray array) throws JSONException, IOException {
        int manipulatableId = id;
        for(int i=0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if(object.has("loop")) {
                JSONArray subArray = object.getJSONArray("questions");
                for(int j=1; j<=loopCounter[object.getInt("loop")]; j++) {
                    JSONObject subSearch = findQuestionInArray(manipulatableId - (i * j), subArray);
                    manipulatableId -= (subArray.length()); // Remove subquestions from total
                    if (subSearch != null) {
                        return subSearch;
                    }
                }
                manipulatableId += 1; // Need as loop is an item but not a question
            } else { // Must be a question
                if(i==manipulatableId) {
                    return object;
                }
            }
        }
        return null;
    }

    public JSONObject getJsonQuestion(int id) throws IOException, JSONException {
        return findQuestionInArray(id, readJSON());
    }

    private int getQuestionCount(boolean withNumberOnly, JSONArray array) throws JSONException {
        int count = 0, loopId = 0;
        for(int i=0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if(object.has("loop")) {
                JSONArray subArray = object.getJSONArray("questions");
                count += getQuestionCount(withNumberOnly, subArray) * loopCounter[loopId];
            } else { // Must be a question
                if(!withNumberOnly || object.has("questionNumber")) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getQuestionCount() throws IOException, JSONException {
        return getQuestionCount(true, readJSON());
    }

    public int getRealQuestionCount() throws IOException, JSONException {
        return getQuestionCount(false, readJSON());
    }

    private JSONObject getCurrentAnswers(String container) throws JSONException {
        String answers = this.answerPrefs.getString(container + Keys.ANSWERS_KEY, null);
        if(answers == null) {
            return new JSONObject();
        }
        return new JSONObject(answers);
    }

    public JSONArray getAnswer(int id) throws JSONException, IOException {
        Tuple<String, Integer> container = getContainer(id, readJSON(), null);
        JSONObject answers = getCurrentAnswers(container.first);
        return answers.getJSONArray(String.valueOf(container.second));
    }

    public void saveAnswer(int id, JSONArray answer) throws JSONException, IOException {
        if(answer == null) {
            Log.i("QuestionManager", "Q" + id + " No answer given.");
        }
        Tuple<String, Integer> container = getContainer(id, readJSON(), null);
        JSONObject answers = getCurrentAnswers(container.first);
        answers.put(String.valueOf(container.second), answer);
        String newAnswers = answers.toString();
        answerPrefs.edit().putString(container.first + Keys.ANSWERS_KEY, newAnswers).apply();
        Log.i("QuestionManager", "New answers: " + container.first + Keys.ANSWERS_KEY + " : "  + newAnswers);
    }

    private int getNumberOfLoops() throws IOException, JSONException {
        int count = 0;
        JSONArray array = readJSON();
        for(int i=0; i< array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if(obj.has("loop")) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the name of the container ("loop#" or "base") and the question number in that container
     *
     * @param id  The global id of the question
     * @param array  The base array.  Should always be readJson()
     * @param parent  Used for recursion.  Should always be null on first call.
     * @return  A tuple of (containerName, questionId)
     * @throws JSONException
     */
    private Tuple<String, Integer> getContainer(int id, JSONArray array, JSONObject parent) throws JSONException {
        int questionId = 0;
        for(int i=0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if(object.has("loop")) {
                JSONArray subArray = object.getJSONArray("questions");
                for(int j=1; j<=loopCounter[object.getInt("loop")]; j++) {
                    Tuple<String, Integer> found = getContainer(id - (i * j), subArray, object);
                    id -= (subArray.length()); // Remove subquestions from total
                    if (found != null) {
                        return new Tuple<String, Integer>(found.first, found.second + ((j - 1) * subArray.length()));
                    }
                }
                id += 1; // Need as loop is an item but not a question
            } else { // Must be a question
                if(i==id) {
                    if(parent != null) {
                        return new Tuple<String, Integer>("loop" + parent.getInt("loop"), questionId);
                    } else {
                        return new Tuple<String, Integer>("base", questionId);
                    }
                } else {
                    questionId++;
                }
            }
        }
        return null;
    }

    class Tuple<T, U> {

        private T first;
        private U second;

        public Tuple(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "(" + first + "," + second + ")";
        }
    }
}
