package com.kamalkumar.cloudcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kamalkumar.cloudcm.app.AppConfig;
import com.kamalkumar.cloudcm.app.AppController;
import com.kamalkumar.cloudcm.helper.SQLiteHandler;
import com.kamalkumar.cloudcm.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    public Button loginLink,registerButton;
    public EditText name,phone,email,password;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginLink=(Button) findViewById(R.id.login_link);
        registerButton=(Button) findViewById(R.id.register_button);
        name=(EditText)  findViewById(R.id.name_text);
        phone=(EditText)  findViewById(R.id.mobile_text);
        email=(EditText)  findViewById(R.id.email_text);
        password=(EditText)  findViewById(R.id.password_text);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();
        }
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
                String nameText=name.getText().toString();
                String phoneText=phone.getText().toString();
                String emailText=email.getText().toString();
                String passwordText=password.getText().toString();
                if (nameText.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Name", Toast.LENGTH_LONG).show();
                    name.setError("Please Provide Your Name");
                    return;
                } else if (phoneText.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Mobile Number", Toast.LENGTH_LONG).show();
                    phone.setError("Please Provide Your Mobile Number");
                    return;
                }else if (phoneText.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Please Provide valid 10 digit mobile number", Toast.LENGTH_LONG).show();
                    phone.setError("Please Provide valid 10 digit mobile number");
                    return;
                }else if (emailText.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Email", Toast.LENGTH_LONG).show();
                    email.setError("Please Provide Your Email");
                    return;
                }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    //Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Please Provide Valid Email", Toast.LENGTH_LONG).show();
                    email.setError("Please Provide Valid Email");
                    return;
                }else if (passwordText.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Provide Your Password", Toast.LENGTH_LONG).show();
                    password.setError("Please Provide Your Password");
                    return;
                } else {

                    registerUser(nameText, emailText, phoneText, passwordText);
                }
            }

        });
    }
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email, final String mobile,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String mobile = user.getString("mobile");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, mobile, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
    }
}
