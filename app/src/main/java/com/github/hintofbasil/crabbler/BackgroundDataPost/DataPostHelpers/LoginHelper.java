package com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by will on 23/05/16.
 */
public class LoginHelper extends PostHelper {

    private String urlString = "http://crab.napier.ac.uk/api/0.1/users";

    public LoginHelper() {
        try {
            this.url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e("LoginHelper", "Invalid URL: " + urlString);
        }
    }

    @Override
    protected String getData() {
        return "{\"phone\":{\"phone_id\":\"1001\",\"role\":\"fisherman\"}}";
    }

    @Override
    public boolean successful() {
        return false;
    }
}
