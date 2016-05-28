package com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by will on 23/05/16.
 */
public abstract class PostHelper {

    protected String responseData;
    private URL url;

    public PostHelper(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            Log.e("RegisterHelper", "Invalid URL: " + url);
        }
    }

    public void post() throws IOException {
        if(url == null) {
            // Basic assert
            Log.e("PostHelper", "No URL set");
            return;
        }
        String data = getData();
        if(data == null) {
            Log.d("PostHelper", "Data is null");
            return;
        }
        String response = doPost(getData());
        if(response!=null) {
            Log.i("PostHelper", "Posted: " + url);
            responseData = response;
        }
    }

    protected String doPost(String content) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        OutputStream out = new BufferedOutputStream(connection.getOutputStream());
        out.write(content.getBytes("UTF-8"));
        out.flush();

        //TODO handle 409
        if (connection.getResponseCode() == 200) {
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            byte[] bytes = new byte[1024];
            int length = inputStream.read(bytes);
            return new String(bytes, "UTF-8").substring(0, length);
        } else {
            Log.d("PostHelper", "Invalid response: " + connection.getResponseMessage());
        }
        return null;
    }

    /**
     *  Should be overridden to get data to post
     *
     * @return data to post to url
     */
    protected abstract String getData();

    /**
     *
     * Should be overridden to validate responseData
     *
     * @return true on valid response otherwise false
     */
    public abstract boolean successful();

}
