package net.christopherwhite.lender.lender;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

public class ArchivedItemsViewActivity extends AppCompatActivity {
    private static final String TAG = ItemViewActivity.class.getSimpleName();
    private ItemsDBHandler mDatabase;

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
        List<Item> allItems = mDatabase.loadAllArchivedHandler();
        if(allItems.size() > 0){
            itemView.setVisibility(View.VISIBLE);
            ItemArchiveAdapter mAdapter = new ItemArchiveAdapter(this, allItems);
            itemView.setAdapter(mAdapter);
        }else {
            itemView.setVisibility(View.GONE);
            Toast.makeText(this, "There is nothing in the archive.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }
}