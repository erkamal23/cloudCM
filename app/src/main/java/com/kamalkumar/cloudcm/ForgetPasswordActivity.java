package com.kamalkumar.cloudcm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPasswordActivity extends AppCompatActivity {
    public Button loginLink,registerLink,resetPasswordButton;
    public EditText phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        loginLink=(Button) findViewById(R.id.login_link);
        registerLink=(Button) findViewById(R.id.register_link);
        resetPasswordButton=(Button) findViewById(R.id.reset_password_button);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);

            }


        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myintent=new Intent(view.getContext(), RegisterActivity.class);
                startActivity(myintent);
                overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out);

            }


        });
    }
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
    }
}
