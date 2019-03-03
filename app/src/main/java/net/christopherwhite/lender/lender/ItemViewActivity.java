package net.christopherwhite.lender.lender;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.christopherwhite.lender.lender.Item;
import net.christopherwhite.lender.lender.ItemAdapter;
import net.christopherwhite.lender.lender.ItemsDBHandler;
import net.christopherwhite.lender.lender.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemViewActivity extends AppCompatActivity {
    private static final String TAG = ItemViewActivity.class.getSimpleName();
    private ItemsDBHandler mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);
        RecyclerView itemView = (RecyclerView)findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        itemView.setLayoutManager(linearLayoutManager);
        itemView.setHasFixedSize(true);
        mDatabase = new ItemsDBHandler(this);
        List<Item> allItems = mDatabase.loadAllHandler();
        if(allItems.size() > 0){
            itemView.setVisibility(View.VISIBLE);
            ItemAdapter mAdapter = new ItemAdapter(this, allItems);
            itemView.setAdapter(mAdapter);
        }else {
            itemView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no product in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add new quick task
                addTaskDialog();
            }
        });
    }
    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_item_layout, null);
        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText descriptionField = (EditText)subView.findViewById(R.id.enter_description);
        final EditText borrowerName = (EditText)subView.findViewById(R.id.borrower_name);
        final EditText borrowerEmail = (EditText)subView.findViewById(R.id.borrower_email);
        final EditText dateDateLent = (EditText)subView.findViewById(R.id.date_lent);
        final EditText dateDateReturned = (EditText)subView.findViewById(R.id.date_returned);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new item");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD ITEM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                final String name = nameField.getText().toString();
                final String description = descriptionField.getText().toString();
                final String borrower = borrowerName.getText().toString();
                final String borrowerMail = borrowerEmail.getText().toString();
                Date dateLent = null;
                // Date dateLent = new Date();
                long displayDateLent;

                try {
                    dateLent = dateFormat.parse(dateDateLent.getText().toString());
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing DateLent datetime failed", e);
                }

                if (dateLent != null){
                    displayDateLent = dateLent.getTime();
                }

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 1);
                Date returnDate = cal.getTime();
                try {
                    returnDate = dateFormat.parse(dateDateReturned.getText().toString());
                } catch (ParseException e) {
                    Log.e(TAG, "Parsing ReturnDate datetime failed", e);
                }

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(ItemViewActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{

                    Item item = new Item(name,description,borrower,borrowerMail,dateLent,returnDate);
                    mDatabase.addHandler(item);
                    Toast.makeText(ItemViewActivity.this, "Updated (not really)", Toast.LENGTH_LONG).show();
                    //refresh the activity
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ItemViewActivity.this, "Task cancelled!!", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }
}