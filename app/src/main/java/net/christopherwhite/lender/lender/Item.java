package net.christopherwhite.lender.lender;

import android.graphics.Bitmap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Item {
    // fields
    private int itemID;
    private String itemName;
    private String itemDescription;
    private String itemImage;
    private String borrowerName;
    private String borrowerEmail;
    private Date dateLent;
    private Date returnDate;
    private String verify;

    // constructors
    public Item() {}

    public Item(String itemName,
                String itemDescription,
                String borrowerName,
                String borrowerEmail,
                Date dateLent,
                Date returnDate
                ) {
        super();
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.borrowerName = borrowerName;
        this.borrowerEmail = borrowerEmail;
        this.dateLent = dateLent;
        this.returnDate = returnDate;
    }

    public Item(int itemID,
                String itemName,
                String itemDescription,
                String borrowerName,
                String borrowerEmail,
                Date dateLent,
                Date returnDate
    ) {
        super();
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.borrowerName = borrowerName;
        this.borrowerEmail = borrowerEmail;
        this.dateLent = dateLent;
        this.returnDate = returnDate;
    }


    public Item(int itemID,
                String itemName,
                String itemDescription,
                String itemImage, // https://developer.android.com/training/data-storage/files#WriteExternalStorage
                String borrowerName,
                String borrowerEmail,
                Date dateLent,
                Date returnDate,
                String verify) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.borrowerName = borrowerName;
        this.borrowerEmail = borrowerEmail;
        this.dateLent = dateLent;
        this.returnDate = returnDate;
        this.verify = verify;
    }


    // properties


    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }

    public Date getDateLent() {
           return this.dateLent;
    }

    public void setDateLent(Date dateLent) {
        this.dateLent = dateLent;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
