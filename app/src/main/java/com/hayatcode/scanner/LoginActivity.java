package com.hayatcode.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hayatcode.scanner.data.UserLocalStore;
import com.hayatcode.scanner.model.User;

public class LoginActivity extends AppCompatActivity {

    Button BT_login;
    EditText ET_email, ET_password;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    User user;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BT_login = findViewById(R.id.login);
        ET_email = findViewById(R.id.email);
        ET_password = findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        userLocalStore = new UserLocalStore(this);
        user = new User();

        BT_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    firebaseAuth.
                            signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final FirebaseUser firebaseUser = task.getResult().getUser();

                                        databaseReference.child("user").child(firebaseUser.getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        user = dataSnapshot.getValue(User.class);
                                                        userLocalStore.setUserLoggedIn(true);
                                                        userLocalStore.storeUserData(user);
                                                        setResult(Constant.RESULT_SUCCESS);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

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


        String email = ET_email.getText().toString();
        String password = ET_password.getText().toString();

        user.setEmail(email);
        user.setPassword(password);

        if (!Utils.isEmailValid(email)) {
            ET_email.setError(getString(R.string.email_error));
            validate = false;
        } else
            ET_email.setError(null);


        if (password.length() < 6) {
            ET_password.setError(getString(R.string.password_required));
            validate = false;
        } else
            ET_password.setError(null);

        return validate;
    }
}
