package edu.dtlevyiastate.archie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/** MainActivity runs the list Fragment and swaps view adapters based on user selection
 * @author David Levy <dtlevy@iastate.edu>
 * @version 0.1
 */
public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static Firebase myFirebaseRef;
    private static DataSnapshot mySnapshot;

    public static String curUserId;
    public static String curName;
    public static String curProject;
    public static String curModule;

    public static String curTask;
    private static int viewLevel=1;

    public static ArrayAdapter<RowItem> projectListAdapter;
    public static ArrayAdapter<RowItem> moduleListAdapter;
    public static ArrayAdapter<RowItem> taskListAdapter;

    public static int getViewLevel(){
        return viewLevel;
    }

    public static void nextViewLevel(){
       if(viewLevel != 3){
           viewLevel++;
       }
    }

    public static void setListAdapter(String selectionName){

        switch(viewLevel)
        {
            case 3:
                Log.v(LOG_TAG,"setting task list");
                setTaskListAdapter(selectionName);
                break;
            case 2:
                Log.v(LOG_TAG,"setting module list");
                setModuleListAdapter(selectionName);
                break;
            case 1:
                Log.v(LOG_TAG, "setting project list");
                setProjectListAdapter();
                break;
            default:
                Log.v(LOG_TAG, "Default List Set : View Level:" + viewLevel);
                setProjectListAdapter();
            break;

        }
    }

    public static ArrayAdapter<RowItem> getListAdapter(){
        switch(getViewLevel())
        {
            case 1:
               return getProjectListAdapter();

            case 2:

                return getModuleListAdapter();

            case 3:
                return getTaskListAdapter();

            default:
                return getProjectListAdapter();


        }
    }
    public static void setProjectListAdapter() {
        //viewLevel=1;
        Log.v(LOG_TAG, "view level = " + viewLevel);
        projectListAdapter.clear();
        if (mySnapshot != null) {
            for (DataSnapshot project : mySnapshot.getChildren() ) {
                if (project.child("Name").getValue() != null) {

                    //Get Project Details from Firebase JSON
                    String pName = project.child("Name").getValue().toString();
                    String pDesc = project.child("Description").getValue().toString();
                    int statusImageId = Utility.getStatusImage(Integer.parseInt(project.child("Status").getValue().toString()));

                    //Create RowItem for ArrayList
                    RowItem proj = new RowItem(statusImageId, pName, pDesc);

                    //Add RowItem to the adapter arraylist
                    projectListAdapter.add(proj);
                }

            }
        }
    }

    public static void setModuleListAdapter(String projectName) {
        //viewLevel = 2;
        Log.v(LOG_TAG,"view level = " + viewLevel);
        moduleListAdapter.clear();
        curProject = projectName;
        if (mySnapshot != null) {
            Log.v(LOG_TAG, mySnapshot.child(curProject).child("modules").toString());
            for (DataSnapshot module : mySnapshot.child(projectName).child("modules").getChildren() ) {
                if (module.child("Name").getValue() != null) {

                    //Get Project Details from Firebase JSON
                    String pName = module.child("Name").getValue().toString();
                    String pDesc = module.child("Description").getValue().toString();
                    int statusImageId = Utility.getStatusImage(Integer.parseInt(module.child("Status").getValue().toString()));

                    //Create RowItem for ArrayList
                    RowItem proj = new RowItem(statusImageId, pName, pDesc);

                    //Add RowItem to the adapter arraylist
                    moduleListAdapter.add(proj);

                }

            }
        }
    }

    public static void setTaskListAdapter(String moduleName) {
        //viewLevel =3;
        Log.v(LOG_TAG,"view level = "+ viewLevel);
        taskListAdapter.clear();
        curModule = moduleName;
        if (mySnapshot != null) {
            Log.v(LOG_TAG, mySnapshot.child(curProject).child("modules").child(moduleName).toString());
            for (DataSnapshot task : mySnapshot.child(curProject).child("modules").child(moduleName).child("tasks").getChildren() ) {
                if (task.child("Name").getValue() != null) {
                    //Get Project Details from Firebase JSON
                    String pName = task.child("Name").getValue().toString();
                    String pDesc = task.child("Description").getValue().toString();
                    int statusImageId = Utility.getStatusImage(Integer.parseInt(task.child("Status").getValue().toString()));

                    //Create RowItem for ArrayList
                    RowItem proj = new RowItem(statusImageId, pName, pDesc);

                    //Add RowItem to the adapter arraylist
                    taskListAdapter.add(proj);
                }
                else
                {
                    Log.v(LOG_TAG, "no tasks...");
                }
            }
            Log.v(LOG_TAG, "end for loop");
        }

    }



    public static ArrayAdapter<RowItem> getModuleListAdapter() {
        return moduleListAdapter;
    }
    public static ArrayAdapter<RowItem> getTaskListAdapter() {
        return taskListAdapter;
    }
    public static ArrayAdapter<RowItem> getProjectListAdapter() {
        return projectListAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        curUserId=this.getIntent().getStringExtra("uid").toString();
        myFirebaseRef =new Firebase("https://archieproject.firebaseio.com/users/"+ curUserId);

        myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                curName = snapshot.child("first name").getValue() + " " + snapshot.child("last name").getValue();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        myFirebaseRef =new Firebase("https://archieproject.firebaseio.com/users/"+ curUserId + "/projects/");

        projectListAdapter = new ListAdapter(this, new ArrayList<RowItem>());
        moduleListAdapter = new ListAdapter(this, new ArrayList<RowItem>());
        taskListAdapter = new ListAdapter(this, new ArrayList<RowItem>());

        myFirebaseRef.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot snapshot)  {
                mySnapshot = snapshot;
                //Log.v(LOG_TAG, snapshot.getValue().toString());
                Log.v(LOG_TAG, "Checking authdata");
                AuthData authData = myFirebaseRef.getAuth();
                if (authData != null) {
                    Log.v(LOG_TAG, "data change - initiating list update");
                    switch(viewLevel){
                        case 3:
                            Log.v(LOG_TAG,"setting task list");
                            setTaskListAdapter(curModule);
                        case 2:
                            Log.v(LOG_TAG,"setting module list");
                            setModuleListAdapter(curProject);
                        case 1:
                            Log.v(LOG_TAG,"setting project list");
                            setProjectListAdapter();
                        default:
                            //FallThrough Intended to make sure all lists are set at any given level
                            Log.v(LOG_TAG, "List update complete");
                    }
                    Log.v(LOG_TAG, "Getting Info for Uid: " + authData.getUid());


                    //Log.v(LOG_TAG, "Snapshot: " + mySnapshot.getValue().toString());
                    setProjectListAdapter();
                } else {
                    // no user authenticated with Firebase
                    Log.v(LOG_TAG, "no user auth");
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed - you are dumb: " + firebaseError.getMessage());
            }
        });

        getSupportActionBar().setTitle("My Projects");


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MyListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        myFirebaseRef.unauth();
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        switch(MainActivity.getViewLevel())
        {
            case 1:
                logout();
                break;
            case 2:
                viewLevel=1;
                getSupportActionBar().setTitle("Projects");
                MyListFragment.setListView();
                break;
            case 3:
                viewLevel=2;
                getSupportActionBar().setTitle(curProject);
                MyListFragment.setListView();
                break;
            default:
                logout();
                break;

        }
    }
    public void setActionTitle(String title){
        getSupportActionBar().setTitle(title);
    }


    private void logout(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                //don't finish
                        dialog.dismiss();
            }
                 })
                .show();

    }


    //TODO SETUP for backstack?
    public void setContent(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MyListFragment())
                .commit();
    }


}
