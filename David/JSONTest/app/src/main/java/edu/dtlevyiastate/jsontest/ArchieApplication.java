package edu.dtlevyiastate.jsontest;

import android.app.Application;
import com.firebase.client.Firebase;

/**
 * Created by Guru on 4/15/2015.
 */
public class ArchieApplication  extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
