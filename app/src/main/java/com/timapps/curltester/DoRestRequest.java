package com.timapps.curltester;

import android.os.AsyncTask;
import android.widget.EditText;
import android.app.Activity;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tim on 10/10/14.
 */
public class DoRestRequest extends AsyncTask<URL, Integer, String> {
    protected String doInBackground(URL... urls) {
        String output = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) urls[0]
                    .openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            OutputStreamWriter writer = new OutputStreamWriter(
                    connection.getOutputStream());

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                output = "ok";
            } else {
                output = "fail";
            }
        } catch (IOException ex) {
            output = "ioexception";
        }


        return output;
    }


}
