package com.example.titan.bpmsafety;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.

    private EditText employee_id, password;
    private ProgressBar progressBar;
    private TextInputLayout textInputEmployeeCode, textInputPassword;
    private TextView forgot_password;

    static String token;
    //String userName, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        employee_id = findViewById(R.id.edittext_employee_code);
        password = findViewById(R.id.edittext_password);

        textInputEmployeeCode = findViewById(R.id.employee_code);
        textInputPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.login_progress);

        forgot_password = findViewById(R.id.forgot_password);

        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Forcing the App to crash to test Firebase crash reporting
                //Crashlytics.getInstance().crash();

                hideKeyboard(view);
                if (!isConnected(LoginActivity.this)) buildDialog(LoginActivity.this).show();
                else
                {
                    String employeeId = employee_id.getText().toString().trim();
//                int final_eid;
//                try {
//                    final_eid = Integer.parseInt(employeeId);
//                } catch (NumberFormatException nfe) {
//                    final_eid = 0;
//                }

                String pwd = password.getText().toString().trim();

                if (!employeeId.isEmpty() && !pwd.isEmpty())
                {
                    Log.d("Em length",employeeId.length()+"");
                    if ( employeeId.length() < 4)
                        employee_id.setError("Enter a valid Emp Code");
                    else
                    {
                        if(employeeId.length()< 7)

                        {   String zeros="";
                            int length= 7-employee_id.length();
                            for( int i =0; i< length; i++){
                                zeros= zeros+"0";
                            }
                            employeeId = zeros+employeeId;
                            Log.d("Employee id",employeeId);

                        }
                        new SyncData().execute(confirmInput(employeeId));
                    }



                   // progressBar.setVisibility(view.VISIBLE);
                    //Toast.makeText(LoginActivity.this, "You're logged in", Toast.LENGTH_LONG).show();

                } else {
                    if (employeeId.isEmpty())
                        employee_id.setError("This field cannot be empty");
                    if (pwd.isEmpty())
                        password.setError("This field cannot be empty");
                }

            } }
        });

        forgot_password = findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog1(LoginActivity.this).show();
            }
        });

    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public AlertDialog.Builder buildDialog1(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //builder.setMessage("To reset your password please drop an email with your employee code to ID - helpdeskge@titan.co.in via your official email account");
        builder.setMessage(Html.fromHtml("To "+"reset "+"your "+"password "+"please "+"drop "+"an "+"email "+"with "+
                "your "+"employee "+"code "+"to "+"<b>"+"helpdeskge@titan.co.in "+"</b>"+"via "+"your "+"official "+"email "+"account. "));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or Wi-fi to access this. Press ok to Exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }
    public JSONObject confirmInput(String employeeId) {

        JSONObject jsonObject = new JSONObject();

        try {

            //jsonObject.put("username", textInputEmployeeCode.getEditText().getText().toString());
            //jsonObject.put("password", textInputPassword.getEditText().getText().toString());
            //JSONParser_with_httpURLConnection jsonParser_with_httpURLConnection = new JSONParser_with_httpURLConnection();
            Log.d("UserName",employee_id.getText().toString());
            Log.d("Password",password.getText().toString());
            JSONParser_with_httpURLConnection.userName = "e"+employeeId.trim();
            JSONParser_with_httpURLConnection.mpassword = password.getText().toString().trim();
            NavDrawer.employee_id = "e"+employeeId.trim();
            jsonObject.put("refresh-groups","false");
            jsonObject.put("requested-lifetime","7200");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    class SyncData extends AsyncTask<JSONObject, String, String> {


        /*Before starting background thread Show Progress Dialog*/
        ProgressDialog pDialog;

        JSONParser_with_httpURLConnection jsonParser_with_httpURLConnection = new JSONParser_with_httpURLConnection();
        String url = "https://ebpm.titan.co.in/bpm/system/login";
        JSONObject json = new JSONObject();

        //UAT: https://guna.titan.in:8443/bpm/system/login
        //Production API: https://ebpm.titan.co.in/bpm/system/login

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /* Creating product
         */
        protected String doInBackground(JSONObject... args)
        {
            try {

                Log.d("JSON Request", "JSONObject" + args[0].toString());

                json = jsonParser_with_httpURLConnection.makeHttpRequest(url, "POST", args[0]);
                Log.d("JSONRESPONSE1", json.toString());
                Log.d("JSON Token",json.getString("csrf_token"));
                 // Storing the Token to JSON Parser class static variable
                 token = json.getString("csrf_token");



            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /*  After completing background task Dismiss the progress dialog*/

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            try {
                if (pDialog != null && pDialog.isShowing()) {

                    pDialog.dismiss();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (json.getBoolean("Timeout")) {

                    switch(json.getInt("errorNo"))
                    {
                        case 400:
                            Toast.makeText(getApplicationContext(),"Bad request",Toast.LENGTH_LONG).show();
                            break;
                        case 401:
                            employee_id.setText("");
                            password.setText("");
                            Toast.makeText(getApplicationContext(),"Invalid username/password",Toast.LENGTH_LONG).show();
                            break;
                        case 403:
                            employee_id.setText("");
                            password.setText("");
                            Toast.makeText(getApplicationContext(),"Login session got expired",Toast.LENGTH_LONG).show();
                            break;
                        case 409:
                            Toast.makeText(getApplicationContext()," Internal Server error",Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(getApplicationContext()," Internal Server error",Toast.LENGTH_LONG).show();
                            break;
                            default: break;

                    }

                    return;

                } else {
                    employee_id.setText("");
                    password.setText("");
                    Intent MainIntent = new Intent(LoginActivity.this, NavDrawer.class);
                    startActivity(MainIntent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // finish();

        }

    }
}

