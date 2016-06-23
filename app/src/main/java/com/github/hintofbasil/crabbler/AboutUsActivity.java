package com.github.hintofbasil.crabbler;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class AboutUsActivity extends AppCompatActivity implements AboutUsFragment.OnFragmentInteractionListener {

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
            Log.e("AboutUsActivity", "Unable to read about_us.json\n" + Log.getStackTraceString(e));
            return;
        }

        LinearLayout toolbarMenu = (LinearLayout) findViewById(R.id.toolbar_menu_button);
        toolbarMenu.setVisibility(View.GONE);

        Button menuButton = (Button) findViewById(R.id.back_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public JSONArray readJSON() throws IOException, JSONException {
        InputStream jsonInputStream = getBaseContext().getResources().openRawResource(R.raw.about_us);
        byte[] buffer = new byte[8192];
        int length = jsonInputStream.read(buffer);
        String jsonString = new String(buffer).substring(0, length);
        return new JSONArray(jsonString);
    }

    @Override
    public void onFragmentInteraction(View view) {
        View content = view.findViewById(R.id.content);
        View expandButton = view.findViewById(R.id.expand);
        View contractButton = view.findViewById(R.id.contract);
        if(content.getVisibility()==View.VISIBLE) {
            content.setVisibility(View.GONE);
            expandButton.setVisibility(View.VISIBLE);
            contractButton.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.VISIBLE);
            expandButton.setVisibility(View.GONE);
            contractButton.setVisibility(View.VISIBLE);
        }
    }
}
