package com.ioki.key;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.Gravity.LEFT;

public class Dashboard extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleBtn;
    private FloatingActionButton fab;

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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
    }

    @SuppressLint("RtlHardcoded")
    public void loadFrequentlyUsed(MenuItem item) {
        fab.setVisibility(View.GONE);
        new populateRecyclerViewTask(this, "frequent", listItems, recyclerView).execute("1","2");
        drawerLayout.closeDrawer(LEFT);
    }

    @SuppressLint("RtlHardcoded")
    public void loadLocks(MenuItem item) {
        fab.setVisibility(View.VISIBLE);
        new populateRecyclerViewTask(this, "locks", listItems, recyclerView).execute("1","2");
        drawerLayout.closeDrawer(LEFT);
    }

    @SuppressLint("RtlHardcoded")
    public void loadCredentials(MenuItem item) {
        fab.setVisibility(View.VISIBLE);
        new populateRecyclerViewTask(this, "credentials", listItems, recyclerView).execute("1","2");
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

    @SuppressLint("StaticFieldLeak")
    // Inner class to invoke POST request for fetching list items
    public static class populateRecyclerViewTask extends AsyncTask<String, Void, String> {

        // Stores response from server
        private String requestType;
        private String response = "";
        private Context context;
        private RecyclerView recyclerView;
        private List<ListItem> listItems;

        private populateRecyclerViewTask(Context context, String requestType, List<ListItem> listItems, RecyclerView recyclerView) {
            this.context = context;
            this.requestType = requestType;
            this.listItems = listItems;
            this.recyclerView = recyclerView;
        }

        @Override
        protected String doInBackground(String... params) {
            // Making map of POST parameters
            HashMap<String, String> dataParams = new HashMap<>();
            dataParams.put("username", params[0]);
            dataParams.put("password",params[1]);
            dataParams.put("submit","1");

            // Generating URL for POST request
            String requestURL = NetworkUtils.IoKi_BASE_URL + requestType + "/";

            // Fetching response using utility function
            response = NetworkUtils.performPostCall(requestURL,dataParams);
            return response;
        }

        private void populateDummyListItems() {
            for(int i=0; i<3; i++)
                listItems.add(new ListItem(requestType + " Dummy " + (i+1),"Data not received from server in required format. Populating recycler view with dummy list items."));
        }

        @Override
        protected void onPostExecute(String queryResults) {
            listItems.clear();
            if (response!= null && !response.equals("")) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    JSONArray listItemArray = responseJSON.getJSONArray("listItems");
                    for(int i=0; i<listItemArray.length(); i++) {
                        JSONObject listItemObject = listItemArray.getJSONObject(i);
                        listItems.add(new ListItem(listItemObject.getString("heading"), listItemObject.getString("description ")));
                    }
                } catch (JSONException e) {populateDummyListItems();}
            }
            else populateDummyListItems();
            recyclerView.setAdapter(new ListItemAdapter(listItems, context));
        }
    }
 }
