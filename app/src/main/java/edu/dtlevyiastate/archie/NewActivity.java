package edu.dtlevyiastate.archie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;


public class NewActivity extends ActionBarActivity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    protected final static String firebaseURL = "https://archieproject.firebaseio.com/";
    protected String firebasePath = firebaseURL;
    private final Firebase rootRef = new Firebase(firebaseURL);
    private String pretext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);


        switch(MainActivity.getViewLevel()){
            case 3:
                //Looking at list of tasks
                pretext = "Task ";
                firebasePath = firebaseURL + "/users/" + MainActivity.curUserId + "/projects/" + MainActivity.curProject + "/modules/" + MainActivity.curModule + "/tasks/";
                break;
            case 2:
                //Looking at list of modul;es
               pretext = "Module ";
                firebasePath = firebaseURL + "/users/"+ MainActivity.curUserId + "/projects/" + MainActivity.curProject + "/modules/";
                break;
            case 1:
                //Looking at list of projects
                pretext = "Project ";
                firebasePath = firebaseURL + "/users/" + MainActivity.curUserId + "/projects/";
                break;
            default:
                pretext = " ";
        }
        ((TextView) findViewById(R.id.nametext)).setText(pretext + "name:");
        ((TextView) findViewById(R.id.desctext)).setText(pretext + "description:");
        ((TextView) findViewById(R.id.datetext)).setText(pretext + "due date:");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu); //Login menu is blank
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void add(View view) {



        //Firebase ref = new Firebase(url);



        //Get new user info
        final String name = ((EditText) findViewById(R.id.new_name)).getText().toString().trim();
        final String desc = ((EditText) findViewById(R.id.new_desc)).getText().toString().trim();
        final String date = ((EditText) findViewById(R.id.new_date)).getText().toString().trim();

        String url = firebasePath + name;
        Firebase newRef = new Firebase(url);
        Log.v(LOG_TAG, url);
        AuthData authData = newRef.getAuth();

        if (authData != null) {
            Log.v(LOG_TAG, "Auth as " + authData.getUid());
            Map<String, Object> newEntry = new HashMap<String, Object>();
            newEntry.put("Name", name);
            newEntry.put("Description", desc);
            newEntry.put("DueDate", date);
            newEntry.put("Status", 0);

            newRef.updateChildren(newEntry, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        Log.v(LOG_TAG, "Data could not be saved. " + firebaseError.getMessage());
                    } else {
                        Log.v(LOG_TAG, "Data saved successfully. Finishing activity...");
                        finish();
                    }
                }
            });
            ;
        }
        else{
            Log.v(LOG_TAG, "No authdata");
        }
    }

}