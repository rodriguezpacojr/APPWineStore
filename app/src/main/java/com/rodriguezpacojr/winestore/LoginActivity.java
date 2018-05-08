package com.rodriguezpacojr.winestore;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    @BindView(R.id.edtuser) EditText edtuser;
    @BindView(R.id.edtpassword) EditText edtpassword;
    @BindView(R.id.chbsession) CheckBox chbsession;
    @BindView(R.id.btnlogIn) Button btnlogIn;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        Log.e("volley_error", error.toString());
    }

    @Override
    public void onResponse(String response) {
        Log.d("volley_response", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String token = jsonObject.getString("token");

            Setup.setToken(token);

            if (!token.equalsIgnoreCase("Access Denied"))
                startActivity(new Intent(this, SplashScreenActivity.class));
            else
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

    void validate() {
        Setup setup = new Setup();
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/user/validate/";
        String user = edtuser.getText().toString();
        String password = edtpassword.getText().toString();
        URL += user + "/" + password;

        setup.setUSER(user);
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
                Intent intConf = new Intent(this, SetIpActivity.class);
                startActivity(intConf);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}