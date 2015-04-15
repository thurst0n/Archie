package edu.dtlevyiastate.jsontest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by David on 3/29/2015.
 */
public class FetchUserData extends AsyncTask<String, Void, ArrayList<RowItem>> {

    //Set Log Tag to class Name: FetchUserData
    private final String LOG_TAG = FetchUserData.class.getSimpleName();






    @Override
    protected ArrayList<RowItem> doInBackground(String... params) {

        // Check for parameters.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String userJsonStr = null;

        //int numDays = 7; //How to find List Length from JSON?

        try {
            //Json URLS
            //http://aakashsheth.com/document.html
            //http://104.236.203.207/jsonexample.html
            URL url = new URL("http://104.236.203.207:3000/api/user");

            Log.v(LOG_TAG, "Built URI " + url);

            // Create the request to Designated URL, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            userJsonStr = buffer.toString();

            Log.v(LOG_TAG, "String: " + userJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the json data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            //jsonData = userJsonStr; //Set Global var for intent. Better way?

            return Utility.getProjectsFromUser(userJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the json
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<RowItem> result) {
        if (result != null) {
            //Clear previous entries
            setProjectList(result);
        }
    }

    public void setProjectList(ArrayList<RowItem> results){
        //Clear previous entries
        //userDataAdapter.clear();

        //add new entries one by one.
        //for(ArrayList<RowItem> userStr : results) {
            //userDataAdapter.add(userStr);
        //}
    }
}