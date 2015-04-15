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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.theopentutorials.android.R;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TaskFragment extends Fragment {

    private static final String LOG_TAG = ModuleFragment.class.getSimpleName();
    private ArrayAdapter<String> userDataAdapter;

    public TaskFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userDataAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_task, // The name of the layout ID.
                        R.id.list_item_task_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        // The module activity called via intent.  Inspect the intent for project data.
        Intent intent = getActivity().getIntent();
        if (intent != null){// && intent.hasExtra(Intent.EXTRA_TEXT)) {
            //mModuleStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            if(intent.hasExtra("JSON")) {
                try {
                    String result[] = Utility.getTasksFromModule(intent.getStringExtra("JSON"));
                    if (result != null)
                    {
                        setTaskList(result);
                    }
                }
                catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

            }
        }


        ListView moduleListView = (ListView) rootView.findViewById(R.id.listview_tasks);
        moduleListView.setAdapter(userDataAdapter);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
    }

    public void setTaskList(String results[]){
        //Clear previous entries
        userDataAdapter.clear();

        //add new entries one by one.
        for(String userStr : results) {
            userDataAdapter.add(userStr);
        }
    }


}
