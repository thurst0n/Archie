package edu.dtlevyiastate.archie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

/** Fragment to hold the listview for main activity
 * @author David Levy <dtlevy@iastate.edu>
 * @version 0.1
 */
public class MyListFragment extends Fragment {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    //private ArrayAdapter<RowItem> userDataAdapter;
    private static ListView projectListView;

    public static void setListView(){
        projectListView.setAdapter(MainActivity.getListAdapter());
    }

    public MyListFragment() {
    }

    public void add(View view)
    {
        Intent newIntent = new Intent(getActivity(), NewActivity.class);
        startActivity(newIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
            }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The ArrayAdapter will take data from a source (DB/JSON Object) and
        // use it to populate the ListView it's attached to.
        // Get current list adapter from main and set it here to the rootview for this
        // list fragment
        //userDataAdapter = MainActivity.getListAdapter();
        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);
        projectListView = (ListView) rootView.findViewById(R.id.listview);
        projectListView.setAdapter(MainActivity.getListAdapter());

        //Set list click listener, if an item is clicked(selected) then we will change view
        //to the nexst list
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (MainActivity.getViewLevel() == 3) {
                    //MainActivity.getListAdapter().getItem(position).getTitle();
                    final Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("pos", position);
                    intent.putExtra("task", MainActivity.getListAdapter().getItem(position).getTitle());
                    startActivity(intent);
                }
                else{

                    //userDataAdapter = MainActivity.getListAdapter();
                    RowItem selection = MainActivity.getListAdapter().getItem(position);
                    MainActivity.nextViewLevel();
                    MainActivity.setListAdapter(selection.getTitle());
                    Log.v(LOG_TAG, selection.getTitle());
                    projectListView.setAdapter(MainActivity.getListAdapter());
                    if(MainActivity.getViewLevel()==3){
                        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(MainActivity.curProject + "/" + MainActivity.curModule);
                    }
                    else{
                        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(MainActivity.curProject);
                    }
                }
            }

        });
        Button addButton = (Button) rootView.findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent newIntent = new Intent(getActivity(), NewActivity.class);
                    startActivity(newIntent);
            }
        });
        return rootView;
    }



}