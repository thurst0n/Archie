package edu.dtlevyiastate.jsontest;

import java.util.ArrayList;
import org.json.JSONObject;
/**
 * Created by David on 3/7/2015.
 */
public class ProjectContainer {
    private static ProjectContainer mInstance = null;
    private JSONObject json;
    private String id;
    private String name;
    private String createdBy;
    private String due;
    private byte status;
    private String description;
    private ArrayList<ModuleContainer> modules;


    private ProjectContainer()
    {
        //Call fetfhuserdata and fill in project info.
    }


    public static ProjectContainer getInstance(){
        if(mInstance == null)
        {
            mInstance = new ProjectContainer();
        }
        return mInstance;
    }




    public JSONObject getJson() { return json;}
    public void setJson(JSONObject newJson){this.json = newJson;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ArrayList<ModuleContainer> getModules() {
        return modules;
    }

    public void setModules(ArrayList<ModuleContainer> modules) {
        this.modules = modules;
    }





}