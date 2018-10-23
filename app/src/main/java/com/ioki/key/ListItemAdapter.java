package com.ioki.key;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.ioki.key.MainActivity.RESPONSE;
import static com.ioki.key.MainActivity.getPreferenceObject;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private List<ListItem> listItems;

    ListItemAdapter(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemAdapter.ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        holder.heading.setText(listItem.getHeading());
        holder.description.setText(listItem.getDescription());
        if(listItem.getRequestType().equals("credentials")) holder.listItemActionButton.setVisibility(View.GONE);

        else if (listItem.getRequestType().equals("locks")) {
            final String username = getPreferenceObject().getPreferences(MainActivity.USERNAME);
            final String lockID = listItem.getId();

            String status = sendSocketRequest(username, lockID, "2");
            Log.d("ioki-app",status);
            listItem.setLockStatus(status);
            holder.setListItemActionButton(status);
            String command = "";
            if(status.equals("L")) command = "0";
            else if(status.equals("U")) command = "1";
            final String finalCommand = command;
            holder.listItemActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String response = sendSocketRequest(username,lockID, finalCommand);
                    if (finalCommand.equals("0") && response.equals("PASS")) {
                        holder.listItemActionButton.setImageResource(R.mipmap.baseline_lock_open_black_24);
                        listItem.setLockStatus("0");
                    } else if (finalCommand.equals("1") && response.equals("PASS")) {
                        holder.listItemActionButton.setImageResource(R.mipmap.baseline_lock_black_24);
                        listItem.setLockStatus("1");
                    } else if (finalCommand.equals("2")) {
                        if (response.equals("L")) {
                            holder.listItemActionButton.setImageResource(R.mipmap.baseline_lock_black_24);
                            listItem.setLockStatus("1");
                        } else if (response.equals("U")) {
                            holder.listItemActionButton.setImageResource(R.mipmap.baseline_lock_open_black_24);
                            listItem.setLockStatus("0");
                        }
                    }
                }
            });
            holder.listItemActionButton.setVisibility(View.VISIBLE);
        }

        else holder.listItemActionButton.setVisibility(View.GONE);


        holder.deleteItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteItemTask(view).execute(listItem.getRequestType(), listItem.getId());
            }
        });

        holder.updateItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
            }
        });
    }

    private String sendSocketRequest(String userID, String lockID, String command) {
        ArrayList<String> response = new ArrayList<>();
        String[] arr = new String[3];
        arr[0] = userID;
        arr[1] = lockID;
        arr[2] = command;
        (new Client(response)).execute(arr);
        return response.get(0);
    }

    private void def() {
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView heading;
        TextView description;

        ImageButton listItemActionButton;
        ImageButton updateItemActionButton;
        ImageButton shareItemActionButton;
        ImageButton deleteItemActionButton;

        ViewHolder(View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.listItemHeading);
            description = itemView.findViewById(R.id.listItemDescription);

            listItemActionButton = itemView.findViewById(R.id.listItemActionButton);
            updateItemActionButton = itemView.findViewById(R.id.updateItemActionButton);
            shareItemActionButton = itemView.findViewById(R.id.shareItemActionButton);
            deleteItemActionButton = itemView.findViewById(R.id.deleteItemActionButton);
        }

        public void setListItemActionButton(String status) {
            if(status.equals("L")) listItemActionButton.setImageResource(R.mipmap.baseline_lock_black_24);
            else if(status.equals("U")) listItemActionButton.setImageResource(R.mipmap.baseline_lock_open_black_24);
            else if(status.equals("FAIL")) listItemActionButton.setImageResource(R.mipmap.baseline_offline_bolt_24);
        }
    }

    // Inner class to invoke POST request for user/credentials/add
    private static class deleteItemTask extends AsyncTask<String, Void, String> {

        private String response;
        View v;

        public deleteItemTask(View v) {
            this.response = "";
            this.v = v;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("submit","1");

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + params[0] + "/delete/" + params[1];
            Log.d("ioki-debug", requestURL);

            // Fetching response using utility function
            response = NetworkUtils.performPostCall(requestURL,dataParams);
            return response;
        }

        protected void onPostExecute(String queryResults) {
            if (response != null && !response.equals("")) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    getPreferenceObject().saveData(RESPONSE, response);

                    String status = json.getJSONArray("messages").getJSONObject(0).getString("type");
                    String message = json.getJSONArray("messages").getJSONObject(0).getString("message");
                    Toast.makeText(v.getContext(),message,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Toast.makeText(v.getContext(),"Invalid Server Response",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
