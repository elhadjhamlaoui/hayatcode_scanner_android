package com.hayatcode.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hayatcode.scanner.data.UserLocalStore;
import com.hayatcode.scanner.model.User;

public class Scanner extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {


    private QRCodeReaderView qrCodeReaderView;

    User user;
    UserLocalStore userLocalStore;

    DatabaseReference databaseReference;

    boolean enabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {


        if (enabled) {
            enabled = false;
            String email = text.split("/")[1];

            if (userLocalStore.isLoggedIn() && FirebaseAuth.getInstance().getCurrentUser() != null) {


                databaseReference.child("user").orderByChild("email")
                        .equalTo(email)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot data) {

                                for (DataSnapshot dataSnapshot : data.getChildren()) {

                                    Static.clientUID = dataSnapshot.getKey();
                                    Static.client = dataSnapshot.getValue(User.class);
                                    Static.privacy = 2;

                                    finish();
                                    startActivity(new Intent(Scanner.this,
                                            ProfileActivity.class));
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Toast.makeText(Scanner.this,
                                        databaseError.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            } else {
                Intent intent = new Intent(Scanner.this, Scanner2.class);
                intent.putExtra("email", email);
                finish();
                startActivity(intent);
            }

        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
        enabled = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

}
