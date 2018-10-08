package com.ioki.key;

public class ListItem {
    private String heading;
    private String description;
    private String requestType;

    ListItem(String heading, String description, String requestType) {
        this.heading = heading;
        this.description = description;
        this.requestType = requestType;
    }

    public String getHeading() {return heading;}

    public String getDescription() {return description;}

    public String getRequestType() {return requestType;}
}
