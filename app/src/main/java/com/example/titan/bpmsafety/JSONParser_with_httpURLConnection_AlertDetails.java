package com.example.titan.bpmsafety;

/**
 * Created by e1596739 on 5/8/2017.
 */
//public class JSONParser_with_httpURLConnection {
//}
//Error No 100-Socket Timeout
// 101-Connection Error
//102- I/O Exception
//104 - General Exception Internal Error

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONParser_with_httpURLConnection_AlertDetails {

    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    StringBuilder sbParams;
    String paramsString;
    static String csrftoken;
    final String errorNo = "ErrorNo";

    public JSONObject makeHttpRequest(String url, String method, JSONObject params) {


            try {

                if ("GET".equals(method)) {

                urlObj = new URL(url);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod(method);
                conn.connect();


            } else {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("POST");


                Log.d("Token here",csrftoken);
                //conn.setRequestProperty("BPMCSRFToken", csrftoken);
                    String token = csrftoken;
                    Log.d("Token again",token);
                    conn.setRequestProperty("BPMCSRFToken", token);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Authorization",JSONParser_with_httpURLConnection.basicAuth);
                    Log.d("Encoded String", JSONParser_with_httpURLConnection.basicAuth);

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);

                conn.connect();

                paramsString = params.toString();
                Log.d("JSON Params", paramsString);
                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();
            }




                //Receive the response from the server
                Log.d("Response code", conn.getResponseCode()+"");
                Log.d("Response message",conn.getResponseMessage()+"");
                // If response code not 201 then we are returning HTTP Error codes
                if(conn.getResponseCode()!= 201){
                    try {
                        jObj = new JSONObject();
                        jObj.put("Timeout", true);
                        jObj.put("errorNo", conn.getResponseCode());
                        conn.disconnect();
                        // Log.d("Socket Exception", "TimeOut");
                        return jObj;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("JSON Parser", "result: " + result.toString());

            } catch (SocketTimeoutException exc) {
                Log.d("Socket Exception", "TimeOut");
                try {
                    jObj = new JSONObject();
                    jObj.put("Timeout", true);
                    jObj.put(errorNo, 100);
                    conn.disconnect();
                    // Log.d("Socket Exception", "TimeOut");
                    return jObj;
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (ConnectException con) {

                Log.d("Connection Exception", "TimeOut");
                try {
                    jObj = new JSONObject();
                    jObj.put("Timeout", true);
                    jObj.put(errorNo, 101);
                    conn.disconnect();
                    // Log.d("Socket Exception", "TimeOut");
                    return jObj;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {

                e.printStackTrace();
                try {
                    jObj = new JSONObject();
                    jObj.put("Timeout", true);
                    jObj.put(errorNo, 102);
                    conn.disconnect();
                    return jObj;
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                Log.d(" Exception ", "In JsonParser");
                e.printStackTrace();
                try {
                    jObj = new JSONObject();
                    jObj.put("Timeout", true);
                    jObj.put(errorNo, 104);
                } catch (JSONException x) {
                    x.printStackTrace();
                }
                conn.disconnect();
                Log.d(" Exception ", "In JsonParser");
                return jObj;
            }


        conn.disconnect();

        // try parse the string to a JSON object
        try {

            jObj = new JSONObject(result.toString());
            jObj.put("Timeout", false);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            try {
                jObj = new JSONObject();
                jObj.put("Timeout", true);
                jObj.put(errorNo, 103);
            } catch (JSONException x) {
                x.printStackTrace();
            }
        } catch (Exception exec) {
            exec.printStackTrace();
            try {
                jObj = new JSONObject();
                jObj.put("Timeout", true);
                jObj.put(errorNo, 104);
            } catch (JSONException x) {
                x.printStackTrace();
            }

            Log.d(" Exception ", "In JsonParser");
            return jObj;
        }

        // return JSON Object
        return jObj;
    }

}
