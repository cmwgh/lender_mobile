package net.christopherwhite.lender.lender;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
            textItemName.setText("");
            textItemDescription.setText("");
            textBorrowerName.setText("");
            textBorrowerEmail.setText("");
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
                "Items Loaded", Toast.LENGTH_LONG).show();
    }
/*
    public void deleteStudent(View view) {
        ItemsDBHandler dbHandler = new ItemsDBHandler(this, null,
                null, 1);
        boolean result = dbHandler.deleteHandler(Integer.parseInt(
                studentid.getText().toString()));
        if (result) {
            studentid.setText("");
            studentname.setText("");
            lst.setText("Record Deleted");
        } else
            studentid.setText("No Match Found");
    }

    public void updateStudent(View view) {
        ItemsDBHandler dbHandler = new ItemsDBHandler(this, null,
                null, 1);
        boolean result = dbHandler.updateHandler(Integer.parseInt(
                studentid.getText().toString()), studentname.getText().toString());
        if (result) {
            studentid.setText("");
            studentname.setText("");
            lst.setText("Record Updated");
        } else
            studentid.setText("No Match Found");
    }*/


}