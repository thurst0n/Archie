package edu.dtlevyiastate.jsontest;

import java.util.ArrayList;

/**
 * Created by David on 3/7/2015.
 */
public class TaskContainer{
    public void setWorkers(ArrayList<String> workers) {
        this.workers = workers;
    }

    //private static ModuleContainer mInstance = null;
    private String id;
    private String name;
    private String createdBy;
    private String due;
    private byte status;
    private String description;
    private ArrayList<String> comments;
    private ArrayList<String> workers;




    private TaskContainer() {
        //Call fetchfhuserdata and fill in project info.
    }


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

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getWorkers() {
        return workers;
    }


}


