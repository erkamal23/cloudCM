package com.kamalkumar.cloudcm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    public Button loginLink,registerButton;
    public EditText phone,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginLink=(Button) findViewById(R.id.login_link);
        registerButton=(Button) findViewById(R.id.register_button);
        phone=(EditText)  findViewById(R.id.mobile_text);
        email=(EditText)  findViewById(R.id.email_text);
        password=(EditText)  findViewById(R.id.password_text);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);

            }


        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Mobile Number", Toast.LENGTH_LONG).show();
                    phone.setError("Please Provide Your Mobile Number");
                    return;
                } else if (phone.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Please Provide valid 10 digit mobile number", Toast.LENGTH_LONG).show();
                    phone.setError("Please Provide valid 10 digit mobile number");
                    return;
                }else if (email.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Email", Toast.LENGTH_LONG).show();
                    email.setError("Please Provide Your Email");
                    return;
                }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Please Provide Valid Email", Toast.LENGTH_LONG).show();
                    email.setError("Please Provide Valid Email");
                    return;
                }else if (password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Password", Toast.LENGTH_LONG).show();
                    password.setError("Please Provide Your Password");
                    return;
                } else {


                }
            }

        });
    }
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
    }
}
