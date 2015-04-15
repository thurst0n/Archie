package edu.dtlevyiastate.jsontest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * A placeholder fragment containing a simple view.
 */
public class ModuleFragment extends Fragment {

    private static final String LOG_TAG = ModuleFragment.class.getSimpleName();
    private ArrayAdapter<String> userDataAdapter;
    private JSONArray jsonModuleArray = new JSONArray();
    private String jsonString = new String();

    public ModuleFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userDataAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_module, // The name of the layout ID.
                        R.id.list_item_module_textview, // The ID of the textview to populate.
                        new ArrayList<String>());


        View rootView = inflater.inflate(R.layout.fragment_module, container, false);

        // The module activity called via intent.  Inspect the intent for project data.
        Intent intent = getActivity().getIntent();
        if (intent != null){// && intent.hasExtra(Intent.EXTRA_TEXT)) {
            //mModuleStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            if(intent.hasExtra("JSON")) {
                try {
                    jsonString = intent.getStringExtra("JSON");
                    String[] results = Utility.getModulesFromProject(jsonString);
                    if (results != null) {
                        setModuleList(results);
                    }

                }
                catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

            }
        }
        else{
            if(savedInstanceState != null)
            {
                try {
                    jsonString = savedInstanceState.getString("json");
                    String[] results = Utility.getModulesFromProject(jsonString);
                    if (results != null) {
                        setModuleList(results);
                    }
                }
                catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

            }
        }




        ListView moduleListView = (ListView) rootView.findViewById(R.id.listview_modules);
        moduleListView.setAdapter(userDataAdapter);


        moduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String project = userDataAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ModuleActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, project); //Text for header/title
                try {
                    jsonModuleArray = Utility.getModuleArrayFromProject(new JSONObject(jsonString));

                    //Add actual JSON Data to be passed to next activity -- All of it for now
                    intent.putExtra("JSON", jsonModuleArray.getJSONObject(position).toString());
                    Log.v(LOG_TAG, jsonModuleArray.getJSONObject(position).toString());
                }
                catch(JSONException e){
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        return rootView;


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("json", jsonString);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //jsonString = savedInstanceState.getString("json");
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
    }


    public void setModuleList(String results[]){
        //Clear previous entries
        userDataAdapter.clear();

        //add new entries one by one.
        for(String userStr : results) {
            userDataAdapter.add(userStr);
        }
    }



}
