package net.christopherwhite.lender.lender;

//werk voor de boek

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";
    TextView lst;
    TextView text_View_Id;
    EditText textItemName;
    EditText textItemDescription;
    EditText textBorrowerName;
    EditText textBorrowerEmail;
    EditText dateDateLent;
    EditText dateReturnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lst = findViewById(R.id.list);
        text_View_Id = findViewById(R.id.textViewId);
        textItemName = findViewById(R.id.textItemName);
        textItemDescription = findViewById(R.id.textItemDescription);
        textBorrowerName = findViewById(R.id.textBorrowerName);
        textBorrowerEmail = findViewById(R.id.textBorrowerEmail);
        dateDateLent = findViewById(R.id.dateDateLent);
        dateReturnDate = findViewById(R.id.dateReturnDate);
    }

    public void addItem (View view) {
        ItemsDBHandler dbHandler = new ItemsDBHandler(this, null, null, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    public void findItem (View view) {
        ItemsDBHandler dbHandler = new ItemsDBHandler(this, null, null, 1);
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
        ItemsDBHandler dbHandler = new ItemsDBHandler(this, null, null, 1);
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
/*
    public void deleteItem(View view) {
        ItemsDBHandler dbHandler = new ItemsDBHandler(this, null,
                null, 1);
        boolean result = dbHandler.deleteHandler(Integer.parseInt(
                itemId.getText().toString()));
        if (result) {
            textItemName.setText("");
            textItemDescription.setText("");
            textBorrowerName.setText("");
            textBorrowerEmail.setText("");
            dateDateLent.setText("");
            dateReturnDate.setText("");
            Toast.makeText(MainActivity.this,
            Integer.parseInt(
                itemId.getText().toString()) + " Item Deleted", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(MainActivity.this,
            "Item Not Found", Toast.LENGTH_LONG).show();
    }
*/
    public void updateItem(View view) {
        if (text_View_Id != null) {
            ItemsDBHandler dbHandler = new ItemsDBHandler(this, null,
                    null, 1);



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
// https://www.androidhive.info/2014/07/android-custom-listview-with-image-and-text-using-volley/
}