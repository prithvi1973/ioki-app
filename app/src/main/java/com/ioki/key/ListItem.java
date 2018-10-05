package com.ioki.key;

import android.widget.ImageButton;

public class ListItem {
    String heading;
    String description;
    ImageButton actionButton;

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

    public ImageButton getActionButton() {
        return actionButton;
    }
}
