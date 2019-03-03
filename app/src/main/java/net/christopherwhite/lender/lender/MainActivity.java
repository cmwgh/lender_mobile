package net.christopherwhite.lender.lender;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";
    public static final String ADD_ITEM = "net.christopherwhite.lender.lender.ADDITEM";

    TextView lst;
    TextView text_View_Id;
    EditText textItemName;
    EditText textItemDescription;
    EditText textBorrowerName;
    EditText textBorrowerEmail;
    EditText dateDateLent;
    EditText dateReturnDate;

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        lst = findViewById(R.id.list);
        text_View_Id = findViewById(R.id.textViewId);
        textItemName = findViewById(R.id.textItemName);
        textItemDescription = findViewById(R.id.textItemDescription);
        textBorrowerName = findViewById(R.id.textBorrowerName);
        textBorrowerEmail = findViewById(R.id.textBorrowerEmail);
        dateDateLent = findViewById(R.id.dateDateLent);
        dateReturnDate = findViewById(R.id.dateReturnDate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
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
                                Intent all_items_intent = new Intent(MainActivity.this, ItemViewActivity.class);
                                startActivity(all_items_intent);
                                drawerLayout.closeDrawers();
                                return true;

                            case R.id.add_item:
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
                                return true;

                            case R.id.show_archived_items:
                                // User chose the "view archived items" item, show the app settings UI...
                                menuItem.setChecked(true);
                                // Intent all_items_intent = new Intent(MainActivity.this, activity_all_items.class);
                                Intent archived_items_intent = new Intent(MainActivity.this, ArchivedItemsViewActivity.class);
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

    /** Called when the user taps the Add button */
    public void addItemIntent(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        EditText editText = (EditText) findViewById(R.id.textItemName);
        String message = editText.getText().toString();
        intent.putExtra(ADD_ITEM, message);
        startActivity(intent);
    }

    public void addItem (View view) {
        if(TextUtils.isEmpty(textItemName.getText())) {
            Toast.makeText(MainActivity.this,
                    "Mandatory fields must be filled in", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(textBorrowerName.getText())){
            Toast.makeText(MainActivity.this,
                    "Mandatory fields must be filled in", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(textBorrowerEmail.getText())) {
            Toast.makeText(MainActivity.this,
                    "Mandatory fields must be filled in", Toast.LENGTH_LONG).show();
        }
        else {
        ItemsDBHandler dbHandler = new ItemsDBHandler(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String itemName = textItemName.getText().toString();
        String itemDescription = textItemDescription.getText().toString();
        String borrowerName = textBorrowerName.getText().toString();
        String borrowerEmail = textBorrowerEmail.getText().toString();
        Date date = new Date();
        Date dateLent = date;

        try {
            dateLent = dateFormat.parse(dateDateLent.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, "Parsing DateLent datetime failed", e);
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date returnDate = cal.getTime();
        try {
            returnDate = dateFormat.parse(dateReturnDate.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, "Parsing ReturnDate datetime failed", e);
        }


        Item item = new Item(itemName,itemDescription,borrowerName,borrowerEmail,dateLent,returnDate);
        dbHandler.addHandler(item);
        textItemName.setText("");
        textItemDescription.setText("");
        textBorrowerName.setText("");
        textBorrowerEmail.setText("");
        dateDateLent.setText("");
        dateReturnDate.setText("");
        }


    }

    public void findItem (View view) {
        ItemsDBHandler dbHandler = new ItemsDBHandler(this);
        Item item = dbHandler.findHandler(textItemName.getText().toString());
        if (item != null) {
            lst.setText(String.valueOf(item.getItemID()) +" "+ item.getItemName());

            Toast.makeText(MainActivity.this,
                    item.getItemName() + " Found", Toast.LENGTH_LONG).show();

            textItemName.setText(item.getItemName());
            textItemDescription.setText(item.getItemDescription());
            textBorrowerName.setText(item.getBorrowerName());
            textBorrowerEmail.setText(item.getBorrowerEmail());
            text_View_Id.setText(String.valueOf(item.getItemID()));
            dateDateLent.setText("");
            dateReturnDate.setText("");

        } else {
            lst.setText("No Match Found");
            Toast.makeText(MainActivity.this,
                    "No Match Found", Toast.LENGTH_LONG).show();
        }
    }

    public void loadItems(View view) {
        ItemsDBHandler dbHandler = new ItemsDBHandler(this);
        lst.setText(dbHandler.loadHandler());
        textItemName.setText("");
        textItemDescription.setText("");
        textBorrowerName.setText("");
        textBorrowerEmail.setText("");
        dateDateLent.setText("");
        dateReturnDate.setText("");
        Toast.makeText(MainActivity.this,
                lst.getLineCount()-1 +" Items Loaded", Toast.LENGTH_LONG).show();
    }

    public void deleteItem(View view) {
        if (text_View_Id != null) {
            ItemsDBHandler dbHandler = new ItemsDBHandler(this);
            boolean result = dbHandler.deleteHandler(Integer.parseInt(
                    text_View_Id.getText().toString()));
            if (result) {
                Toast.makeText(MainActivity.this,
                        textItemName.getText().toString() + " has been removed", Toast.LENGTH_LONG).show();
                textItemDescription.setText("");
                textBorrowerName.setText("");
                textBorrowerEmail.setText("");
                dateDateLent.setText("");
                dateReturnDate.setText("");
                lst.setText("");
                textItemName.setText("");
                text_View_Id.setText("");
            } else
                Toast.makeText(MainActivity.this,
                        "Item Not Found", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MainActivity.this,
                    "No Item Selected", Toast.LENGTH_LONG).show();
        }
    }
    public void archiveItem(View view) {
        if (text_View_Id != null) {
            ItemsDBHandler dbHandler = new ItemsDBHandler(this);
            boolean result = dbHandler.archiveHandler(Integer.parseInt(
                    text_View_Id.getText().toString()));
            if (result) {
                Toast.makeText(MainActivity.this,
                        textItemName.getText().toString() + " has been archived", Toast.LENGTH_LONG).show();
                textItemDescription.setText("");
                textBorrowerName.setText("");
                textBorrowerEmail.setText("");
                dateDateLent.setText("");
                dateReturnDate.setText("");
                lst.setText("");
                textItemName.setText("");
                text_View_Id.setText("");
            } else
                Toast.makeText(MainActivity.this,
                        "Item Not Found", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MainActivity.this,
                    "No Item Selected", Toast.LENGTH_LONG).show();
        }
    }

    public void unArchiveItem(View view) {
        if (text_View_Id != null) {
            ItemsDBHandler dbHandler = new ItemsDBHandler(this);
            boolean result = dbHandler.unArchiveHandler(Integer.parseInt(
                    text_View_Id.getText().toString()));
            if (result) {
                Toast.makeText(MainActivity.this,
                        textItemName.getText().toString() + " has been un-archived", Toast.LENGTH_LONG).show();
                textItemDescription.setText("");
                textBorrowerName.setText("");
                textBorrowerEmail.setText("");
                dateDateLent.setText("");
                dateReturnDate.setText("");
                lst.setText("");
                textItemName.setText("");
                text_View_Id.setText("");
            } else
                Toast.makeText(MainActivity.this,
                        "Item Not Found", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MainActivity.this,
                    "No Item Selected", Toast.LENGTH_LONG).show();
        }
    }

    public void updateItem(View view) {
        if (text_View_Id != null) {
            ItemsDBHandler dbHandler = new ItemsDBHandler(this);



           // boolean result = dbHandler.updateHandler(Integer.parseInt(
           //         text_View_Id.getText().toString()), itemName);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int itemId = Integer.parseInt(text_View_Id.getText().toString());
            String itemName = textItemName.getText().toString();
            String itemDescription = textItemDescription.getText().toString();
            String borrowerName = textBorrowerName.getText().toString();
            String borrowerEmail = textBorrowerEmail.getText().toString();
            Date date = new Date();
            Date dateLent = date;

            try {
                dateLent = dateFormat.parse(dateDateLent.getText().toString());
            } catch (ParseException e) {
                Log.e(TAG, "Parsing DateLent datetime failed", e);
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1);
            Date returnDate = cal.getTime();
            try {
                returnDate = dateFormat.parse(dateReturnDate.getText().toString());
            } catch (ParseException e) {
                Log.e(TAG, "Parsing ReturnDate datetime failed", e);
            }



            Item updateItem = new Item(itemId,itemName,itemDescription,borrowerName,borrowerEmail,dateLent,returnDate);
            boolean result = dbHandler.updateHandler(updateItem);

            if (result) {
                Item item = dbHandler.findByIDHandler(Integer.parseInt(
                        text_View_Id.getText().toString()));
                lst.setText(String.valueOf(item.getItemID()) +" "+ item.getItemName());
                textItemName.setText(item.getItemName());
                textItemDescription.setText(item.getItemDescription());
                textBorrowerName.setText(item.getBorrowerName());
                textBorrowerEmail.setText(item.getBorrowerEmail());
                text_View_Id.setText(String.valueOf(item.getItemID()));
                dateDateLent.setText("");
                dateReturnDate.setText("");
                Toast.makeText(MainActivity.this,

                    "Item Updated", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this,

                    "No Match Found", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this,
                            "No Item Selected", Toast.LENGTH_LONG).show();
        }
}


    public void clearFields(View view) {
    lst.setText("");
    textItemName.setText("");
    textItemDescription.setText("");
    textBorrowerName.setText("");
    textBorrowerEmail.setText("");
    dateDateLent.setText("");
    dateReturnDate.setText("");
    Toast.makeText(MainActivity.this,
            "Content Cleared", Toast.LENGTH_LONG).show();
}

@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
    View v = getCurrentFocus();

    if (v != null &&
            (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
            v instanceof EditText &&
            !v.getClass().getName().startsWith("android.webkit.")) {
        int scrcoords[] = new int[2];
        v.getLocationOnScreen(scrcoords);
        float x = ev.getRawX() + v.getLeft() - scrcoords[0];
        float y = ev.getRawY() + v.getTop() - scrcoords[1];

        if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
            hideKeyboard(this);
    }
    return super.dispatchTouchEvent(ev);
}

    public static void hideKeyboard(MainActivity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
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


// https://www.androidhive.info/2014/07/android-custom-listview-with-image-and-text-using-volley/
}