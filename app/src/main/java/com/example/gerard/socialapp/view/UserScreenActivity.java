package com.example.gerard.socialapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gerard.socialapp.GlideApp;
import com.example.gerard.socialapp.R;
import com.example.gerard.socialapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserScreenActivity extends AppCompatActivity {

    ImageView uImagen;
    TextView uNombre;
    TextView uCorreo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        uImagen = findViewById(R.id.uImage);
        uNombre = findViewById(R.id.uName);
        uCorreo = findViewById(R.id.uEmail);

        String uid = getIntent().getStringExtra("uid");

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                GlideApp.with(UserScreenActivity.this)
                        .load(user.photoUrl)
                        .circleCrop()
                        .into(uImagen);

                uNombre.setText(user.displayName);
                uCorreo.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

