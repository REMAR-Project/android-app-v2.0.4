package com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by will on 23/05/16.
 */
public abstract class PostHelper {

    protected URL url;
    private String responseData;

    public void post() throws IOException {
        if(url == null) {
            // Basic assert
            Log.e("DataPostProcessService", "No URL set");
            return;
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        OutputStream out = new BufferedOutputStream(connection.getOutputStream());
        out.write(getData().getBytes("UTF-8"));
        out.flush();

        //TODO handle 409
        if (connection.getResponseCode() == 200) {
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            byte[] bytes = new byte[1024];
            int length = inputStream.read(bytes);
            responseData = new String(bytes, "UTF-8").substring(0, length);
            Log.i("DataPostProcessService", "Posted: " + url);
            amendData();
        }
    }

    /**
     *  Should be overridden to get data to post
     *
     * @return data to post to url
     */
    protected abstract String getData();

    /**
     * Can be overwritten to edit data after post()
     */
    protected void amendData() throws IOException {

    }

    /**
     *
     * Should be overridden to validate responseData
     *
     * @return true on valid response otherwise false
     */
    public abstract boolean successful();

}
