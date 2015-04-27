package edu.dtlevyiastate.archie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/** Starting activity for application - allows user to login so they may access their data
 * @author David Levy <dtlevy@iastate.edu>
 * @version 0.1
 */
public class LoginActivity extends ActionBarActivity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    //Firebase DB reference - NO connection is established
    private static Firebase myFirebaseRef =new Firebase("https://archieproject.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Login button press method. Takes user input and checks authorization with Firebase.
     * If successful authentication is made we start the Main activity.
     * @param view
     */
    public void login(View view)    {
        //Establish intent variable to pass start next activity and pass any desired data.
        final Intent intent = new Intent(this, MainActivity.class);

        //Establish variables to access EditText user entries
        EditText emailText = (EditText) findViewById(R.id.email);
        EditText passText = (EditText) findViewById(R.id.password);

        // Create a handler to handle the result of the authentication
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.v(LOG_TAG, "Logged in As: UserID: " + authData.getUid());

                //Package authenticated userID to with the intent for next activity(Main)
                intent.putExtra("uid", authData.getUid().toString());

                //Start main activity
                startActivity(intent);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
                //TODO HANDLE LOGIN ERROR FOR USER
                // Something went wrong :(
                switch (firebaseError.getCode()) {
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        Log.v(LOG_TAG, "Invalid user does not exist");
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        Log.v(LOG_TAG,"Ivalid Password");
                        break;
                    default:
                        Log.v(LOG_TAG, "Default");
                        break;
                }
            }
        };

        // Send auth request to firebase with user inputs handler will handle result so
        // onAuthenticated or on AuthenticationError will be called after this line
        myFirebaseRef.authWithPassword(emailText.getText().toString(), passText.getText().toString(), authResultHandler);
    }

    public void register(View view)
    {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
