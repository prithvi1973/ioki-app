package com.ioki.key;

import android.os.AsyncTask;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends AsyncTask<Void, Void, String> {
    private static final String HOST = "localhost";
    private static final int PORT = 9292;
    private static final String userId = "p3R5gPSUFUI=";
    private static String lockId = "1";
    private int command = 0;
    String response;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public Client(AsyncResponse delegate, String lockId, int command, String response){
        this.delegate = delegate;
        lockId = lockId;
        command = command;
        response = response;
    }

    @Override
    protected String doInBackground(Void... arg0) {

        response = null;
        Socket socket = null;

        try {
            socket = new Socket(HOST, PORT);

            // getting input output streams
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // checking if the connection is established
            String serverInput = input.readLine();
            if (serverInput.equals("PASS")) {

                // sending command
                output.println(userId + ":" + lockId + ":" + command);

                // checking response
                response = input.readLine();
                if (response.equals("PASS"))
                    response = "Success";
                else if (command == 3) {
                    if (response.equals("L"))
                        response = "Locked";
                    else if (response.equals("U"))
                        response = "Unlocked";
                    else response = "Failure";
                } else response = "Failure";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // closing socket connection
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

            return response;
}

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}


// TODO:  in the on click method of lock, write this code
// TODO: this class will implement AsyncResponse
//    @Override
//    public void onClick(View arg0) {
//        // get lockid from response "lockid"
//        Scanner sc = new Scanner(System.in);
//        int command;
//        do {
//            String response;
//            command = sc.nextInt();
//            Client client = new Client(this, lockid, command, response);
//            client.execute();
//
//            // depending on the response, show toast and toggle the lock icon
//            //Possible responses: Success, Locked, Unlocked, Failure
//
//            //this override the implemented method from AsyncResponse
//              @Override
//              void processFinish(String output){
//                   //Here you will receive the result fired from async class
//                   //of onPostExecute(result) method.
//                   response = output;
//              }
//        } while (command != 0);
//    }
