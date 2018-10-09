package com.ioki.key;

public class ListItem {
    private String heading;
    private String description;
    private String requestType;
    private String id;

    ListItem(String heading, String description, String requestType, String id) {
        this.heading = heading;
        this.description = description;
        this.requestType = requestType;
        this.id = id;
    }

    public String getHeading() {return heading;}

    public String getDescription() {return description;}

    public String getRequestType() {return requestType;}

    public String getId() {return id;}
}
