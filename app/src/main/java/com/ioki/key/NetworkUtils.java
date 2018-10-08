package com.ioki.key;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import android.content.SharedPreferences;
import javax.net.ssl.HttpsURLConnection;

import static com.ioki.key.MainActivity.PREFERENCES;
import static com.ioki.key.MainActivity.RESPONSE;
import static com.ioki.key.MainActivity.mSharedPreferences;

/**
 * These utilities will be used to communicate with the server.
 */
public class NetworkUtils {

    /** Base url will look like https://www.ioki.com
     *  When required, users/login/ or users/register/ can be appended to it
     *  to generate usage specific URLs
     */
    final static String IoKi_HOST_IP = "http://192.168.43.224/";
    final static String IoKi_BASE_URL = IoKi_HOST_IP + "ioki/api/";

    /** Helper function for performPostDataString
     *  Reads a hash map of parameters
     *  Prepares and returns string with parameters POST query
     *  Example: <name:jhanak,pass:1234,type:1> becomes "name=jhanak&pass=1234&type=1"
     */
    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        String oldSessionVars = mSharedPreferences.getString(RESPONSE, null);
        result.append("&session=" + oldSessionVars);

        return result.toString();
    }

    /**
     * Establishes an HTTP Connection
     * Posts data to requestURL with postDataParams
     * Fetches response stream
     * Builds and returns response as string
     */
    public static String  performPostCall(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        // TODO: Add app request identifier in request URL
        Log.d("ioki", requestURL);
        try {
            Log.d("ioki", getPostDataString(postDataParams));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder response = new StringBuilder();
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response.append(line);
                }
            }
            else {
                response = new StringBuilder();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
