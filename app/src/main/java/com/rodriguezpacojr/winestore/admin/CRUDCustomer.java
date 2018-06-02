package com.rodriguezpacojr.winestore.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.rodriguezpacojr.winestore.MapsActivity;
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.RoutesActivity;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.adapters.CustomerListAdapter;
import com.rodriguezpacojr.winestore.adapters.CustomerListAdapter;
import com.rodriguezpacojr.winestore.adapters.ProductsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CRUDCustomer extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    @BindView(R.id.txtinsertCustomer)
    TextView textView;
    @BindView(R.id.edtname)
    EditText edtname;
    @BindView(R.id.edtlastName)
    EditText edtlastName;

    @BindView(R.id.edtbornDate)
    EditText edtbornDate;
    @BindView(R.id.imgCalendarBD)
    ImageView imgCalendarBD;

    @BindView(R.id.edtemail)
    EditText edtemail;
    @BindView(R.id.edtphone)
    EditText edtphone;
    @BindView(R.id.edtrfc)
    EditText edtrfc;

    @BindView(R.id.edtentryDate)
    EditText edtentryDate;
    @BindView(R.id.imgCalendarED)
    ImageView imgCalendarED;

    @BindView(R.id.spnRoute)
    Spinner spnRoute;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    public static TextView latitudeValueGPS;
    public static TextView longitudeValueGPS;

    ArrayList<String> arrRo = new ArrayList();
    ArrayList<Integer>arridRo = new ArrayList();
    int keyRoute;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    LocationManager locationManager;
    public static double longitudeGPS = -1, latitudeGPS=-1;
    DatePickerDialog datePickerDialog;
    Setup setup = new Setup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_customer);

        ButterKnife.bind(this);
        latitudeValueGPS = findViewById(R.id.latitudeValueGPS);
        longitudeValueGPS = findViewById(R.id.longitudeValueGPS);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        requestQueue = Volley.newRequestQueue(this);
        if (CustomerListAdapter.flagUpdate)
            fillFields();
        else
            btnRegister.setText("REGISTER");
        init();


        calendars();
    }

    private void calendars() {
        imgCalendarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CRUDCustomer.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                                monthOfYear++;
                                String separatorm = "-";
                                String separatord = "-";
                                if (monthOfYear<10)
                                    separatorm+="0";
                                if (dayOfMonth<10)
                                    separatord+="0";
                                edtbornDate.setText(year + separatorm + monthOfYear + separatord + (dayOfMonth));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        imgCalendarED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CRUDCustomer.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        String separatorm = "-";
                        String separatord = "-";
                        if (monthOfYear<10)
                            separatorm+="0";
                        if (dayOfMonth<10)
                            separatord+="0";
                        edtentryDate.setText(year + separatorm + monthOfYear + separatord + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    void fillFields(){
        btnRegister.setText("UPDATE");
        textView.setText("Update Customer");
        edtname.setText(CustomerListAdapter.name);
        edtlastName.setText(CustomerListAdapter.lastName);
        edtbornDate.setText(CustomerListAdapter.bornDate);
        edtphone.setText(CustomerListAdapter.phone);
        edtemail.setText(CustomerListAdapter.email);
        edtrfc.setText(CustomerListAdapter.rfc);
        edtentryDate.setText(CustomerListAdapter.entryDate);
        latitudeValueGPS.setText(CustomerListAdapter.latitude+"");
        longitudeValueGPS.setText(CustomerListAdapter.longitude+"");

        latitudeGPS = CustomerListAdapter.latitude;
        longitudeGPS = CustomerListAdapter.longitude;
    }



    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void init(){
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/route/listroutes/"+setup.getToken();
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
            arridRo.clear(); arrRo.clear();
            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONArray jsonArrayC = jsonObject.getJSONArray("route");

                for (int i=0; i<jsonArrayC.length(); i++){
                    JSONObject jsonObjectTP = jsonArrayC.getJSONObject(i);
                    arridRo.add(jsonObjectTP.getInt("keyRoute"));
                    arrRo.add(jsonObjectTP.getString("destination"));
                }

                spnCity();
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

                            Intent intInicio = new Intent(CRUDCustomer.this, LoginActivity.class);
                            intInicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intInicio);
                            finish();
                        }
                    });
            dialog.show();
        }
    }

    private void spnCity(){
        ArrayAdapter<String> adapterC =  new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,arrRo);
        spnRoute.setAdapter(adapterC);
        if (CustomerListAdapter.flagUpdate){
            int posi = 0;
            for (int i=0; i < arridRo.size(); i++)
                if (arridRo.get(i) == CustomerListAdapter.keyRoute)
                    posi = i;
            spnRoute.setSelection(posi);
        }
        else
            spnRoute.setSelection(0);

        spnRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                keyRoute = (arridRo.get(pos));
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
            getLatitudeGPS();
            if (latitudeGPS != -1) {

                int method;
                String task, message;
                if(CustomerListAdapter.flagUpdate) {
                    method = 2;
                    task = "update";
                    message = "updated";
                } else {
                    method = 1;
                    task = "insert";
                    message = "inserted";
                }

                String name = edtname.getText().toString();
                String lastName = edtlastName.getText().toString();
                String bornDate = edtbornDate.getText().toString();
                String email = edtemail.getText().toString();
                String phone = edtphone.getText().toString();
                String rfc = edtrfc.getText().toString();
                String entryDate = edtentryDate.getText().toString();
                String route = String.valueOf(keyRoute);
                Double lat = getLatitudeGPS();
                Double lng = getLongitudeGPS();

                jsonObject = new JSONObject();
                try {
                    if (CustomerListAdapter.flagUpdate)
                        jsonObject.put("keyCustomer", CustomerListAdapter.key);

                    jsonObject.put("name", name);
                    jsonObject.put("lastName", lastName);
                    jsonObject.put("bornDate", bornDate);
                    jsonObject.put("email", email);
                    jsonObject.put("phone", phone);
                    jsonObject.put("RFC", rfc);
                    jsonObject.put("entryDate", entryDate);
                    jsonObject.put("photo", "foto");
                    jsonObject.put("latitude", lat);
                    jsonObject.put("longitude", lng);
                    jsonObject.put("keyRoute", route);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }

                String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/customer/"+task+"customer/"+setup.getToken();

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
                Toast.makeText(this, "The Customer was "+message, Toast.LENGTH_SHORT).show();

                HomeAdminActivity.mNumber = 0;
                Intent intent = new Intent(this, HomeAdminActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void deleteCustomer(int key) {
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/customer/deletecustomer/"+key+"/"+setup.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL, this, this){
            @Override
            public Map<String, String> getHeaders (){
                Map<String, String> params = new HashMap<>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "root", "root").getBytes(), Base64.DEFAULT)));
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };
        ListCustomersFragment.requestQueue.add(stringRequest);
    }

    public static double getLongitudeGPS() {
        return longitudeGPS;
    }

    public static void setLongitudeGPS(double longitudeGPS) {
        CRUDCustomer.longitudeGPS = longitudeGPS;
    }

    public static double getLatitudeGPS() {
        return latitudeGPS;
    }

    public static void setLatitudeGPS(double latitudeGPS) {
        CRUDCustomer.latitudeGPS = latitudeGPS;
    }
}