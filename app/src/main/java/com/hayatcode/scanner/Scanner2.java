package com.hayatcode.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hayatcode.scanner.data.UserLocalStore;
import com.hayatcode.scanner.model.User;

public class Scanner2 extends AppCompatActivity {

    EditText ET_pin;
    Button BT_login;
    Button BT_login_no_pin;

    FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner2);

        ET_pin = findViewById(R.id.password);
        BT_login = findViewById(R.id.login);
        BT_login_no_pin = findViewById(R.id.login_no_pin);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        BT_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    final String email = getIntent().getStringExtra("email");

                    firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                databaseReference.child("user").orderByChild("email")
                                        .equalTo(email)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot data) {

                                                for (DataSnapshot dataSnapshot : data.getChildren()) {

                                                    Static.clientUID = dataSnapshot.getKey();
                                                    Static.client = dataSnapshot.getValue(User.class);
                                                    Static.privacy = 1;
                                                    if (String.valueOf(Static.client.getPin())
                                                            .equals(ET_pin.getText().toString())) {


                                                        finish();
                                                        startActivity(new Intent(Scanner2.this,
                                                                ProfileActivity.class));
                                                    } else {
                                                        AlertDialog.Builder builder =
                                                                new AlertDialog.Builder(Scanner2.this);
                                                        builder.setMessage(getString(R.string.wrong_pin));
                                                        builder.setPositiveButton(getString(R.string.retry),
                                                                null);
                                                        builder.show();
                                                    }
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                Toast.makeText(Scanner2.this,
                                                        databaseError.getMessage(), Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }

                        }
                    });


                }

            }
        });

        BT_login_no_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = getIntent().getStringExtra("email");

                firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child("user").orderByChild("email")
                                    .equalTo(email)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot data) {

                                            for (DataSnapshot dataSnapshot : data.getChildren()) {

                                                Static.clientUID = dataSnapshot.getKey();
                                                Static.client = dataSnapshot.getValue(User.class);
                                                Static.privacy = 0;


                                                finish();
                                                startActivity(new Intent(Scanner2.this,
                                                        ProfileActivity.class));

                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                            Toast.makeText(Scanner2.this,
                                                    databaseError.getMessage(), Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                        }

                    }
                });

            }
        });


    }

    private boolean validate() {
        boolean validate = true;

        String password = ET_pin.getText().toString();


        if (password.length() != 5) {
            ET_pin.setError(getString(R.string.pin_required));
            validate = false;
        } else
            ET_pin.setError(null);


        return validate;
    }
}
