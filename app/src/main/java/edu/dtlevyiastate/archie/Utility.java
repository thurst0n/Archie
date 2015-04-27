package edu.dtlevyiastate.archie;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/** Utility class to hold helper methods
 * @author David Levy <dtlevy@iastate.edu>
 * @version 0.1
 */
public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static int getStatusImage(int status) {
        switch (status) {
            case 0:
                return R.drawable.status_0;
            case 1:
                return R.drawable.status_1;
            case 2:
                return R.drawable.status_2;
            case 3:
                return R.drawable.status_3;
            default:
                return R.drawable.status_0;
        }
    }
}

