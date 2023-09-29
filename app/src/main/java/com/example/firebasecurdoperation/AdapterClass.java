package com.example.firebasecurdoperation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebasecurdoperation.databinding.RecyclerItemBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class AdapterClass extends FirebaseRecyclerAdapter<ModelClass, AdapterClass.ViewHolder> {

    ModelClass modelClass;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public AdapterClass(@NonNull FirebaseRecyclerOptions<ModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ModelClass model) {

        // For Fetching Data on Recycler View

        holder.binding.nameView.setText(model.getName());
        holder.binding.emailView.setText(model.getEmail());
        holder.binding.phoneView.setText(model.getPhone());
        Glide.with(holder.binding.imageView.getContext()).load(model.getUrl()).into(holder.binding.imageView);

        // To Delete Data

        holder.binding.deleteData.setOnClickListener(v -> {
            AlertDialog.Builder alterdialog = new AlertDialog.Builder(holder.binding.deleteData.getContext());
            alterdialog.setTitle("Warning");
            alterdialog.setMessage("Are you sure to Delete this data?");
            alterdialog.setIcon(R.drawable.baseline_delete_24);

            alterdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    FirebaseDatabase.getInstance().getReference().child("StudentTable").child(getRef(position).getKey()).removeValue();


                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alterdialog.show();

        });


        // To Updata Data

        holder.binding.editData.setOnClickListener(v -> {

            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.binding.editData.getContext())
                    .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.edit_bottom_sheet))
                    .setExpanded(true, 850)
                    .create();


            View view = dialogPlus.getHolderView();


            EditText name = view.findViewById(R.id.updateName);
            EditText phone = view.findViewById(R.id.updateContact);
            EditText email = view.findViewById(R.id.updateEmail);
            EditText url = view.findViewById(R.id.updateUrl);
            Button btnUpdate = view.findViewById(R.id.btnUpdate);

            // Set the Value on Edit Text

            name.setText(model.getName());
            email.setText(model.getEmail());
            url.setText(model.getUrl());
            phone.setText(model.getPhone());


            dialogPlus.show();

            btnUpdate.setOnClickListener(v1 -> {

                Map<String, Object> map = new HashMap<>();
                // Getting the new Changed Value
                map.put("name", name.getText().toString());
                map.put("email", email.getText().toString());
                map.put("url", url.getText().toString());
                map.put("phone", phone.getText().toString());


                FirebaseDatabase.getInstance().getReference().child("StudentTable")
                        .child(getRef(position).getKey())
                        .updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.binding.imageView.getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.binding.imageView.getContext(), "Error occur while updating data", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();

                            }
                        });

            });


        });


    }

    @NonNull
    @Override
    public AdapterClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerItemBinding.bind(itemView);
        }
    }


}
