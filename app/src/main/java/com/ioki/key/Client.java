package com.ioki.key;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends AsyncTask<String, Void, Void> {
    private static final String HOST = NetworkUtils.IoKi_HOST;
    private static final int PORT = 9292;
    private ArrayList<String> response;

    public Client(ArrayList<String> response) {
        Log.d("ioki-app","Hello");
        this.response = response;
    }

    @Override
    protected Void doInBackground(String... args) {
        String response = "FAIL";
        Log.d("ioki-app", "1");
        Socket socket = null;
        Log.d("ioki-app", "2");

        try {
            // establishing connection
            InetAddress serverAddress = InetAddress.getByName(HOST);
            Log.d("ioki-app", serverAddress.toString());
            socket = new Socket(serverAddress, PORT);
            Log.d("ioki-app", "4");

            // sending command
            (new PrintWriter(socket.getOutputStream(), true)).println(args[0] + ":" + args[1] + ":" + args[2]);
            Log.d("ioki-app", "5");

            // checking response
            response = (new BufferedReader(new InputStreamReader(socket.getInputStream()))).readLine();
            Log.d("ioki-app", response);
            Log.d("ioki-app", args.toString());
            if (args[2].equals("2"))
                if (response.equals("L"))
                    response = "Locked";
                else if (response.equals("U"))
                    response = "Unlocked";
                else response = "FAIL";

        } catch (Exception e) {
            e.printStackTrace();
        }

        // closing socket connection
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // returning result
        this.response.add(response);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}