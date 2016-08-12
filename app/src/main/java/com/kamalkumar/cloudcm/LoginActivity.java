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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    public Button registerLink,loginButton,forgetPasswordLink;
    public EditText username,password;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerLink=(Button) findViewById(R.id.register_link);
        loginButton=(Button) findViewById(R.id.login_button);
        forgetPasswordLink=(Button) findViewById(R.id.forget_password_link);
        username=(EditText) findViewById(R.id.username_text) ;
        password=(EditText) findViewById(R.id.password_text);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(view.getContext(), ForgetPasswordActivity.class);
                startActivity(myintent);
                overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out);

            }


        });
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
                String mobileStr = username.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();
                if (mobileStr.length() == 0) {
                    //Toast.makeText(getApplicationContext(), "Please Provide Your Mobile Number", Toast.LENGTH_LONG).show();
                    username.setError("Please Provide Your Mobile Number");
                    return;
                }else if (mobileStr.toString().length() != 10) {
                    //Toast.makeText(getApplicationContext(), "Please Provide valid 10 digit mobile number", Toast.LENGTH_LONG).show();
                    username.setError("Please Provide valid 10 digit mobile number");
                    return;
                } else if (passwordStr.toString().length() == 0) {
                   // Toast.makeText(getApplicationContext(), "Please Provide Your Password", Toast.LENGTH_LONG).show();
                    password.setError("Please Provide Your Password");
                    return;
                } else {
                    checkLogin(mobileStr, passwordStr);

                }
            }

        });
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String mobile, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String mobile = user.getString("mobile");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, mobile, uid, created_at);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("password", password);
                Log.d(TAG, "mobile: " + mobile.toString());
                Log.d(TAG, "password: " + password.toString());
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
}
