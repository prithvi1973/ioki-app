package com.ioki.key;

public class ListItem {
    private String heading;
    private String description;

    ListItem(String heading, String description) {
        this.heading = heading;
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

}
