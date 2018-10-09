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
    public void onBindViewHolder(@NonNull ListItemAdapter.ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);
        holder.heading.setText(listItem.getHeading());
        holder.description.setText(listItem.getDescription());
        if(listItem.getRequestType().equals("locks")) holder.listItemActionButton.setVisibility(View.VISIBLE);
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
