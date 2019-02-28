package net.christopherwhite.lender.lender;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class activity_all_items extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {
    public static final String ITEM_VIEW = "net.christopherwhite.lender.lender.ITEMVIEW";
    private static final String TAG = "";
    RecyclerViewAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        ItemsDBHandler dbHandler = new ItemsDBHandler(context);
        // String lines[] = dbHandler.loadHandler().split("\\r?\\n");
        List<Item> items = dbHandler.loadAllHandler();

        // data to populate the RecyclerView with
        ArrayList<String> itemNames = new ArrayList<>();
        itemNames.add("Book");
        itemNames.add("Shovel");
        itemNames.add("Car");
        itemNames.add("Boat");
        itemNames.add("Record");
        itemNames.add("Book2");
        itemNames.add("Shovel2");
        itemNames.add("Car2");
        itemNames.add("Boat2");
        itemNames.add("Record2");
        itemNames.add("Book3");
        itemNames.add("Shovel3");
        itemNames.add("Car3");
        itemNames.add("Boat3");
        itemNames.add("Record3");
        itemNames.add("Book4");
        itemNames.add("Shovel4");
        itemNames.add("Car4");
        itemNames.add("Boat4");
        itemNames.add("Record4");
        itemNames.add("Another Book");
        itemNames.add("Broom");
        itemNames.add("Bucket");
        itemNames.add("Phone");
        itemNames.add("Socks");
        itemNames.add("a");
        itemNames.add("b");
        itemNames.add("c");
        itemNames.add("d");
        itemNames.add("e");
        itemNames.add("f");
        itemNames.add("1");
        itemNames.add("2");
        itemNames.add("3");
        itemNames.add("4");
        itemNames.add("5");
        // itemNames.addAll(Arrays.asList(lines));

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAllItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // adapter = new RecyclerViewAdapter(this, itemNames);
        adapter = new RecyclerViewAdapter(this, items);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ItemViewActivity.class);
        int message = position;
        intent.putExtra(ITEM_VIEW, message);
        Log.d(TAG, "position " + Integer.valueOf(position));
        startActivity(intent);
        // Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

/*    public void displayItemDetails(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        EditText editText = (EditText) findViewById(R.id.);
        String message = adapter.getItem(position);
        intent.putExtra(ADD_ITEM, message);
        startActivity(intent);
    }*/
}

