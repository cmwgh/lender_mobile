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
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>{
    private Context context;
    private List<Item> listItems;
    private ItemsDBHandler mDatabase;

    public ItemAdapter(Context context, List<Item> listItems) {
        this.context = context;
        this.listItems = listItems;
        mDatabase = new ItemsDBHandler(context);
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_layout, parent, false);
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
                mDatabase.deleteHandler(singleItem.getItemID());
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText descriptionField = (EditText)subView.findViewById(R.id.enter_description);
        final EditText borrowersName = (EditText)subView.findViewById(R.id.borrower_name);
        final EditText borrowersEmail = (EditText)subView.findViewById(R.id.borrower_email);
        final EditText dateLent = (EditText)subView.findViewById(R.id.date_lent);
        final EditText returnDate = (EditText)subView.findViewById(R.id.date_returned);
        if(item != null){
            nameField.setText(item.getItemName());
            descriptionField.setText(item.getItemDescription());
            borrowersName.setText(item.getBorrowerName());
            borrowersEmail.setText(item.getBorrowerEmail());

            //dateLent.setText(dateFormat().item.getDateLent());
            //returnDate.setText(item.getReturnDate());
            //quantityField.setText(String.valueOf(item.getItemID()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit item");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("EDIT ITEM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                //final int quantity = Integer.parseInt(quantityField.getText().toString());
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context, "Updated (not really)", Toast.LENGTH_LONG).show();
                    // mDatabase.updateHandler(new Item(item.getItemID(), name)));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}