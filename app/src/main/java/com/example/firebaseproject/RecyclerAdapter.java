package com.example.firebaseproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Layout;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends FirebaseRecyclerAdapter<EmployeeClass, RecyclerAdapter.ViewHolder> {

    // initializing a recycler view adapter that inherits FirebaseRecyclerAdapter
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    // constructor for the recyclerview adapter
    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<EmployeeClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder,  final int position, @NonNull EmployeeClass model) {
        // setting employee itemView with the imported employee instance from firebase realtime database
        holder.name.setText(model.getName());
        holder.role.setText(model.getRole());
        holder.email.setText(model.getEmail());

        // using a third party Glide module to display an image into an android application from a URL
        Glide.with(holder.img.getContext())
                .load(model.getImageURL())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        // delete button is listening for click events
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // when button clicked, it will show an alert dialog taking confirmation from the user
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are you sure you want to delete?");
                builder.setMessage("Deleted data will can not be restored");

                // if user confirms the delete
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // calling the firebase realtime database to look for a key (of the selected itemView of recyclerview)
                        // in the 'employee' root and remove the value
                        // employee record is deleted permanently from both the database and the application
                        FirebaseDatabase.getInstance().getReference().child("employees").child(getRef(position).getKey()).removeValue();
                    }
                });

                // if user changes their mind
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // shows a friendly message that delete operation is cancelled
                        Toast.makeText(holder.name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        // edit button is listening for click events
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // using a third party DialogPlus module to generate a custom made dialog popup generated from an xml file
                // this dialog popup will show up when edit button is clicked
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_popup))
                        .setGravity(Gravity.BOTTOM)
                        .create();

                // finding elements from xml file that the DialogPlus uses to generate the dialog popup
                View view = dialogPlus.getHolderView();
                EditText name = view.findViewById(R.id.NameInput);
                EditText role = view.findViewById(R.id.RoleInput);
                EditText email = view.findViewById(R.id.EmailInput);
                EditText imageURL = view.findViewById(R.id.ImageInput);
                Button updateButton = view.findViewById(R.id.updateButton);

                // assigning the values from the imported employee instance from the database to the elements
                name.setText(model.getName());
                role.setText(model.getRole());
                email.setText(model.getEmail());
                imageURL.setText(model.getImageURL());

                // dialogPlus is displayed
                dialogPlus.show();

                // update button is listening for click events
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // form input validation
                        if (name.getText().toString().equals("") || role.getText().toString().equals("") || email.getText().toString().equals("")){
                            Toast.makeText(holder.name.getContext(), "Please fill out name, role and email fields before updating", Toast.LENGTH_SHORT).show();
                        }

                        // if validation passes
                        else {
                            // creating a hashmap and setting 4 different key value pairs
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", name.getText().toString());
                            map.put("role", role.getText().toString());
                            map.put("email", email.getText().toString());
                            map.put("imageURL", imageURL.getText().toString());

                            // calling the firebase realtime database and look for an employee record
                            // matching the key of the employee item of the recyclerview
                            // and then passing the updated hashmap to replace that particular employee record in database
                            FirebaseDatabase.getInstance().getReference().child("employees")
                                    .child(getRef(position).getKey()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                                        // if update operation succeeds, it will show a friendly toast message to the user
                                        // the dialog popup will go away
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(holder.name.getContext(), "Data updated successfully.", Toast.LENGTH_SHORT).show();
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {

                                        // if update operation fail, it will show a friendly toast message that operation has failed
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.name.getContext(), "Error while updating", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

            }
        });
    }

    // assigning the main_item.xml as the layout of every item in the recyclerview
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);

        return new ViewHolder(view);
    }


    // view holder that contains all the elements of an item in a recyclerview and initialized
    class ViewHolder extends RecyclerView.ViewHolder{

        // initializing
        CircleImageView img;
        TextView name, role, email;
        Button editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // finding the elements from xml file and assigning them to variables
            img = (CircleImageView) itemView.findViewById(R.id.circleImage);
            name = (TextView) itemView.findViewById(R.id.nameText);
            role = (TextView) itemView.findViewById(R.id.roleText);
            email = (TextView) itemView.findViewById(R.id.emailText);
            editButton = (Button) itemView.findViewById(R.id.EditButton);
            deleteButton = (Button) itemView.findViewById(R.id.DeleteButton);
        }
    }
}
