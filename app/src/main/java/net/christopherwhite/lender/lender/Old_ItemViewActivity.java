package net.christopherwhite.lender.lender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Old_ItemViewActivity extends AppCompatActivity {

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
        setContentView(R.layout.content_item_view);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String message = intent.getStringExtra(activity_all_items.ITEM_VIEW);

        ItemsDBHandler dbHandler = new ItemsDBHandler(this);
        // Item item = dbHandler.findByIDHandler(Integer.parseInt(message));
        Item item = new Item();
        if (item.getItemID() >= 1) {
            Toast.makeText(this,
                    item.toString() + " Found", Toast.LENGTH_LONG).show();

            // textItemName.setText(item.getItemName());
            // textItemDescription.setText(item.getItemDescription());
            // textBorrowerName.setText(item.getBorrowerName());
            // textBorrowerEmail.setText(item.getBorrowerEmail());
            // text_View_Id.setText(String.valueOf(item.getItemID()));
            // dateDateLent.setText("");
            // dateReturnDate.setText("");

        } else {
            Toast.makeText(this,
                    "No Match Found for " + message, Toast.LENGTH_LONG).show();
        }

    }


/*    public void findItem (View view) {
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
    }*/



}
