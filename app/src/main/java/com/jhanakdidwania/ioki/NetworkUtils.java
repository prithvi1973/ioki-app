package com.jhanakdidwania.ioki;


import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    final static String ip = "172.31.113.81";
    final static String IOki_BASE_URL =
           "http://" + ip + "/users";
    final static String IOki_REGISTER_URL =
            "http://" + ip + "/users/register";

    final static String LOGIN_PARAM_QUERY_NAME = "name";
    final static String LOGIN_PARAM_QUERY_PASSWORD = "pass";

    static String username;
    static String name;
    static String password;
    static String confirmp;
    static String mobile;
    static String PIN;
    static String email;

    public static void setUsername(String username) {
        this.username = username;
    }

    public static void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmp(String confirmp) {
        this.confirmp = confirmp;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static URL buildUrl(String LoginQueryName, String LoginQueryPassword) {
        Uri builtUri = Uri.parse(IOki_BASE_URL).buildUpon()
                .appendQueryParameter(LOGIN_PARAM_QUERY_NAME, LoginQueryName)
                .appendQueryParameter(LOGIN_PARAM_QUERY_PASSWORD, LoginQueryPassword)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
