package com.ioki.key;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

public class Dashboard extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleBtn;
    private android.support.v7.widget.Toolbar navToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        navToolbar = findViewById(R.id.nav_action);
        setSupportActionBar(navToolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        toggleBtn = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggleBtn);
        toggleBtn.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
//        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.navBar_locks){
            Toast.makeText(this, "Lock is selected", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId()==R.id.navBar_credentials){
            Toast.makeText(this, "Credentials", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId()==R.id.navBar_settings){
            Toast.makeText(this, "Go to Settings", Toast.LENGTH_SHORT).show();
        }
        return toggleBtn.onOptionsItemSelected(item);
    }
}
