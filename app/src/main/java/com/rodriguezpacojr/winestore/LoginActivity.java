package com.rodriguezpacojr.winestore;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rodriguezpacojr.winestore.admin.CRUDCustomer;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    @BindView(R.id.edtuser) EditText edtuser;
    @BindView(R.id.edtpassword) EditText edtpassword;
    @BindView(R.id.chbsession) CheckBox chbsession;
    @BindView(R.id.btnlogIn) Button btnlogIn;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private android.app.ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        edtuser.setText("admin");
        edtpassword.setText("admin");

        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        Log.e("volley_error", error.toString());
    }

    @OnClick(R.id.btnlogIn)
    public void onClick(View v) {
        if (v.getId() == R.id.btnlogIn) {
            if (edtuser.getText().toString().equals("") || edtpassword.getText().toString().equals(""))
                Toast.makeText(this,"Fill all the fields",Toast.LENGTH_SHORT).show();
            else
                validate();
        }
    }

    private String getMD5(final String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for(int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while(h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", "md5() NoSuchAlgorithmException: " + e.getMessage());
        }
        return "";
    }

    void validate() {
        Setup setup = new Setup();
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/user/validate/";
        String user = edtuser.getText().toString();
        String password = edtpassword.getText().toString();
        password = getMD5(password);
        URL += user + "/" + password;

        Setup.setUserName(user);
        stringRequest = new StringRequest(Request.Method.GET, URL, this, this){
            @Override
            public Map<String, String> getHeaders () {
                Map<String, String> params = new HashMap<>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                return params;
            }
        };

        requestQueue.add(stringRequest);
        System.out.println("DDDDDDDDDDDDDDDDDD: " +requestQueue);
    }

    @Override
    public void onResponse(String response) {
        Log.d("volley_response", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String token = jsonObject.getString("token");
            final String role = jsonObject.getString("role");

            Setup.setToken(token);

            if (!token.equalsIgnoreCase("Access Denied")) {
                progressBar = new android.app.ProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Get Started...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.cancel();
                        Intent intInicio;
                        if (role.equals("admin"))
                            intInicio = new Intent(LoginActivity.this, HomeAdminActivity.class);
                        else
                            intInicio = new Intent(LoginActivity.this, RoutesActivity.class);

                        intInicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intInicio);
                    }
                }, 3000);
            }
            else
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itmSettings:
                openSetting();
                //dialogLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void openSetting() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.activity_set_ip, null);

        final EditText edtipAddress = (EditText) mView.findViewById(R.id.edtipAddress);
        final EditText edtportNumber = (EditText) mView.findViewById(R.id.edtportNumber);
        edtipAddress.setText("192.168.1.86");
        edtportNumber.setText("8080");

        mBuilder.setView(mView)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String ip = edtipAddress.getText().toString();
                String port = edtportNumber.getText().toString();
                Setup setup = new Setup();
                setup.setIpAddress(ip);
                setup.setPortNumber(port);
            }
        });
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}