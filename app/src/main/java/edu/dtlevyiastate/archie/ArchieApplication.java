package edu.dtlevyiastate.archie;

import android.app.Application;

import com.firebase.client.Firebase;
/**
 * @author David Levy <dtlevy@iastate.edu>
 * @version 0.1
 */
public class ArchieApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        //Establish firebase context Application wide
        Firebase.setAndroidContext(this);

    }
}
