package com.hayatcode.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hayatcode.scanner.model.User;

public class OrderNewCode3 extends AppCompatActivity {
    EditText ET_nationalId, ET_address, ET_postalcode, ET_organization;
    Button BT_next;
    User user;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_new_code3);
        ET_nationalId = findViewById(R.id.national_id);
        ET_address = findViewById(R.id.address);
        ET_postalcode = findViewById(R.id.postalcode);
        ET_organization = findViewById(R.id.organization);

        BT_next = findViewById(R.id.next);

        user = getIntent().getParcelableExtra("user");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        BT_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {

                    firebaseAuth.signInWithEmailAndPassword(user.getEmail(),
                            user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(OrderNewCode3.this);
                                builder.setMessage(getString(R.string.email_used));
                                builder.setPositiveButton(getString(R.string.retry),
                                        null);
                                builder.show();
                            } else {
                                firebaseAuth
                                        .createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    firebaseAuth.
                                                            signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        final FirebaseUser firebaseUser = task.getResult().getUser();

                                                                        user.setPassword("");
                                                                        databaseReference.child("user").child(firebaseUser.getUid())
                                                                                .setValue(user)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            Intent intent = new Intent(getApplicationContext(), OrderNewCode5.class);
                                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                            startActivity(intent);
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        });
                            }
                        }
                    });

                }
            }
        });
    }

    private boolean validate() {
        boolean validate = true;


        int nationalId = Integer.parseInt(ET_nationalId.getText().toString());
        String address = ET_address.getText().toString();
        String postalcode = ET_postalcode.getText().toString();
        String organization = ET_organization.getText().toString();

        user.setNationalId(nationalId);
        user.setDeliveryAddress(address+" "+postalcode);
        user.setOrganization(organization);

        if (nationalId == 0) {
            ET_nationalId.setError(getString(R.string.field_required));
            validate = false;
        } else
            ET_nationalId.setError(null);

        if (address.isEmpty()) {
            ET_address.setError(getString(R.string.field_required));
            validate = false;
        } else
            ET_address.setError(null);

        if (postalcode.isEmpty()) {
            ET_postalcode.setError(getString(R.string.field_required));
            validate = false;
        } else
            ET_postalcode.setError(null);

        if (organization.isEmpty()) {
            ET_organization.setError(getString(R.string.field_required));
            validate = false;
        } else
            ET_organization.setError(null);


        return validate;
    }
}
