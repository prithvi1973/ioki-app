package com.ioki.key;

public class ListItem {
    private String heading;
    private String description;
    private String requestType;
    private String id;
    private String lockStatus;

    ListItem(String heading, String description, String requestType, String id) {
        this.heading = heading;
        this.description = description;
        this.requestType = requestType;
        this.id = id;
        this.lockStatus = "FAIL";
    }

    public String getHeading() {return heading;}

    public String getDescription() {return description;}

    public String getRequestType() {return requestType;}

    public String getId() {return id;}

    public String getLockStatus() {return lockStatus;}

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }
}
