package com.example.contactdatabaseapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViewContactActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<ContactModel> contactList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        databaseHelper = new DatabaseHelper(this);
        List<ContactModel> contacts = databaseHelper.getAllContacts();

        recyclerView = findViewById(R.id.RVContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = databaseHelper.getAllContacts();
        contactAdapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(contactAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }

    private void reloadList() {
        contactList.clear();
        contactList.addAll(databaseHelper.getAllContacts());
        contactAdapter.notifyDataSetChanged();
    }
}
