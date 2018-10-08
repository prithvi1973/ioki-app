package com.ioki.key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Response {

    private boolean responseValidity;
    private String errors;

    private String messages[];
    private String valid;
    private boolean link;
    private String view;
    private String data[];
    private String session[];

    public Response(JSONObject response) {

        responseValidity = false;
        StringBuilder errorBuilder = new StringBuilder();

        try {
            ArrayList<String> messagesArrayList = new ArrayList<>();
            JSONArray messagesArray = response.getJSONArray("messages");
            for(int i=0; i<messagesArray.length(); i++) {
                messagesArrayList.add(messagesArray.getString(i));
            }
            this.messages = messagesArrayList.toArray(new String[0]);
            responseValidity = true;
        }
        catch (JSONException e) {
            errorBuilder.append("Messages Error");
        }

        try {
            this.valid = response.getJSONObject("redirect").getString("valid");
            responseValidity = true;
        }
        catch (JSONException e) {
            errorBuilder.append("Valid Error");
        }

        try {
            this.valid = response.getJSONObject("redirect").getString("link");
            responseValidity = true;
        }
        catch (JSONException e) {
            errorBuilder.append("Link Error");
        }

        try {
            this.view = response.getString("view");
            responseValidity = true;
        }
        catch (JSONException e) {
            errorBuilder.append("View Error");
        }

        try {
            ArrayList<String> dataArrayList = new ArrayList<>();
            JSONArray dataArray = response.getJSONArray("data");
            for(int i=0; i<dataArray.length(); i++) {
                dataArrayList.add(dataArray.getString(i));
            }
            this.data = dataArrayList.toArray(new String[0]);
            responseValidity = true;
        }
        catch (JSONException e) {
            errorBuilder.append("Data Error");
        }

        try {
            ArrayList<String> sessionArrayList = new ArrayList<>();
            JSONArray sessionArray = response.getJSONArray("session");
            for(int i=0; i<sessionArray.length(); i++) {
                sessionArrayList.add(sessionArray.getString(i));
            }
            this.session = sessionArrayList.toArray(new String[0]);
            responseValidity = true;
        }
        catch (JSONException e) {
            errorBuilder.append("Session Error");
        }

    }

    public boolean isValid() {
        return responseValidity;
    }

    public boolean isResponseValidity() {
        return responseValidity;
    }

    public String getErrors() {
        return errors;
    }

    public String[] getMessages() {
        return messages;
    }

    public String getValid() {
        return valid;
    }

    public boolean isLink() {
        return link;
    }

    public String getView() {
        return view;
    }

    public String[] getData() {
        return data;
    }

    public String[] getSession() {
        return session;
    }
}
