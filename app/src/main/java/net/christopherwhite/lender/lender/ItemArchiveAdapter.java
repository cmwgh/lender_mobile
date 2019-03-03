package net.christopherwhite.lender.lender;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemArchiveAdapter extends RecyclerView.Adapter<ItemViewHolder>{
    private static final String TAG = ItemArchiveAdapter.class.getSimpleName();
    private Context context;
    private List<Item> listItems;
    private ItemsDBHandler mDatabase;

    public ItemArchiveAdapter(Context context, List<Item> listItems) {
        this.context = context;
        this.listItems = listItems;
        mDatabase = new ItemsDBHandler(context);
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.archive_item_list_layout, parent, false);
        return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Item singleItem = listItems.get(position);
        holder.name.setText(singleItem.getItemName());
        holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(singleItem);
            }
        });
        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
                mDatabase.unArchiveHandler(singleItem.getItemID());
                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private void editTaskDialog(final Item item){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_item_layout, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText descriptionField = (EditText)subView.findViewById(R.id.enter_description);
        final EditText borrowerName = (EditText)subView.findViewById(R.id.borrower_name);
        final EditText borrowerEmail = (EditText)subView.findViewById(R.id.borrower_email);
        final EditText dateDateLent = (EditText)subView.findViewById(R.id.date_lent);
        final EditText dateReturnDate = (EditText)subView.findViewById(R.id.date_returned);
        if(item != null){
            nameField.setText(item.getItemName());
            descriptionField.setText(item.getItemDescription());
            borrowerName.setText(item.getBorrowerName());
            borrowerEmail.setText(item.getBorrowerEmail());

            dateDateLent.setText(dateFormat.format(item.getDateLent()));
            dateReturnDate.setText(dateFormat.format(item.getReturnDate()));
            //quantityField.setText(String.valueOf(item.getItemID()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Item");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("UnArchive ITEM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabase.unArchiveHandler(item.getItemID());

                    Toast.makeText(context, "Item returned to active list", Toast.LENGTH_LONG).show();
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }

        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled!", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}