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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by will on 05/05/16.
 */
public class QuestionManager {

    private static QuestionManager questionManager = null;

    JSONArray cache;
    Context context;
    SharedPreferences answerPrefs;
    int[] loopCounter;
    static int questionFile;
    int previousFile;

    private QuestionManager(Context context) {
        this.context = context;
        questionFile = R.raw.questions;
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

    public static void changeQuestionFile(int fileID) {
        questionFile = fileID;
        try {
            questionManager.readJSON();
        } catch (Exception e) {}

    }

    public static QuestionManager get() {
        return questionManager;
    }

    private JSONArray readJSON() throws IOException, JSONException {
        if(cache == null || previousFile != questionFile) {
            InputStream jsonInputStream = context.getResources().openRawResource(questionFile);
            byte[] buffer = new byte[10240];
            int length = jsonInputStream.read(buffer);
            String jsonString = new String(buffer).substring(0, length);
            cache = new JSONArray(jsonString);
            previousFile = questionFile;
        }
        return cache;
    }

    public JSONObject getJsonQuestion(int id) throws IOException, JSONException {
        Tuple<JSONObject, Integer, JSONArray, JSONObject> container = getContainer(id, readJSON(), null);
        return container.fourth;
    }

    private int getQuestionCount(boolean withNumberOnly, JSONArray array) throws JSONException, IOException {
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
                // Check for skip
                if(object.has("jumpOn")) {
                    String[] split = object.getString("jumpOn").split("->");
                    try {
                        String[] split2 = split[0].split(":");
                        JSONArray answer = getAnswer(Integer.parseInt(split2[0]));
                        if (answer.getString(Integer.parseInt(split2[1])).equals("1")) {
                            i += Integer.parseInt(split[1]);
                            i += 1; //TODO check logic error
                        }
                    } catch(JSONException e) {}
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
        String answers = this.answerPrefs.getString(container, null);
        if(answers == null) {
            return new JSONObject();
        }
        return new JSONObject(answers);
    }

    public JSONArray getAnswer(int id) throws JSONException, IOException {
        Tuple<JSONObject, Integer, JSONArray, JSONObject> container = getContainer(id, readJSON(), null);
        JSONObject answers = getCurrentAnswers(containerToAnswerKey(container.first));
        return answers.getJSONArray(String.valueOf(container.second));
    }

    public void saveAnswer(int id, JSONArray answer) throws JSONException, IOException {
        if(answer == null) {
            Log.i("QuestionManager", "Q" + id + " No answer given.");
        }
        Tuple<JSONObject, Integer, JSONArray, JSONObject> container = getContainer(id, readJSON(), null);
        // TODO handle custom save to
        JSONObject answers = getCurrentAnswers(containerToAnswerKey(container.first));
        answers.put(String.valueOf(container.second), answer);
        // Check for expand/contract
        int questionCount = getQuestionCountInContainer(container.third);
        if((container.second+1) % questionCount == 0) {
            if(container.first != null && container.first.has("loop") && container.first.has("stopOn")) {
                String[] saveTo = container.first.getString("stopOn").split(":");
                int loopCount = (container.second + 1) / questionCount;
                int loopId = container.first.getInt("loop");
                if(answer.getString(Integer.parseInt(saveTo[0])).equals(saveTo[1])) {
                    loopCounter[loopId] = loopCount;
                    Log.i("QuestionManager", "Ending loop " + loopId);
                } else {
                    Log.i("QuestionManager", "Looping " + loopId);
                    int count = loopCounter[loopId];
                    if(count <= loopCount) {
                        loopCounter[loopId] = loopCount + 1;
                        Log.i("QuestionManager", "Extending loop to " + loopCount + 1);
                    } else {
                        Log.i("QuestionManager", "Not extending loop count. " + count + " > " + loopCount);
                    }
                }
            }
        }
        String newAnswers = answers.toString();
        answerPrefs.edit().putString(containerToAnswerKey(container.first), newAnswers).apply();
        Log.i("QuestionManager", "New answers: " + containerToAnswerKey(container.first) + " : " + newAnswers);
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
     * @return  A tuple of (parent, questionId, questionsArray).  Parent is null if parent is base array.
     * @throws JSONException
     */
    private Tuple<JSONObject, Integer, JSONArray, JSONObject> getContainer(int id, JSONArray array, JSONObject parent) throws JSONException, IOException {
        int questionId = 0;
        for(int i=0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if(object.has("loop")) {
                JSONArray subArray = object.getJSONArray("questions");
                for(int j=1; j<=loopCounter[object.getInt("loop")]; j++) {
                    Tuple<JSONObject, Integer, JSONArray, JSONObject> found = getContainer(id - (i * j), subArray, object);
                    id -= (subArray.length()); // Remove subquestions from total
                    if (found != null) {
                        int questionCount = getQuestionCountInContainer(subArray);
                        return new Tuple<JSONObject, Integer, JSONArray, JSONObject>(found.first, found.second + ((j - 1) * questionCount), subArray, null);
                    }
                }
            } else { // Must be a question
                if(questionId==id) {
                    if(parent != null) {
                        return new Tuple<JSONObject, Integer, JSONArray, JSONObject>(parent, questionId, array, object);
                    } else {
                        return new Tuple<JSONObject, Integer, JSONArray, JSONObject>(parent, questionId, array, object);
                    }
                } else {
                    // Check for skip
                    if(object.has("jumpOn")) {
                        String[] split = object.getString("jumpOn").split("->");
                        String[] split2 = split[0].split(":");
                        JSONArray answer = getAnswer(Integer.parseInt(split2[0]));
                        if(answer.getString(Integer.parseInt(split2[1])).equals("1")) {
                            id += Integer.parseInt(split[1]);
                            continue;
                        }
                    }
                    questionId++;
                }
            }
        }
        return null;
    }

    private int getQuestionCountInContainer(JSONArray array) throws JSONException {
        int count = 0;
        for(int i=0; i< array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if(obj.has("loop")) {
                continue;
            }
            count++;
        }
        return count;
    }

    private String containerToAnswerKey(JSONObject container) throws JSONException {
        if(container != null) {
            return Keys.ANSWERS_KEY + "_" + container.getInt("loop");
        } else {
            return Keys.ANSWERS_KEY + "_base";
        }
    }

    class Tuple<T, U, V, W> {

        private T first;
        private U second;
        private V third;
        private W fourth;

        public Tuple(T first, U second, V third, W fourth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }

        public V getThird() {
            return third;
        }

        public W getFourth() {
            return fourth;
        }

        @Override
        public String toString() {
            return "(" + first + "," + second + "," + third + "," + fourth + ")";
        }
    }

    public List<JSONObject> exportAnswers() throws JSONException, IOException {
        List<JSONObject> lst = new ArrayList<JSONObject>();
        String baseAnswersString = answerPrefs.getString(containerToAnswerKey(null), null);
        JSONObject baseAnswers = new JSONObject(baseAnswersString);
        lst.add(baseAnswers);
        for(JSONObject loop : getLoops(readJSON())) {
            List<JSONObject> newLst = new ArrayList<JSONObject>();
            int loopLength = getQuestionCountInContainer(loop.getJSONArray("questions"));
            int loopCount = loopCounter[loop.getInt("loop")];
            String answersKey = containerToAnswerKey(loop);
            JSONObject answers = new JSONObject(answerPrefs.getString(answersKey, null));
            for(JSONObject obj : lst) {
                for(int i = 0; i < loopCount; i++) {
                    // TODO get position
                    JSONObject newObj = duplicateJSONObject(obj, 0, loopLength);
                    for (int j = 0; j < loopLength; j++) {
                        newObj.put(String.valueOf(j), answers.get(String.valueOf((i*loopLength) + j)));
                    }
                    newLst.add(newObj);
                }
                answerPrefs.edit().remove(answersKey).apply();
            }
            lst = newLst;
        }
        answerPrefs.edit().remove(containerToAnswerKey(null)).apply();
        init(); // Reinitialise to reset question loops
        return lst;
    }

    private JSONObject duplicateJSONObject(JSONObject old, int position, int offset) throws JSONException {
        JSONObject newObj = new JSONObject();
        Iterator<String> itr = old.keys();
        while(itr.hasNext()) {
            String key = itr.next();
            Object value = old.get(key);
            int intKey = Integer.parseInt(key);
            if(intKey >= position) {
                key = String.valueOf(intKey + offset);
            }
            newObj.put(key, value);
        }
        return newObj;
    }

    private List<JSONObject> getLoops(JSONArray array) throws IOException, JSONException {
        List<JSONObject> lst = new ArrayList<JSONObject>();
        for(int i=0; i<array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if(obj.has("loop")) {
                lst.add(obj);
                List<JSONObject> subarray = getLoops(obj.getJSONArray("questions"));
                for(JSONObject o : subarray) {
                    lst.add(o);
                }
            }
        }
        return lst;
    }
}
