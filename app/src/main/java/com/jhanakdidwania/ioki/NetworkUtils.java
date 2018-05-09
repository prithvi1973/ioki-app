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
           "http://" + ip + "/capstone/login";

    final static String PARAM_QUERY_NAME = "name";
    final static String PARAM_QUERY_PASSWORD = "pass";

    public static URL buildUrl(String LoginQueryName, String LoginQueryPassword) {
        Uri builtUri = Uri.parse(IOki_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY_NAME, LoginQueryName)
                .appendQueryParameter(PARAM_QUERY_PASSWORD, LoginQueryPassword)
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
