package edu.dtlevyiastate.archie;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends ActionBarActivity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    protected static final String firebaseURL = "https://archieproject.firebaseio.com/";
    private final Firebase rootRef = new Firebase(firebaseURL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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



    public void check(View view)
    {
        //Register new Intent
        final Intent login = new Intent(this, LoginActivity.class);

        //Get new user info
        final String first = ((EditText) findViewById(R.id.reg_firstname)).getText().toString().trim();
        final String last = ((EditText) findViewById(R.id.reg_lasttname)).getText().toString().trim();
        final String email = ((EditText) findViewById(R.id.reg_email)).getText().toString().trim();
        final String password = ((EditText) findViewById(R.id.reg_password)).getText().toString().trim();
        String confirmPassword = ((EditText) findViewById(R.id.reg_confirm_password)).getText().toString().trim();


        //Attempt to create a new user
        rootRef.createUser(email,password, new Firebase.ValueResultHandler<Map<String, Object>>(){

            //If successful, authenticate user and login them in
            @Override
            public void onSuccess(Map<String, Object> result) {

                rootRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {

                        Log.v(LOG_TAG, "Authenticated with Payload: " +  authData.toString());

                        String url = firebaseURL + "users/" + authData.getUid();

                        Firebase userRef = new Firebase(url);

                        Map<String,Object> credentials = new HashMap<String, Object>();
                        credentials.put("email", email);
                        credentials.put("first name", first);
                        credentials.put("last name", last);

                        userRef.setValue(credentials);

                        userRef.unauth();

                        startActivity(login);

                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                    }
                });



            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.v(LOG_TAG, "Registration Successful");
            }
        });


    }

    public void login(View view)
    {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }
}