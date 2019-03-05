package net.christopherwhite.lender.lender;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

public class ArchivedItemsViewActivity extends AppCompatActivity {
    private static final String TAG = ItemViewActivity.class.getSimpleName();
    private ItemsDBHandler mDatabase;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_item_view);
        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);
        RecyclerView itemView = (RecyclerView)findViewById(R.id.archived_items_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        itemView.setLayoutManager(linearLayoutManager);
        itemView.setHasFixedSize(true);
        mDatabase = new ItemsDBHandler(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        List<Item> allItems = mDatabase.loadAllArchivedHandler();
        if(allItems.size() > 0){
            itemView.setVisibility(View.VISIBLE);
            ItemArchiveAdapter mAdapter = new ItemArchiveAdapter(this, allItems);
            itemView.setAdapter(mAdapter);
        }else {
            itemView.setVisibility(View.GONE);
            Toast.makeText(this, "There is nothing in the archive.", Toast.LENGTH_LONG).show();
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(
                Color.parseColor("#4285f4"));
        actionbar.setBackgroundDrawable(colorDrawable);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        FragmentAddItem fragment = new FragmentAddItem();
                        switch (menuItem.getItemId()) {
                            case R.id.all_items:
                                // User chose the "all_items" item, show the app settings UI...
                                menuItem.setChecked(true);
                                // Intent all_items_intent = new Intent(MainActivity.this, activity_all_items.class);
                                Intent all_items_intent = new Intent(ArchivedItemsViewActivity.this, ItemViewActivity.class);
                                startActivity(all_items_intent);
                                drawerLayout.closeDrawers();
                                return true;

                            /*case R.id.add_item:
                                // User chose the "add_item" action, mark the current item
                                menuItem.setChecked(true);
                                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                                startActivity(intent);
                                // break;
                                drawerLayout.closeDrawers();
                                return true;

                            case R.id.find_item:
                                // User chose the "add_item" action, mark the current item
                                menuItem.setChecked(true);

                                drawerLayout.closeDrawers();
                                return true;*/

                            case R.id.show_archived_items:
                                // User chose the "view archived items" item, show the app settings UI...
                                menuItem.setChecked(true);
                                // Intent all_items_intent = new Intent(MainActivity.this, activity_all_items.class);
                                Intent archived_items_intent = new Intent(ArchivedItemsViewActivity.this, ArchivedItemsViewActivity.class);
                                startActivity(archived_items_intent);
                                drawerLayout.closeDrawers();
                                return true;

                            default:
                                // set item as selected to persist highlight
                                menuItem.setChecked(true);

                                // close drawer when item is tapped
                                drawerLayout.closeDrawers();
                                // fragmentForChange
                                // Add code here to update the UI based on the item selected
                                // For example, swap UI fragments here
                        }



                        return true;
                    }
                });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }
}