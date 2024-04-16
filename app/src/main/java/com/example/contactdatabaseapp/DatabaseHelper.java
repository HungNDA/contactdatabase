package com.example.contactdatabaseapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactDatabase";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOB = "dob";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROFILE_IMAGE = "profile_image";
    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + " TEXT,"
            + KEY_DOB + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PROFILE_IMAGE + " BLOB"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_CONTACTS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Add a new contact
    public void addContact(ContactModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_DOB, contact.getDob());
        values.put(KEY_EMAIL, contact.getEmail());
        // Convert Bitmap to byte array
        byte[] profileImageBytes = DatabaseUtils.getBytes(contact.getProfileImage());
        values.put(KEY_PROFILE_IMAGE, profileImageBytes);

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Get all contacts
    public List<ContactModel> getAllContacts() {
        List<ContactModel> contactList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);

            // Check if the cursor is not null and has rows
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(KEY_NAME);
                int dobIndex = cursor.getColumnIndex(KEY_DOB);
                int emailIndex = cursor.getColumnIndex(KEY_EMAIL);
                int imageIndex = cursor.getColumnIndex(KEY_PROFILE_IMAGE);

                // Loop through rows and add to the list
                do {
                    // Convert byte array to Bitmap
                    byte[] profileImageBytes = cursor.getBlob(imageIndex);
                    Bitmap profileImage = DatabaseUtils.getImage(profileImageBytes);

                    ContactModel contact = new ContactModel(
                            cursor.getString(nameIndex),
                            cursor.getString(dobIndex),
                            cursor.getString(emailIndex),
                            profileImage);

                    // Adding contact to the list
                    contactList.add(contact);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return contactList;
    }

}