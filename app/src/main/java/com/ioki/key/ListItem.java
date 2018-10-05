package com.ioki.key;

public class ListItem {
    String heading;
    String description;

    public ListItem(String heading, String description) {
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
