package com.timapps.curltester;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    public void makeRequest(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        try {
            EditText urlBox = (EditText) findViewById(R.id.editText);
            URL url = new URL(urlBox.getText().toString());

            new AsyncTask<URL, Integer, String>() {
                protected String doInBackground(URL... urls) {
                    String output = "";
                    EditText contentBox = (EditText) findViewById(R.id.editText2);

                    try {
                        HttpURLConnection connection = (HttpURLConnection) urls[0]
                                .openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        OutputStream os = connection.getOutputStream();
                        os.write(contentBox.getText().toString().getBytes());

                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream stream = connection.getInputStream();
                            InputStreamReader isReader = new InputStreamReader(stream);
                            BufferedReader br = new BufferedReader(isReader);
                            output = br.readLine();
                        } else {
                            InputStream inputStream = connection.getErrorStream();
                            Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
                            output = "Received error response code from endpoint: " +
                                    new Integer(connection.getResponseCode()).toString() +
                                    " error body: " + s.next();
                        }
                    } catch (IOException ex) {
                        output = "Unable to connect to the URL: " + ex.toString();
                    }

                    return output;
                }

                protected void onPostExecute(String result) {
                    setResponseOutputText(result);
                }
            }.execute(url);
        } catch (MalformedURLException ex) {
            setResponseOutputText("Malformed URL");
        }
    }

    public void setResponseOutputText(String outputText) {
        EditText editText = (EditText) findViewById(R.id.editText3);
        editText.setText(outputText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_quit) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
