package com.ioki.key;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.ioki.key.MainActivity.RESPONSE;
import static com.ioki.key.MainActivity.getPreferenceObject;

/**
 * These utilities will be used to communicate with the server.
 */
public class NetworkUtils {

    /**
     *  Base url will look like https://www.ioki.com
     *  When required, users/login/ or users/register/ can be appended to it
     *  to generate usage specific URLs
     */
    final static String IoKi_HOST = "192.168.43.224";
    private final static String IoKi_HOST_IP = "http://"+IoKi_HOST+"/";
    final static String IoKi_BASE_URL = IoKi_HOST_IP + "ioki/api/";

    public static final int LOCK_SERVER_COM_PORT = 9292;

    /**
     *  Helper function for performPostDataString
     *  Reads a hash map of parameters
     *  Binds session information in the params
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

        String oldSessionVars = getPreferenceObject().getPreferences(RESPONSE);
        String defaultValue = "DEFAULT";
        if(!oldSessionVars.equals(defaultValue)){
            try {
                JSONObject ob = new JSONObject(oldSessionVars);
                result.append("&session=").append(ob.getJSONObject("session").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    private static String getSocketParamString(String... params) {
        StringBuilder result = new StringBuilder();
        for(int i=0; i<params.length; i++){
            if(i>0)
                result.append(":");
            result.append(params[i]);
        }
        return result.toString();
    }

    /**
     * Establishes an HTTP Connection
     * Posts data to requestURL with postDataParams
     * Fetches response stream
     * Builds and returns response as string
     */
    public static String performPostCall(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
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

    /**
     * Establishes a Socket Connection
     * Sends data to PORT with params
     * Fetches response stream
     * Builds and returns response as string
     */
    public static String performSocketCall(int PORT, String... params) {
        Socket socket = null;
        String response = "FAIL";
        try {
            InetAddress serverAddress = InetAddress.getByName(NetworkUtils.IoKi_HOST);
            socket = new Socket(serverAddress, PORT);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println(getSocketParamString(params));
            response = (new BufferedReader(new InputStreamReader(socket.getInputStream()))).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
