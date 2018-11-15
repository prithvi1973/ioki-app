package com.ioki.key;

public class ListItem {
    private String heading;
    private String description;
    private String requestType;
    private String id;
    private String lockStatus;
    private boolean approved;
    private String lockID;

    ListItem(String heading, String description, String requestType, String id, String lockID, boolean approved) {
        this.heading = heading;
        this.description = description;
        this.requestType = requestType;
        this.id = id;
        this.lockID = lockID;
        this.lockStatus = "FAIL";
        this.approved = approved;
    }

    public String getHeading() {return heading;}

    public String getDescription() {return description;}

    public String getRequestType() {return requestType;}

    public String getId() {return id;}

    public String getLockStatus() {return lockStatus;}

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getLockID() {
        return lockID;
    }
}
