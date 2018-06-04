package com.rodriguezpacojr.winestore;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by francisco on 4/27/18.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService implements Response.Listener<String>, Response.ErrorListener {

    public static final String TAG = "NOTICIAS";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token);
    }

    private void registerToken(String token) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        String URL = "http://192.168.43.179/notifications/register_token.php?token="+token;
        //String URL = "http://10.149.163.38/notifications/register_token.php?token="+token;

        Log.d(TAG, "URL: " + token);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,this,this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Error", error.toString());
    }

    @Override
    public void onResponse(String response) {

    }
}