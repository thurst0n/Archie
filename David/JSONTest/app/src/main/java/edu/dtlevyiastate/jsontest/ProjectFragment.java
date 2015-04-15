package edu.dtlevyiastate.jsontest;
//e - error, w - warn, i - info, d - debug, v - verbose

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.theopentutorials.android.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Encapsulates fetching the json data and displaying it as a {@link android.widget.ListView} layout.
 */
public class ProjectFragment extends Fragment {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private ArrayAdapter<String> userDataAdapter;
    private JSONArray jsonProjectArray = new JSONArray();
    //private JSONObject jsonObject = new JSONObject();

    public ProjectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.userfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchUserData userDataTask = new FetchUserData();
            userDataTask.execute("http://104.236.203.207:3000/api/user/bob@hope.com"); //Made up parameter for future use
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The ArrayAdapter will take data from a source (DB/JSON Object) and
        // use it to populate the ListView it's attached to.
        userDataAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_project, // The name of the layout ID.
                        R.id.list_item_module_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        FetchUserData userDataTask = new FetchUserData();
        userDataTask.execute("http://104.236.203.207:3000/api/user/bob@hope.com");
        View rootView = inflater.inflate(R.layout.fragment_project, container, false);


        /**
         * Find view examples
         * ImageView doesn't have or need an ID. That's okay.
         *
         *          VIEW HIERARCHY
         *
         *                  [Relative Layout] id:root
         *                      /           \
         * id:container[Linear Layout]       [Button] id:btn
         *             /        \
         *      [ImageView]    [TextView] id:txt
         */
        //Button b = (Button)this.findViewById(R.id.btn);
        //LinearLayout container = (LinearLayout)this.findViewById(R.id.Container);
        //TextView t = (TextView)container.findViewById(R.id.txt); //Smaller subtree once container is created


        // Get a reference to the ListView, and attach this adapter to it.
        ListView projectListView = (ListView) rootView.findViewById(R.id.listview_projects);

        projectListView.setAdapter(userDataAdapter);


        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String project = userDataAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ProjectActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, project); //Text for header/title
                try {

                    //Add actual JSON Data to be passed to next activity -- All of it for now
                    intent.putExtra("JSON", jsonProjectArray.getJSONObject(position).toString());
                    Log.v(LOG_TAG, jsonProjectArray.getJSONObject(position).toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchUserData extends AsyncTask<String, Void, String[]> {

        //Set Log Tag to class Name: FetchUserData
        private final String LOG_TAG = FetchUserData.class.getSimpleName();






        @Override
        protected String[] doInBackground(String... params) {

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
                URL url = new URL("http://104.236.203.207:3000/api/user/bob@hope.com");

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
                //jsonObject = getJSONObject(userJsonStr);
                jsonProjectArray = Utility.getProjectArrayFromUser(userJsonStr);

                return Utility.getProjectsFromUser(userJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the json
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                //Clear previous entries
                setProjectList(result);
            }
        }

        public void setProjectList(String results[]){
            //Clear previous entries
            userDataAdapter.clear();

            //add new entries one by one.
            for(String userStr : results) {
                userDataAdapter.add(userStr);
            }
        }
    }
}