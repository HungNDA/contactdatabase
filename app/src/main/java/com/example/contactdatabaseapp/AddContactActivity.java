package com.example.contactdatabaseapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class AddContactActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText editTextName, editTextDOB, editTextEmail;
    private ImageView imageView;
    private String currentPhotoPath;
    private Uri imageUri;
    private Button buttonSave, buttonView, buttonCapture;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editTextName = findViewById(R.id.editTextName);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextEmail = findViewById(R.id.editTextEmail);
        imageView = findViewById(R.id.imageView);
        buttonSave = findViewById(R.id.buttonSave);
        buttonView = findViewById(R.id.buttonView);

        databaseHelper = new DatabaseHelper(AddContactActivity.this);

        ImageButton buttonCapture = findViewById(R.id.buttonCapture);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to save?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract data from UI
                        String name = editTextName.getText().toString().trim();
                        String dob = editTextDOB.getText().toString().trim();
                        String email = editTextEmail.getText().toString().trim();
                        if (name.isEmpty() || dob.isEmpty() || email.isEmpty()) {
                            Toast.makeText(AddContactActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Bitmap profileImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                        ContactModel newContact = new ContactModel(name, dob, email, profileImage);

                        databaseHelper.addContact(newContact);

                        Toast.makeText(AddContactActivity.this, "Contact saved successfully", Toast.LENGTH_SHORT).show();

                        editTextName.getText().clear();
                        editTextDOB.getText().clear();
                        editTextEmail.getText().clear();
                        imageView.setImageResource(R.drawable.person);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, do nothing
                    }
                });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Set onClickListener for View button
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ContactModel> contacts = databaseHelper.getAllContacts();
                Intent intent = new Intent(AddContactActivity.this, ViewContactActivity.class);
                startActivity(intent);
            }
        });
        editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

    }
    private void showDatePickerDialog()     {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        editTextDOB.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    private void openImagePicker() {
        // Tạo Intent để chọn ảnh từ thư viện
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (capturePhotoIntent.resolveActivity(getPackageManager()) != null) {
            Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{capturePhotoIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
        } else {
            startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Lấy ảnh từ thư viện
            Uri selectedImageUri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Lấy ảnh từ camera
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("data")) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }
}