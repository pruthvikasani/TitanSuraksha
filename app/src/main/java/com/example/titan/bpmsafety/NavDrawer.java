package com.example.titan.bpmsafety;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static android.view.View.VISIBLE;

public class NavDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    static Uri capturedImageUri = null;

    File photoFile = null;
    private String mCurrentPhotoPath = "";
    Uri photoURI = null;

    Switch switch1, switch2, switch3;

    String Encodedstring = "";

    Spinner spinner, spinner1, spinner2, spinner3;

    RelativeLayout image_layout;

    Button submit_button;

    Bitmap imageBitmap;

    ImageButton close_button, alert_close_button;

    LinearLayout employee_code_layout;

    TextView employee_code, forgot_password, alert_type_textview, report_location_textview, store_type_textview,
            brand_textview, tv_employeecode;

    ScrollView main_scroller;

    EditText et_employeecode, et_city, et_alert_location, et_store_location, et_alert_description, et_cause, et_prevention;

    String employee_code_number = "", city, alert_location, store_location, alert_description, basic_cause, prevent;

    static String employee_id;

    String value, report_location, store_type, brand;
    FragmentTransaction fragmentTransaction;

    TextView errorText;

    FloatingActionButton fab;
    byte[] byteArray = null;

    final Context context = this;

    TextView time_picker, date_picker;

    //Floating action bar at bottom
    ImageView IvImage, alert_type;
    private static final int REQUEST_CAMERA = 1;
    static final int SELECT_FILE = 0;
    Boolean s1, s2, s3;

    String version="";

    Boolean switch_1 = false, switch_2 = false, switch_3 = false;
    int day, month, year, hour, minute;
    String time = "HH:MM", date = "DD-MM-YYYY";
    String date_time = time + " " + date;
    public FragmentManager fragmentManager = null;

    TextView version_name;

    int dayFinal, monthFinal, yearFinal, hourFinal, miunteFinal;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        try {
            NavDrawer.this.getSupportActionBar().setTitle("SAFETY ALERT PROCESS");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);

        submit_button = findViewById(R.id.submit_button);

        image_layout = findViewById(R.id.image_layout);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version_name = findViewById(R.id.version_number);
        version_name.setText("Version: "+version);

        final Spinner spinner = findViewById(R.id.alertBy_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.reporting_person, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Code related to the four drop downs
        final Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.reported_location, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        final Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.store_type, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        final Spinner spinner3 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.brand, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);


        //Code related to date picker
        date_picker = findViewById(R.id.date_picker);

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NavDrawer.this, NavDrawer.this, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        date="DD-MM-YYYY";
                        date_picker.setText(date);
                    }
                });
                datePickerDialog.show();

            }

        });


        time_picker = findViewById(R.id.time_picker);


        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!date.equals("DD-MM-YYYY")) {
                    Calendar c = Calendar.getInstance();
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);

                    final TimePickerDialog timePickerDialog = new TimePickerDialog(NavDrawer.this, NavDrawer.this, hour, minute, false);
                    timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            time="HH:MM";
                            time_picker.setText(time);
                        }
                    });
                    timePickerDialog.show();
                } else
                    Toast.makeText(getApplicationContext(), "Pick the date first", Toast.LENGTH_LONG).show();

            }
        });


        //Code related to floating action bar at bottom of screen

        IvImage = findViewById(R.id.IvImage);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        alert_type = findViewById(R.id.alert_type);

        alert_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog customDialog = new Dialog(context);
                customDialog.setContentView(R.layout.image_layout);

                ImageView image = customDialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.alert_type);

                ImageButton alert_close_button = customDialog.findViewById(R.id.alert_close_button);
                alert_close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                    }
                });
                customDialog.show();

            }
        });

        alert_type_textview = findViewById(R.id.alert_type_textview);
        report_location_textview = findViewById(R.id.reported_location_textview);
        store_type_textview = findViewById(R.id.store_location_textview);
        brand_textview = findViewById(R.id.brand_textview);


        et_employeecode = findViewById(R.id.et_employee_code);
        tv_employeecode = findViewById(R.id.tv_employee_code);
        et_city = findViewById(R.id.et_city);
        et_alert_location = findViewById(R.id.et_alert_location);
        et_store_location = findViewById(R.id.et_store_location);
        et_alert_description = findViewById(R.id.et_alert_description);
        et_cause = findViewById(R.id.et_cause);
        et_prevention = findViewById(R.id.et_prevent);

        close_button = findViewById(R.id.close_button);

        main_scroller = findViewById(R.id.main_scroller);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(v);
                if (!isConnected(NavDrawer.this)) buildDialog(NavDrawer.this).show();
                else {
                    String statusSwitch1 = "OFF", statusSwitch2 = "OFF", statusSwitch3 = "OFF";

                    if (value.equals("Self"))
                        employee_code_number = tv_employeecode.getText().toString();
                    if (value.equals("Other Employees"))
                        employee_code_number = et_employeecode.getText().toString();

                    city = et_city.getText().toString();
                    alert_location = et_alert_location.getText().toString();
                    store_location = et_store_location.getText().toString();
                    alert_description = et_alert_description.getText().toString();
                    basic_cause = et_cause.getText().toString();
                    prevent = et_prevention.getText().toString();

                    if ((switch1.isChecked() || switch2.isChecked() || switch3.isChecked()) &&  reportedby_validation() && !date.equals("DD-MM-YYYY") && !time.equals("HH:MM") &&
                            report_validation() && !alert_description.isEmpty() && !alert_location.isEmpty())
                    {
                        if (switch1.isChecked())
                            statusSwitch1 = switch1.getTextOn().toString();
                        if (switch2.isChecked())
                            statusSwitch2 = switch2.getTextOn().toString();
                        if (switch3.isChecked())
                            statusSwitch3 = switch3.getTextOn().toString();

                        s1 = string_to_boolean1(statusSwitch1);
                        s2 = string_to_boolean2(statusSwitch2);
                        s3 = string_to_boolean3(statusSwitch3);


                        new SyncData().execute(confirmInput());
                                main_scroller.fullScroll(ScrollView.FOCUS_UP);
                                spinner.setSelection(0);
                                spinner1.setSelection(0);
                                spinner2.setSelection(0);
                                spinner3.setSelection(0);
                                et_employeecode.setText("");
                                et_city.setText("");
                                et_alert_location.setText("");
                                et_store_location.setText("");
                                et_alert_description.setText("");
                                et_cause.setText("");
                                et_prevention.setText("");
                                switch1.setChecked(false);
                                switch2.setChecked(false);
                                switch3.setChecked(false);
                                date="DD-MM-YYYY";
                                date_picker.setText(date);
                                time="HH:MM";
                                time_picker.setText(time);
                                image_layout.setVisibility(View.GONE);
                                alert_type_textview.setError(null);
                                time_picker.setError(null);
                                date_picker.setError(null);
                                report_location_textview.setError(null);
                                store_type_textview.setError(null);
                                brand_textview.setError(null);
                                et_city.setError(null);
                                et_alert_location.setError(null);
                                et_store_location.setError(null);
                                et_alert_description.setError(null);
                                et_alert_location.clearFocus();
                                Encodedstring = "";
                                report_location = "";
                                brand = "";
                                store_type = "";
                                imageBitmap = null;
                                employee_code_number = "";

                    } else {

                        if (value.equals("Select"))
                            ((TextView) spinner.getSelectedView()).setError("Your Error msg Here");
//                        if (employee_code_number.isEmpty())
//                            et_employeecode.setError("This field cannot be empty");
                        if (!(switch1.isChecked() || switch2.isChecked() || switch3.isChecked()))
                            alert_type_textview.setError("Select atleast one alert type");
                        if (date.equals("DD-MM-YYYY"))
                            date_picker.setError("Field cant be empty");
                        if (time.equals("HH:MM"))
                            time_picker.setError("Field cant be empty");
//                        if (report_location.equals("Select"))
//                            report_location_textview.setError("This field can't be empty");
                        if (report_location.equals("Select"))
                            ((TextView) spinner1.getSelectedView()).setError("Your Error msg Here");
                        if (alert_location.isEmpty() && alert_location.length() == 0)
                            et_alert_location.setError("This field cannot be empty");
                        if (alert_description.isEmpty() && alert_description.length() == 0)
                            et_alert_description.setError("This field cannot be empty");
                        Toast.makeText(getApplicationContext(), "Mandatory fields are missing", Toast.LENGTH_LONG).show();
                    }


                }
            }

        });


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    switch_1 = true;
                else
                    switch_1 = false;
                alert_type_error();
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    switch_2 = true;
                else
                    switch_2 = false;
                alert_type_error();

            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    switch_3 = true;
                else
                    switch_3 = false;
                alert_type_error();
            }
        });


        et_alert_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0)
                    et_alert_location.setError("This field cannot be empty");
                else
                    et_alert_location.setError(null);

            }
        });

        et_alert_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0)
                    et_alert_description.setError("This field cannot be empty");
                else
                    et_alert_description.setError(null);

            }
        });

        fragmentManager = getSupportFragmentManager();
    }


    public void alert_type_error() {

        if (switch_1 || switch_2 || switch_3)
            alert_type_textview.setError(null);
        else
            alert_type_textview.setError("Select atleast one alert type");
    }


    public boolean reportedby_validation(){
        if(!value.equals("Select")){
            if (value.equals("Self")) {
                employee_code_number = tv_employeecode.getText().toString();
                return true;
            }
            if (value.equals("Other Employees")) {
                employee_code_number = et_employeecode.getText().toString();

                if( employee_code_number.isEmpty())
                {
                    et_employeecode.setError("This field cannot be empty");
                    return false;
                }
                else
                    return true;
            }

            else return true;
        }
        else{

            Spinner spinner = findViewById(R.id.alertBy_spinner);
            ((TextView) spinner.getSelectedView()).setError("Your Error msg Here");
            return false;
        }
    }

    public boolean report_validation()
    {
        if( !report_location.equals("Select"))
        {
            if (!report_location.equals("RO-North") && !report_location.equals("RO-South") && !report_location.equals("RO-East") && !report_location.equals("RO-West"))
            {
                return true;
            } else {
                        if (!store_type.equals("Select"))
                        {
                            if (store_type.equals("Retail"))
                            {
                                if (!brand.equals("Select") && !city.isEmpty() && !store_location.isEmpty())
                                    return true;
                                else
                                    {
                                        if(brand.equals("Select"))
                                        {
                                            Spinner spinner3 = findViewById(R.id.spinner3);
                                            ((TextView) spinner3.getSelectedView()).setError("Your Error msg Here");
                                        }
                                        if(city.isEmpty())
                                            et_city.setError("This field cannot be empty");
                                        if(store_location.isEmpty())
                                            et_store_location.setError("This field cannot be empty");
                                        return false;
                                    }

                            }
                            if (store_type.equals("Others"))
                            return true;
                        } else
                         {
                             Spinner spinner2 = findViewById(R.id.spinner2);
                             ((TextView) spinner2.getSelectedView()).setError("Your Error msg Here");
                             return false;
                         }
                    }

                return true;
        }

        else {
                Spinner spinner1 = findViewById(R.id.spinner1);
                ((TextView) spinner1.getSelectedView()).setError("Your Error msg Here");
                return false;
            }

    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
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

//    public AlertDialog.Builder buildDialog2(Context c) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(c);
//        builder.setCancelable(false);
//        builder.setMessage("Alert Submitted Successfully. Do you want to raise another request?");
//
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.dismiss();
////                Intent intent = new Intent(NavDrawer.this, NavDrawer.class);
////                startActivity(intent);
////                finish();
//            }
//        });
//
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                Intent logoutIntent = new Intent(NavDrawer.this, LoginActivity.class);
//                startActivity(logoutIntent);
//                finish();
//            }
//        });
//
//        return builder;
//    }


    //dialog that shows up after submitting a request
        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_sent);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            TextView text = (TextView) dialog.findViewById(R.id.suraksha);
            text.setText(msg);

            Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.no_button);
            dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                    Intent logoutIntent = new Intent(NavDrawer.this, LoginActivity.class);
                    startActivity(logoutIntent);
                    finish();
                }
            });

            Button dialogBtn_okay = (Button) dialog.findViewById(R.id.yes_button);
            dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }


    public JSONObject confirmInput() {
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        JSONObject jsonObject4 = new JSONObject();
        JSONObject jsonObject5 = new JSONObject();

        JSONObject innerJSONObject = new JSONObject();
        JSONObject innerJSONObject1 = new JSONObject();

        JSONObject mainJSONObject = new JSONObject();

        try {

            jsonObject.put("name", "alertBy");
            jsonObject.put("data", value);


            jsonObject1.put("name", "identifierEmployeeCode");
            jsonObject1.put("data", employee_code_number);


            jsonObject2.put("name", "safetyAlertDetails");
            innerJSONObject.put("alertLocation", report_location);
            innerJSONObject.put("nearMiss", Boolean.toString(s1));
            innerJSONObject.put("safetyAlertLandmark", alert_location);
            innerJSONObject.put("incident", alert_description);
            innerJSONObject.put("rootCause", basic_cause);
            innerJSONObject.put("remarks", prevent);
            innerJSONObject.put("requestBy", NavDrawer.employee_id);
            innerJSONObject.put("unsafeAct", Boolean.toString(s2));
            innerJSONObject.put("unsafeCondition", Boolean.toString(s3));
            innerJSONObject.put("storeType", store_type);
            innerJSONObject.put("brand", brand);
            innerJSONObject.put("storeLocation", store_location);
            innerJSONObject.put("city", city);
            innerJSONObject.put("fiscalYear", "2018-19");
            jsonObject2.put("data", innerJSONObject);


            jsonObject3.put("name", "supportingPicture");
            innerJSONObject1.put("contentLength", Encodedstring.length());
            innerJSONObject1.put("mimeType", "image/jpeg");
            innerJSONObject1.put("fileName", "SupportingPicture");
            innerJSONObject1.put("content", Encodedstring);
            jsonObject3.put("data", innerJSONObject1);


            jsonObject4.put("name", "fromApp");
            jsonObject4.put("data", "Yes");

            jsonObject5.put("name", "alertTime");
            jsonObject5.put("data", date + "  " + time);


            jsonArray.put(jsonObject);
            jsonArray.put(jsonObject1);
            jsonArray.put(jsonObject2);
            jsonArray.put(jsonObject3);
            jsonArray.put(jsonObject4);
            jsonArray.put(jsonObject5);

            mainJSONObject.put("input", jsonArray);

            //Log.d("Input json object", mainJSONObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mainJSONObject;
    }


    class SyncData extends AsyncTask<JSONObject, String, String> {


        /*Before starting background thread Show Progress Dialog*/
        ProgressDialog pDialog;


        JSONParser_with_httpURLConnection_AlertDetails jsonParser_with_httpURLConnection_details = new JSONParser_with_httpURLConnection_AlertDetails();
        String url_details = "https://guna.titan.in:8443/bpm/processes?model=Safety%20Alert%20Process&container=SFTYA";
        JSONObject json_details = new JSONObject();

        //UAT API: https://guna.titan.in:8443/bpm/processes?model=Safety%20Alert%20Process&container=SFTYA
        //Production API: https://ebpm.titan.co.in/bpm/processes?model=Safety%20Alert%20Process&container=SFTYA
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NavDrawer.this);
            pDialog.setMessage("Sending Alert details...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /* Creating product
         */
        protected String doInBackground(JSONObject... args) {
            try {
                ////Log.d("JSON Request 1", "JSONObject" + args[0]);
                //Log.d("JSON Request ", "JSONObject" + args[0].toString());

//                json = jsonParser_with_httpURLConnection.makeHttpRequest(url,"POST",args[0]);
//                //Log.d("JSON Response 1",json.toString());

                ////Log.d("JSON Token",json.getString("csrf_token"));

                JSONParser_with_httpURLConnection_AlertDetails.csrftoken = LoginActivity.token;
                json_details = jsonParser_with_httpURLConnection_details.makeHttpRequest(url_details, "POST", args[0]);
                //Log.d("JSON Response ", json_details.toString());

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
                if (json_details.getBoolean("Timeout")) {

                    switch (json_details.getInt("errorNo")) {
                        case 400:
                            Toast.makeText(getApplicationContext(), "Bad request", Toast.LENGTH_LONG).show();
                            break;
                        case 401:
                            Toast.makeText(getApplicationContext(), "Not able to authorize", Toast.LENGTH_LONG).show();
                            break;
                        case 403:
                            Toast.makeText(getApplicationContext(), "Unauthorized to raise alert", Toast.LENGTH_LONG).show();
                            break;
                        case 409:
                            Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
                            break;
                        case 500:
                            Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;

                    }


                } else {

                    showDialog(NavDrawer.this,"S U R A K S H A");
                    return;


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // finish();

        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        TextView date_picker = findViewById(R.id.date_picker);
        String yearF = Integer.toString(yearFinal);
        String monthF = Integer.toString(monthFinal);
        String dayF = Integer.toString(dayFinal);

        if (yearF.length() == 1)
            yearF = "0" + yearF;
        if (monthF.length() == 1)
            monthF = "0" + monthF;
        if (dayF.length() == 1)
            dayF = "0" + dayF;

        date = dayF + "-" + monthF + "-" + yearF;
        date_picker.setText(date);

        date_picker.setError(null);

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        miunteFinal = i1;
        //Log.d("1 hour", Integer.toString(timePicker.getCurrentHour()));

        String AM_PM = "";

        if (hourFinal > 12) {
            hourFinal = hourFinal - 12;
            AM_PM = "PM";
        } else if (hourFinal == 0) {
            hourFinal = hourFinal + 12;
            AM_PM = "AM";
        } else if (hourFinal == 12) {
            AM_PM = "PM";
        } else {
            AM_PM = "AM";
        }

        TextView time_picker = findViewById(R.id.time_picker);
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        year = calendar.get(Calendar.YEAR);

        if (dayFinal == day && monthFinal == month + 1 && yearFinal == year) {
            if (i <= hour && i1 <= minute) {

                String hourF = Integer.toString(hourFinal);
                String minuteF = Integer.toString(miunteFinal);
                if (hourF.length() == 1)
                    hourF = "0" + hourF;
                if (minuteF.length() == 1)
                    minuteF = "0" + minuteF;

                time = hourF + ":" + minuteF + " " + AM_PM;
                time_picker.setText(time);
                time_picker.setError(null);
            } else {
                time_picker.setText("HH:MM");
                Toast.makeText(getApplicationContext(), "Future time is not allowed", Toast.LENGTH_LONG).show();


            }
        } else {
            String hourF = Integer.toString(hourFinal);
            String minuteF = Integer.toString(miunteFinal);
            if (hourF.length() == 1)
                hourF = "0" + hourF;
            if (minuteF.length() == 1)
                minuteF = "0" + minuteF;

            time = hourF + ":" + minuteF + " " + AM_PM;
            time_picker.setText(time);
            time_picker.setError(null);
        }

    }

  Boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                Intent intent = new Intent(NavDrawer.this, NavDrawer.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contact:
                Intent contact = new Intent(NavDrawer.this, ContactUsActivity.class);
                startActivity(contact);
                finish();
                break;
            case R.id.nav_faq:
                Intent faq = new Intent(NavDrawer.this, FAQActivity.class);
                startActivity(faq);
                finish();
                break;
            case R.id.nav_logout:
                Intent logoutIntent = new Intent(NavDrawer.this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static boolean string_to_boolean1(String statusSwitch1) {

        if (statusSwitch1.equals("ON"))
            return true;
        else return false;

    }

    public static boolean string_to_boolean2(String statusSwitch2) {
        if (statusSwitch2.equals("ON"))
            return true;
        else return false;
    }

    public static boolean string_to_boolean3(String statusSwitch3) {
        if (statusSwitch3.equals("ON"))
            return true;
        else return false;
    }


    private void selectImage() {
        final NavDrawer.Item[] items = {
                new NavDrawer.Item("Camera", R.drawable.ic_camera_alt_black_24dp),
                new NavDrawer.Item("Gallery", R.drawable.ic_photo_library_black_24dp),
                new NavDrawer.Item("Cancel", 0),//no icon for this one
        };

        ListAdapter adapter = new ArrayAdapter<NavDrawer.Item>(
                this,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("Add an Image")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            if (ContextCompat.checkSelfPermission(NavDrawer.this,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(NavDrawer.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            } else {
                                // Permission has already been granted
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if(intent.resolveActivity(getPackageManager()) != null) {
                                    try {
                                        photoFile = createImageFile();

                                        if (photoFile != null) {
                                            photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.titan.bpmsafety.fileprovider", photoFile);
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                            startActivityForResult(intent, REQUEST_CAMERA);

                                        }
                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }
                                }


                            }

                        } else if (item == 1) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                        } else if (item == 2) {
                            dialog.dismiss();
                        }
                    }
                }).show();

    }

    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storeDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, REQUEST_CAMERA);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(intent.resolveActivity(getPackageManager()) != null) {
                        try {
                            photoFile = createImageFile();

                            if (photoFile != null) {
                                photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.titan.bpmsafety.fileprovider", photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(intent, REQUEST_CAMERA);

                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"Access permission to the camera is required",Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    //Declaring the variables for Custom list adapter
    public static class Item {
        public final String text;
        public final int icon;

        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    //Code related to the selection of options in dialog box to open camera or import photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {

            if (requestCode == REQUEST_CAMERA) {
//                Bundle bundle = data.getExtras();
//                imageBitmap = (Bitmap) bundle.get("data");
//
//
//                //Bitmap image = (Bitmap) bundle.get("data");

                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                IvImage.setImageBitmap(myBitmap);



                image_layout.setVisibility(VISIBLE);
                close_button.setVisibility(View.VISIBLE);
                IvImage.setVisibility(VISIBLE);

                Encodedstring = imageCompression(myBitmap);



                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IvImage.setVisibility(View.GONE);
                        close_button.setVisibility(View.GONE);
                        image_layout.setVisibility(View.GONE);
                        Encodedstring = "";
                        imageBitmap = null;

                    }
                });




            } else if (requestCode == SELECT_FILE) {

                try {
                    Uri selectedImageUri = data.getData();
                    //Bitmap imageBitmap = BitmapFactory.decodeFile(String.valueOf(selectedImageUri));
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    image_layout.setVisibility(View.VISIBLE);
                    IvImage.setVisibility(View.VISIBLE);
                    //Log.d("URI", String.valueOf(selectedImageUri));
                    //Uri selectedImageUrl = data.getData();
                    IvImage.setImageBitmap(imageBitmap);


                    Encodedstring = imageCompression(imageBitmap);

                    close_button.setVisibility(View.VISIBLE);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

//                viewToUse.setImageResource(android.R.color.transparent);
                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IvImage.setVisibility(View.GONE);
                        close_button.setVisibility(View.GONE);
                        image_layout.setVisibility(View.GONE);
                        Encodedstring = "";

                    }
                });

            }
        }
    }


    public String imageCompression(Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byteArray = outputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    //Method to hide the Keyboard when tapped on screen
    public void hideKeyboard(View view) {


        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.alertBy_spinner:
                //Toast.makeText(parent.getContext(),"OnItemSelected "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                value = parent.getItemAtPosition(position).toString();
                if (value.equals("Self") || value.equals("Other Employees")) {
                    LinearLayout employee_code_layout = findViewById(R.id.employee_code_layout);
                    employee_code_layout.setVisibility(VISIBLE);

                    if (value.equals("Self")) {
                        et_employeecode.setVisibility(View.GONE);
                        tv_employeecode.setVisibility(VISIBLE);
                        tv_employeecode.setText(employee_id);
                        employee_code_number = tv_employeecode.getText().toString();

                    }
                    if (value.equals("Other Employees")) {
                        tv_employeecode.setVisibility(View.GONE);
                        et_employeecode.setVisibility(VISIBLE);

                        //Log.d("Employee id is ", employee_code_number);
                    }

                } else {
                    LinearLayout employee_code_layout = findViewById(R.id.employee_code_layout);
                    employee_code_layout.setVisibility(View.GONE);

                }
                break;
            case R.id.spinner1:
                report_location = parent.getItemAtPosition(position).toString();
                if (report_location.equals("RO-East") || report_location.equals("RO-West") || report_location.equals("RO-North") || report_location.equals("RO-South")) {

                    //Log.d("Report location:",report_location);

                    LinearLayout store_type_layout = findViewById(R.id.store_type_layout);
                    store_type_layout.setVisibility(VISIBLE);

                    Spinner spinner2 = findViewById(R.id.spinner2);
                    spinner2.setSelection(0);

                    Spinner spinner3 = findViewById(R.id.spinner3);
                    spinner3.setSelection(0);

                    EditText et_city = findViewById(R.id.et_city);
                    et_city.setText("");

                    EditText store_location_edittext = findViewById(R.id.et_store_location);
                    store_location_edittext.setText("");


                } else {

                    LinearLayout store_type_layout = findViewById(R.id.store_type_layout);
                    store_type_layout.setVisibility(View.GONE);

                    LinearLayout brand_layout = findViewById(R.id.brand_layout);
                    brand_layout.setVisibility(View.GONE);

                    LinearLayout city_layout = findViewById(R.id.city_layout);
                    city_layout.setVisibility(View.GONE);

                    TextView store_location_textview = findViewById(R.id.store_location_textview);
                    store_location_textview.setVisibility(View.GONE);

                    EditText store_location_edittext = findViewById(R.id.et_store_location);
                    store_location_edittext.setVisibility(View.GONE);

                    Spinner spinner2 = findViewById(R.id.spinner2);
                    spinner2.setSelection(0);

                    store_type = null;

                    Spinner spinner3 = findViewById(R.id.spinner3);
                    spinner3.setSelection(0);

                    brand = null;

                    EditText et_city = findViewById(R.id.et_city);
                    et_city.setText("");

                    store_location_edittext.setText("");
                }

                break;
            case R.id.spinner2:
                store_type = parent.getItemAtPosition(position).toString();
                if (store_type.equals("Retail")) {
                    LinearLayout brand_layout = findViewById(R.id.brand_layout);
                    brand_layout.setVisibility(VISIBLE);

                    LinearLayout city_layout = findViewById(R.id.city_layout);
                    city_layout.setVisibility(VISIBLE);

                    TextView store_location_textview = findViewById(R.id.store_location_textview);
                    store_location_textview.setVisibility(VISIBLE);

                    EditText store_location_edittext = findViewById(R.id.et_store_location);
                    store_location_edittext.setVisibility(VISIBLE);

                    Spinner spinner3 = findViewById(R.id.spinner3);
                    spinner3.setSelection(0);

                    EditText et_city = findViewById(R.id.et_city);
                    et_city.setText("");

                    store_location_edittext.setText("");

                } else {

                    LinearLayout brand_layout = findViewById(R.id.brand_layout);
                    brand_layout.setVisibility(View.GONE);

                    LinearLayout city_layout = findViewById(R.id.city_layout);
                    city_layout.setVisibility(View.GONE);

                    TextView store_location_textview = findViewById(R.id.store_location_textview);
                    store_location_textview.setVisibility(View.GONE);

                    EditText store_location_edittext = findViewById(R.id.et_store_location);
                    store_location_edittext.setVisibility(View.GONE);

                    Spinner spinner3 = findViewById(R.id.spinner3);
                    spinner3.setSelection(0);

                    brand = null;

                    EditText et_city = findViewById(R.id.et_city);
                    et_city.setText("");

                    store_location_edittext.setText("");
                }
                //Toast.makeText(parent.getContext(),"Spinner 3 "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner3:
                brand = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(),"Spinner 4 "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
