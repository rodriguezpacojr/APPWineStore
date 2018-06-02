package com.rodriguezpacojr.winestore.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rodriguezpacojr.winestore.HomeAdminActivity;
import com.rodriguezpacojr.winestore.LoginActivity;
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.RoutesActivity;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.adapters.RoutesListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CRUDRoute extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    @BindView(R.id.txtinsertRoute)
    TextView tvtittle;
    @BindView(R.id.edtnameRoute)
    EditText edtnameRoute;
    @BindView(R.id.spnEmployee)
    Spinner spnEmployee;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    ArrayList<String> arrEm = new ArrayList();
    ArrayList<Integer> arridEm = new ArrayList();
    int keyEmployee;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    Setup setup = new Setup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_route);

        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        getEmployees();

        if (RoutesListAdapter.flatUpdate)
            fillFields();
        else
            btnRegister.setText("REGISTER");
    }

    void fillFields(){
        btnRegister.setText("UPDATE");
        tvtittle.setText("Update Route");
        edtnameRoute.setText(RoutesListAdapter.name);
    }

    public void getEmployees(){
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/employee/listemployees/"+setup.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, this, this){
            @Override
            public Map<String, String> getHeaders () {
                Map<String, String> params = new HashMap<>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResponse(String response) {
        if (response != null) {
            arridEm.clear();
            arridEm.clear();
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("employee");

                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObjectTP = jsonArray.getJSONObject(i);
                    arridEm.add(jsonObjectTP.getInt("keyEmployee"));
                    arrEm.add(jsonObjectTP.getString("name") +" "+ jsonObjectTP.getString("lastName"));
                }
                getSpn();
            }catch (JSONException e){
                Log.e("Error",e.toString());
            }
        }
        else {
            final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
            dialog.setTitle("Warnning!")
                    .setMessage("Your session has ended!\nYou must Login again!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            Intent intInicio = new Intent(CRUDRoute.this, LoginActivity.class);
                            intInicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intInicio);
                            finish();
                        }
                    });
            dialog.show();
        }
    }

    private void getSpn(){
        ArrayAdapter<String> adapterC =  new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,arrEm);
        spnEmployee.setAdapter(adapterC);
        if (RoutesListAdapter.flatUpdate){
            int posi = 0;
            for (int i=0; i < arridEm.size(); i++)
                if (arridEm.get(i) == RoutesListAdapter.keyemployee)
                    posi = i;
            spnEmployee.setSelection(posi);
        }
        else
            spnEmployee.setSelection(0);

        spnEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                System.out.println("POS:"+pos);
                System.out.println("A VER QUE ES: "+arridEm.get(pos));

                System.out.println("***********************************************************************");
                keyEmployee = (arridEm.get(pos));
                System.out.println("KEY EMPLOYEE: "+keyEmployee);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Error", error.toString());
    }

    @OnClick(R.id.btnRegister)
    public void onClick(View v) {
        if(v.getId() == R.id.btnRegister) {

            int method;
            String task, message;
            if(RoutesListAdapter.flatUpdate) {
                method = 2;
                task = "update";
                message = "updated";
            } else {
                method = 1;
                task = "insert";
                message = "inserted";
            }

            String name = edtnameRoute.getText().toString();
            String employee = String.valueOf(keyEmployee);

            jsonObject = new JSONObject();
            try {
                if (RoutesListAdapter.flatUpdate)
                    jsonObject.put("keyRoute", RoutesListAdapter.key);

                jsonObject.put("destination", name);
                jsonObject.put("keyEmployee", employee);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }

            String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/route/"+task+"route/"+setup.getToken();
            //URL += Setting.token;

            stringRequest = new StringRequest(method, URL, this, this) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonObject.toString().getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        Log.e("Error", uee.toString());
                        return null;
                    }
                }
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put(
                            "Authorization",
                            String.format("Basic %s", Base64.encodeToString(
                                    String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            Toast.makeText(this, "The Route was "+message, Toast.LENGTH_SHORT).show();

            HomeAdminActivity.mNumber = 3;
            Intent intent = new Intent(this, HomeAdminActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void deleteRoute(int key) {
        try {
            String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/route/deleteroute/" + key + "/"+setup.getToken();

            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL, this, this) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put(
                            "Authorization",
                            String.format("Basic %s", Base64.encodeToString(
                                    String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };
            ListRoutesFragment.requestQueue.add(stringRequest);
        }catch (Exception e){
            System.out.println("BALE GABER");
        }
    }
}