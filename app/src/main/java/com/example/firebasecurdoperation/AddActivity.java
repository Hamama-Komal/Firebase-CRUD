package com.example.firebasecurdoperation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.firebasecurdoperation.databinding.ActivityAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog =new ProgressDialog(AddActivity.this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading....!");

        binding.btnAdd.setOnClickListener(v -> {

            insertData();
        });




    }

    private void insertData() {


        Map<String,Object> map = new HashMap<>();

        map.put("name",binding.getName.getText().toString());
        map.put("url",binding.getUrl.getText().toString());
        map.put("email",binding.getEmail.getText().toString());
        map.put("phone",binding.getContact.getText().toString());

        progressDialog.show();

        FirebaseDatabase.getInstance().getReference().child("StudentTable").push().setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        progressDialog.dismiss();

                        Toast.makeText(AddActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AddActivity.this, MainActivity.class));
                        finish();

                        binding.getName.setText(null);
                        binding.getEmail.setText(null);
                        binding.getUrl.setText(null);
                        binding.getContact.setText(null);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AddActivity.this, "Error! "+e , Toast.LENGTH_SHORT).show();

                    }
                });



    }
}