package com.ioki.key;

import android.annotation.SuppressLint;
import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.ioki.key.MainActivity.RESPONSE;
import static com.ioki.key.MainActivity.getPreferenceObject;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    ListItemAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final ListItem listItem = listItems.get(position);
        holder.heading.setText(listItem.getHeading());
        holder.description.setText(listItem.getDescription());

        switch (listItem.getRequestType()) {
            case "credentials":
                holder.listItemActionButton.setVisibility(View.GONE);
                break;
            case "locks":
                final String username = getPreferenceObject().getPreferences(MainActivity.USERNAME);
                final String lockID = listItem.getId();
                new updateLockStatusTask(holder,listItem).execute(username, lockID);

                holder.listItemActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String command = "2";
                        switch (listItem.getLockStatus()) {
                            case "L":
                                command = "0";
                                break;
                            case "U":
                                command = "1";
                                break;
                            case "O":
                                Toast.makeText(v.getContext(), "Lock is Offline", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        new toggleLockStatusTask(holder,listItem,v.getContext()).execute(username, lockID, command);
                    }
                });
                holder.listItemActionButton.setVisibility(View.VISIBLE);
                break;
            default:
                holder.listItemActionButton.setVisibility(View.GONE);
                break;
        }


        holder.deleteItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteItemTask(view,listItems,position,context).execute(listItem.getRequestType(), listItem.getId());
            }
        });

        holder.shareItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = listItem.getId();
                switch(listItem.getRequestType()){
                    case "credentials":
                        Intent cintent = new Intent(context, UpdateCredential.class);
                        cintent.putExtra("LOGIN", id);
                        context.startActivity(cintent);
                        break;
                    case "locks":
                        Intent lintent = new Intent(context, UpdateLock.class);
                        lintent.putExtra("LOCKID", id);
                        context.startActivity(lintent);
                        break;
                }
            }
        });

        holder.updateItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = listItem.getRequestType();
                String id = listItem.getId();
                switch(type){
                    case "credentials":
                        Intent credUpdateIntent = new Intent(context, UpdateCredential.class);
                        credUpdateIntent.putExtra("LOGIN", id);
                        context.startActivity(credUpdateIntent);
                        break;

                    case "locks":
                        Intent lockUpdateIntent = new Intent(context, UpdateLock.class);
                        lockUpdateIntent.putExtra("LOCKID", id);
                        context.startActivity(lockUpdateIntent);
                        break;
                }
            }
        });

        holder.shareItemActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareAccessIntent = new Intent(context, ShareAccess.class);
                shareAccessIntent.putExtra("ID", listItem.getId());
                shareAccessIntent.putExtra("TYPE", listItem.getRequestType());
                context.startActivity(shareAccessIntent);
            }
        });
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
            switch (status) {
                case "L":
                    listItemActionButton.setImageResource(R.mipmap.baseline_lock_open_black_24);
                    break;
                case "U":
                    listItemActionButton.setImageResource(R.mipmap.baseline_lock_black_24);
                    break;
                case "O":
                    listItemActionButton.setImageResource(R.mipmap.baseline_offline_bolt_24);
                    break;
                default:
                    listItemActionButton.setImageResource(R.mipmap.baseline_error_outline_24);
                    break;
            }
        }
    }

    // Inner class to invoke POST request for user/credentials/add
    private static class deleteItemTask extends AsyncTask<String, Void, String> {

        private String response;
        @SuppressLint("StaticFieldLeak")
        private Context context;
        private List<ListItem> listItems;
        private int pos;
        View v;

        deleteItemTask(View v, List<ListItem> listItems, int pos, Context context) {
            this.response = "";
            this.v = v;
            this.listItems = listItems;
            this.pos = pos;
            this.context = context;
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
                JSONObject json;
                try {
                    json = new JSONObject(response);
                    getPreferenceObject().saveData(RESPONSE, response);
                    String message = json.getJSONArray("messages").getJSONObject(0).getString("message");
                    this.listItems.remove(this.pos);
                    Toast.makeText(v.getContext(),message,Toast.LENGTH_LONG).show();

                    // TODO: Correct this brute force approach to refresh activity
                    // In ShareAccess finish() is called as no intent params are passed
                    // thus refreshing Dashboard activity
                    Intent refreshDashIntent = new Intent(context, ShareAccess.class);
                    context.startActivity(refreshDashIntent);

                } catch (JSONException e) {
                    Toast.makeText(v.getContext(),"Invalid Server Response",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // Inner class to invoke Socket request for lock status fetch process
    private static class updateLockStatusTask extends AsyncTask<String, Void, String> {

        private ViewHolder holder;
        private ListItem listItem;
        private String response = "FAIL";

        updateLockStatusTask(ViewHolder holder, ListItem listItem) {
            this.holder = holder;
            this.listItem = listItem;
        }

        @Override
        protected String doInBackground(String... params) {
            this.response = NetworkUtils.performSocketCall(NetworkUtils.LOCK_SERVER_COM_PORT,params[0],params[1],"2");
            return this.response;
        }

        protected void onPostExecute(String queryResults) {
            listItem.setLockStatus(response);
            holder.setListItemActionButton(response);
        }
    }

    // Inner class to invoke Socket request for lock status change process
    private static class toggleLockStatusTask extends AsyncTask<String, Void, String> {

        private ViewHolder holder;
        private ListItem listItem;
        @SuppressLint("StaticFieldLeak")
        private Context context;
        private String response = "FAIL";

        private String command;

        toggleLockStatusTask(ViewHolder holder, ListItem listItem, Context context) {
            this.holder = holder;
            this.listItem = listItem;
            this.command = "";
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            this.command = params[2];
            this.response = NetworkUtils.performSocketCall(NetworkUtils.LOCK_SERVER_COM_PORT,params[0],params[1],params[2]);
            return this.response;
        }

        protected void onPostExecute(String queryResults) {
            if (command.equals("0") && response.equals("PASS")) {
                listItem.setLockStatus("U");
                holder.setListItemActionButton("U");
                Toast.makeText(context,"Unlocked "+ listItem.getHeading(), Toast.LENGTH_SHORT).show();
            } else if (command.equals("1") && response.equals("PASS")) {
                listItem.setLockStatus("L");
                holder.setListItemActionButton("L");
                Toast.makeText(context,"Locked "+ listItem.getHeading(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
