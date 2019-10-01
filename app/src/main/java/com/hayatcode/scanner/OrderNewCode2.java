package com.hayatcode.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hayatcode.scanner.model.User;

public class OrderNewCode2 extends AppCompatActivity {
    EditText ET_password, ET_repeat_password;
    Button BT_next;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_new_code2);

        ET_password = findViewById(R.id.password);
        ET_repeat_password = findViewById(R.id.repeat_password);

        BT_next = findViewById(R.id.next);

        user = getIntent().getParcelableExtra("user");

        BT_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Intent intent = new Intent(OrderNewCode2.this, OrderNewCode3.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validate() {
        boolean validate = true;


        String password = ET_password.getText().toString();
        String repeat_password = ET_repeat_password.getText().toString();


        user.setPassword(password);

        if (password.length() < 6) {
            ET_password.setError(getString(R.string.password_required));
            validate = false;
        } else
            ET_password.setError(null);

        if (!repeat_password.equals(password)) {
            ET_repeat_password.setError(getString(R.string.password_error_repeat));
            validate = false;
        } else
            ET_repeat_password.setError(null);


        return validate;
    }
}
