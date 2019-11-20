package com.hayatcode.scanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hayatcode.scanner.data.UserLocalStore;
import com.hayatcode.scanner.model.User;

public class MainActivity extends AppCompatActivity {

    Button BT_scanner, BT_register, BT_login, BT_logout;
    FirebaseAuth firebaseAuth;
    User user;
    UserLocalStore userLocalStore;
    TextView TV_name, TV_organization;
    CardView info_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        BT_scanner = findViewById(R.id.scan);
        BT_register = findViewById(R.id.register);
        BT_login = findViewById(R.id.login);

        BT_logout = findViewById(R.id.logout);
        TV_name = findViewById(R.id.name);
        TV_organization = findViewById(R.id.organization);
        info_layout = findViewById(R.id.info_layout);

        if (userLocalStore.isLoggedIn()) {
            BT_register.setVisibility(View.INVISIBLE);
            BT_login.setVisibility(View.INVISIBLE);
            BT_logout.setVisibility(View.VISIBLE);
            info_layout.setVisibility(View.VISIBLE);

            TV_name.setText(user.getFirstName() + " " + user.getFamilyName());
            TV_organization.setText(user.getOrganization());
        } else {
            BT_register.setVisibility(View.VISIBLE);
            BT_login.setVisibility(View.VISIBLE);
            BT_logout.setVisibility(View.INVISIBLE);
            info_layout.setVisibility(View.INVISIBLE);
        }

        BT_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, Scanner.class));
            }
        });

        BT_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, OrderNewCode.class));
            }
        });

        BT_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 22);

            }
        });

        BT_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(getString(R.string.logout));
                builder.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.logout(MainActivity.this, userLocalStore);
                            }
                        });
                builder.setNegativeButton(getString(R.string.no),
                        null);
                builder.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 22) {
            if (resultCode == Constant.RESULT_SUCCESS) {
                userLocalStore = new UserLocalStore(this);
                user = userLocalStore.getLoggedInUser();
                BT_register.setVisibility(View.INVISIBLE);
                BT_login.setVisibility(View.INVISIBLE);
                BT_logout.setVisibility(View.VISIBLE);
                info_layout.setVisibility(View.VISIBLE);

                TV_name.setText(user.getFirstName() + " " + user.getFamilyName());
                TV_organization.setText(user.getOrganization());
            }
        }
    }
}
