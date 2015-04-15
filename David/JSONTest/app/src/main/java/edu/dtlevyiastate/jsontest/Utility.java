package edu.dtlevyiastate.jsontest;

import android.util.Log;

import com.theopentutorials.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by David on 3/7/2015.
 */
public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();


    /**
     * GET PROJECT DATA
     */
    public static String[] getProjectsFromUser(JSONObject userJson) throws JSONException{
         //Get Data from the jsonObj
        JSONArray projectArray = getProjectArrayFromUser(userJson);
        int projectCount = projectArray.length();

        //Setup return string based on cnt provided in json. -- Data/mem usage from increased cnt fields VS cpu usage from .length calls?
        String[] resultStr = new String[projectCount];
        for(int i=0; i < projectCount; i++){
            //Take each project object from the project Array.
            JSONObject curProject = projectArray.getJSONObject(i);

            //Setup result string array to be used to display project list.
            resultStr[i] = curProject.getString("project_name");
        }

        //Logging
        for (String s : resultStr) {
            Log.v(LOG_TAG, "List entry: " + s);
        }
        return resultStr;

    }

    public static JSONArray getProjectArrayFromUser(JSONObject userJson) throws JSONException{
        return userJson.getJSONArray("user_projects");
    }

    public static JSONArray getProjectArrayFromUser(String userJson) throws JSONException{
        return (new JSONObject(userJson)).getJSONArray("user_projects");
    }

    public static String[] getProjectsFromUser(String userJsonStr) throws JSONException {

        //Create jsonObj to access data
        JSONObject jsonObj = new JSONObject(userJsonStr);
        return getProjectsFromUser(jsonObj);
    }


    /**
     * GET MODULE DATA
     */
    public static String[] getModulesFromProject(JSONObject projJson) throws JSONException{

        JSONArray moduleArray = getModuleArrayFromProject(projJson);
        Log.v(LOG_TAG, "moduleArray: " + moduleArray.toString());

        Integer moduleCount = moduleArray.length();
        //Setup return string based on cnt provided in json. -- Data/mem usage from increased cnt fields VS cpu usage from .length calls?
        String[] resultStr = new String[moduleCount];
        for(int i=0; i < moduleCount; i++){
            //Take each module object from the project Array.
            JSONObject curModule = moduleArray.getJSONObject(i);

            //Setup result string array to be used to display project list.
            resultStr[i] = curModule.getString("module_name");
        }

        //Logging
        for (String s : resultStr) {
            Log.v(LOG_TAG, "List entry: " + s);
        }

        return resultStr;
    }

    public static String[] getModulesFromProject(String jsonString)
            throws JSONException {

        JSONObject projectObj = new JSONObject(jsonString);
        return getModulesFromProject(projectObj);

    }

    public static JSONArray getModuleArrayFromProject(JSONObject projJson) throws JSONException{
        return projJson.getJSONArray("project_modules");
    }


    /**
     * GET TASK DATA
     */
    public static String[] getTasksFromModule(JSONObject modJson) throws JSONException{


        JSONArray taskArray = getTaskArrayFromModule(modJson);
        Log.v(LOG_TAG, "taskArray: " + taskArray.toString());

        int taskCount = taskArray.length();
        //Setup return string baseo on cnt provided in json. -- Data/mem usage from increased cnt fields VS cpu usage from .length calls?
        String[] resultStr = new String[taskCount];
        for(int i=0; i < taskCount; i++){
            //Take each module object from the project Array.
            JSONObject curModule = taskArray.getJSONObject(i);

            //Setup result string array to be used to display project list.
            resultStr[i] = curModule.getString("task_name");
        }

        //Logging
        for (String s : resultStr) {
            Log.v(LOG_TAG, "List entry: " + s);
        }
        return resultStr;
    }

    public static String[] getTasksFromModule(String modJsonStr) throws JSONException{
        JSONObject modJson = new JSONObject(modJsonStr);
        return getTasksFromModule(modJson);
    }

    public static JSONArray getTaskArrayFromModule(JSONObject modJson) throws JSONException{
        return modJson.getJSONArray("module_tasks");
    }







    /**
     * Take the String representing the complete JSON Model in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getWidgetDataFromJson(String userJsonStr)
            throws JSONException {


        JSONObject widgetJson = new JSONObject(userJsonStr);

        JSONObject widget = widgetJson.getJSONObject(String.valueOf(R.string.OWM_WIDGET));

        JSONObject window = widget.getJSONObject(String.valueOf(R.string.OWM_WINDOW));
        String title = window.getString(String.valueOf(R.string.OWM_TITLE));

        JSONObject img = widget.getJSONObject(String.valueOf(R.string.OWM_IMAGE));
        String image = img.getString(String.valueOf(R.string.OWM_SOURCE));

        JSONObject txt = widget.getJSONObject(String.valueOf(R.string.OWM_TEXT));
        String text = txt.getString(String.valueOf(R.string.OWM_DATA));


        String[] resultStrs = new String[3];
        resultStrs[0] = String.valueOf(R.string.OWM_WINDOW) + " - " + title;
        resultStrs[1] = String.valueOf(R.string.OWM_IMAGE) + " - " + image;
        resultStrs[2] = String.valueOf(R.string.OWM_SOURCE) + " - " + text;


        for (String s : resultStrs) {
            Log.v(LOG_TAG, "List entry: " + s);
        }
        return resultStrs;

    }



    //TODO Replace hardcoded strings with strings.xml  String.valueOf(R.string.OWM_TEXT) not parsing correctly
    private String[] getProjectDataFromJson(String userJsonStr)
            throws JSONException {
        //Create jsonObj to access data
        JSONObject jsonObj = new JSONObject(userJsonStr);
        //jsonObj.getString("name");

        //Get Data from the jsonObj
        //JSONArray projectArray = new JSONArray(projectName);
        String projectName = jsonObj.getString("project_name");
        ArrayList projectList = new ArrayList();
        projectList.add(jsonObj);
        JSONArray projectArray = new JSONArray(projectList);
        //jsonProjectArray = projectArray;

            /*int projectCount = projectArray.length();
            jsonProjectArray = projectArray;
            //Setup return string based on cnt provided in json. -- Data/mem usage from increased cnt fields VS cpu usage from .length calls?
            String[] resultStr = new String[projectCount];
            for(int i=0; i < projectCount; i++){
                //Take each project object from the project Array.
                JSONObject curProject = projectArray.getJSONObject(i);

                //Setup result string array to be used to display project list.
                resultStr[i] = curProject.getString("project_name");
            }
            */
        String[] resultStr = new String[1];
        resultStr[0] = projectName;
        //Logging
        for (String s : resultStr) {
            Log.v(LOG_TAG, "List entry: " + s);
        }
        return resultStr;

    }

}

