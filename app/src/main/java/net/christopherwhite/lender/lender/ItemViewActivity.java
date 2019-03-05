package net.christopherwhite.lender.lender;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import net.christopherwhite.lender.lender.Item;
import net.christopherwhite.lender.lender.ItemAdapter;
import net.christopherwhite.lender.lender.ItemsDBHandler;
import net.christopherwhite.lender.lender.R;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemViewActivity extends MainActivity {
    private static final String TAG = ItemViewActivity.class.getSimpleName();
    private ItemsDBHandler mDatabase;
    private DrawerLayout drawerLayout;
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
        drawerLayout = findViewById(R.id.drawer_layout);
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
                                Intent all_items_intent = new Intent(ItemViewActivity.this, ItemViewActivity.class);
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
                                Intent archived_items_intent = new Intent(ItemViewActivity.this, ArchivedItemsViewActivity.class);
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
    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_item_layout, null);
        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText descriptionField = (EditText)subView.findViewById(R.id.enter_description);
        final EditText borrowerName = (EditText)subView.findViewById(R.id.borrower_name);
        final EditText borrowerEmail = (EditText)subView.findViewById(R.id.borrower_email);
        final EditText dateDateLent = (EditText)subView.findViewById(R.id.date_lent);
        final EditText dateDateReturned = (EditText)subView.findViewById(R.id.date_returned);
        final ImageView imageView = subView.findViewById(R.id.myImageView);
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
                    Log.v(TAG, "returnDate —  " + returnDate);
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



    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        Log.v(TAG, "-- Start take picture intent --");
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.v(TAG, "photoURI —  " + photoFile.toString());
            } catch (IOException ex) {
                // Error occurred while creating the File
                // ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "net.christopherwhite.lender.lender.fileprovider",
                        photoFile);
                Log.v(TAG, "photoURI —  " + photoURI.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }



    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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