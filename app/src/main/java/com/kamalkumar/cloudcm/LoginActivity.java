package com.kamalkumar.cloudcm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public Button registerLink,loginButton,forgetPasswordLink;
    public EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerLink=(Button) findViewById(R.id.register_link);
        loginButton=(Button) findViewById(R.id.login_button);
        forgetPasswordLink=(Button) findViewById(R.id.forget_password_link);
        username=(EditText) findViewById(R.id.username_text) ;
        password=(EditText) findViewById(R.id.password_text);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(view.getContext(), RegisterActivity.class);
                startActivity(myintent);
                overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out);

            }


        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Mobile Number", Toast.LENGTH_LONG).show();
                    username.setError("Please Provide Your Mobile Number");
                    return;
                }else if (username.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Please Provide valid 10 digit mobile number", Toast.LENGTH_LONG).show();
                    username.setError("Please Provide valid 10 digit mobile number");
                    return;
                } else if (password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Password", Toast.LENGTH_LONG).show();
                    password.setError("Please Provide Your Password");
                    return;
                } else {


                }
            }

        });
    }
}
