package com.example.firebasecurdoperation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.firebasecurdoperation.databinding.ActivityMainBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    AdapterClass adapterClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Floating Button
        binding.AddButton.setOnClickListener(v -> {

            startActivity(new Intent(MainActivity.this, AddActivity.class));
            
        });

        // Recycler View

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        binding.recycler.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<ModelClass> options = new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(FirebaseDatabase.getInstance().getReference().child("StudentTable"), ModelClass.class).build();

        adapterClass = new AdapterClass(options);
        binding.recycler.setAdapter(adapterClass);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterClass.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterClass.stopListening();
    }
}