package com.ioki.key;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleBtn;
    private android.support.v7.widget.Toolbar navToolbar;
    private Menu navMenu;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Initializing Side Nav and toggle button
        navToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(navToolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        toggleBtn = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
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

    public void loadFrequentlyUsed(MenuItem item) {
        listItems.clear();

        // TODO: Fetch Frequently Used JSON
        for(int i=0; i<3; i++) {
            ListItem listItem = new ListItem("Frequent Used " + (i+1),"Description " + (i+1));
            listItems.add(listItem);
        }

        adapter = new ListItemAdapter(listItems, this);
        recyclerView.setAdapter(adapter);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void loadLocks(MenuItem item) {
        listItems.clear();

        // TODO: Fetch Locks JSON
        for(int i=0; i<4; i++) {
            ListItem listItem = new ListItem("Lock " + (i+1),"Description " + (i+1));
            listItems.add(listItem);
        }

        adapter = new ListItemAdapter(listItems, this);
        recyclerView.setAdapter(adapter);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void loadCredentials(MenuItem item) {
        listItems.clear();

        // TODO: Fetch Credentials JSON
        for(int i=0; i<10; i++) {
            ListItem listItem = new ListItem("Credential " + (i+1),"Description " + (i+1));
            listItems.add(listItem);
        }

        adapter = new ListItemAdapter(listItems, this);
        recyclerView.setAdapter(adapter);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav,menu);
        navMenu = menu;
        loadFrequentlyUsed(navMenu.findItem(R.id.navBar_quick));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggleBtn.onOptionsItemSelected(item);
    }

}
