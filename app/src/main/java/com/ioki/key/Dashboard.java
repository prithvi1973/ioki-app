package com.ioki.key;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.Gravity.LEFT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ioki.key.MainActivity.getPreferenceObject;

public class Dashboard extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleBtn;
    private FloatingActionButton fab;
    private ProgressBar listItemProgressBar;
    private RecyclerView recyclerView;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Initializing Navigation Control
        Toolbar navToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(navToolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        toggleBtn = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        fab = findViewById(R.id.floatingActionButton);
        drawerLayout.addDrawerListener(toggleBtn);
        toggleBtn.syncState();
        ActionBar ab = getSupportActionBar();
        if(ab!=null) ab.setDisplayHomeAsUpEnabled(true);

        // Initializing recycler view and listItems
        recyclerView = findViewById(R.id.recyclerView);
        listItemProgressBar = findViewById(R.id.listItemProgressBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItemProgressBar.setVisibility(VISIBLE);
        listItems = new ArrayList<>();
    }

    @SuppressLint("RtlHardcoded")
    public void loadFrequentlyUsed(MenuItem item) {
        fab.setVisibility(GONE);
        new populateRecyclerViewTask("locks", listItems, recyclerView, listItemProgressBar).execute("1");
        drawerLayout.closeDrawer(LEFT);
    }

    @SuppressLint("RtlHardcoded")
    public void loadLocks(MenuItem item) {
        fab.setVisibility(VISIBLE);
        new populateRecyclerViewTask("locks", listItems, recyclerView, listItemProgressBar).execute("1");
        drawerLayout.closeDrawer(LEFT);
    }

    @SuppressLint("RtlHardcoded")
    public void loadCredentials(MenuItem item) {
        fab.setVisibility(VISIBLE);
        new populateRecyclerViewTask("credentials", listItems, recyclerView, listItemProgressBar).execute("1");
        drawerLayout.closeDrawer(LEFT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav,menu);
        loadFrequentlyUsed(menu.findItem(R.id.navBar_quick));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggleBtn.onOptionsItemSelected(item);
    }

    public void logout(MenuItem item){
        Intent intent = new Intent(this, MainActivity.class);
        getPreferenceObject().removeAllSharedPreferences();
        startActivity(intent);
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    // Inner class to invoke POST request for fetching list items
    public static class populateRecyclerViewTask extends AsyncTask<String, Void, String> {

        // Stores response from server
        private String requestType;
        private String response = "";
        private RecyclerView recyclerView;
        private List<ListItem> listItems;
        private ProgressBar listItemProgressBar;

        private populateRecyclerViewTask(String requestType, List<ListItem> listItems, RecyclerView recyclerView, ProgressBar listItemProgressBar) {
            this.requestType = requestType;
            this.listItems = listItems;
            this.recyclerView = recyclerView;
            this.listItemProgressBar = listItemProgressBar;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("submit","1");

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + requestType + "/";

            // Fetching response using utility function
            response = NetworkUtils.performPostCall(requestURL,dataParams);
            return response;
        }

        private void populateDummyListItems() {
            for(int i=0; i<3; i++)
                listItems.add(new ListItem(requestType.toUpperCase() + " Dummy " + (i+1),"Lock/Credential ID" + (i+1), requestType));
        }

        @Override
        protected void onPostExecute(String queryResults) {
            listItems.clear();
            Log.d("ioki-debug", requestType.toUpperCase() + " JSON Response: " + response);
            if (response!= null && !response.equals("")) {
                try {
                    JSONObject responseJSON = new JSONObject(response);

                    if(requestType.equals("locks")) {
                        JSONArray listItemArray = responseJSON.getJSONArray("data");
                        for (int i = 0; i < listItemArray.length(); i++) {
                            JSONObject listItemObject = listItemArray.getJSONObject(i);
                            Log.d("ioki-debug", listItemObject.getString("name") + " | " + listItemObject.getString("id"));
                            listItems.add(new ListItem(listItemObject.getString("name"), listItemObject.getString("id"), requestType));
                        }
                    }
                    else if(requestType.equals("credentials")) {
                        JSONArray encrypted = responseJSON.getJSONObject("data").getJSONArray("encrypted");
                        JSONArray decrypted = responseJSON.getJSONObject("data").getJSONArray("decrypted");
                        for(int i=0; i < encrypted.length(); i++) {
                            JSONObject enc = encrypted.getJSONObject(i);
                            JSONObject dec = decrypted.getJSONObject(i);
                            listItems.add(new ListItem(dec.getString("login"), enc.getString("link"), requestType));
                        }
                    }
                } catch (JSONException e) {populateDummyListItems();}
            }
            else populateDummyListItems();
            listItemProgressBar.setVisibility(GONE);
            recyclerView.setAdapter(new ListItemAdapter(listItems));
        }
    }

 }
