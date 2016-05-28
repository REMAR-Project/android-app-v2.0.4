package com.github.hintofbasil.crabbler;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        try {
            JSONArray data = readJSON();
            FragmentManager fm = getFragmentManager();
            for(int i=0;i<data.length();i++) {
                JSONObject dataObject = data.getJSONObject(i);
                String title = dataObject.getString("title");
                String content = dataObject.getString("content");
                fm.beginTransaction().add(R.id.fragment_container,
                        AboutUsFragment.newInstance(title, content),
                        AboutUsFragment.FRAGMENT_TAG)
                        .commit();
            }
        } catch (IOException|JSONException e) {
            Log.e("AboutUs", "Unable to read about_us.json\n" + Log.getStackTraceString(e));
            return;
        }
    }

    public JSONArray readJSON() throws IOException, JSONException {
        InputStream jsonInputStream = getBaseContext().getResources().openRawResource(R.raw.about_us);
        byte[] buffer = new byte[8192];
        int length = jsonInputStream.read(buffer);
        String jsonString = new String(buffer).substring(0, length);
        return new JSONArray(jsonString);
    }
}
