package com.example.contactdatabaseapp;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<ContactModel> contactList;

    public ContactAdapter(List<ContactModel> contactList) {
        this.contactList = contactList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewDOB, textViewEmail;
        public ImageView imageViewProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.editTextName);
            textViewDOB = itemView.findViewById(R.id.editTextDOB);
            textViewEmail = itemView.findViewById(R.id.editTextEmail);
            imageViewProfile = itemView.findViewById(R.id.imageView); //
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel contact = contactList.get(position);
        holder.textViewName.setText("" + contact.getName());
        holder.textViewDOB.setText("" + contact.getDob());
        holder.textViewEmail.setText("" + contact.getEmail());

        // Assuming you have a method in ContactModel to get the profile image as Bitmap
        Bitmap profileImage = contact.getProfileImage();
        if (profileImage != null) {
            holder.imageViewProfile.setImageBitmap(profileImage);
        } else {
            // If the profile image is null, you may set a default image or handle it as per your requirement
            holder.imageViewProfile.setImageResource(R.drawable.image1);
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}