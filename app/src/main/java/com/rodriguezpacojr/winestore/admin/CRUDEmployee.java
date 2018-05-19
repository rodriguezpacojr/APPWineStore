package com.rodriguezpacojr.winestore.admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.adapters.EmployeesListAdapter;

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

public class CRUDEmployee extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    @BindView(R.id.txtinsertEmployee)
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
    @BindView(R.id.spnUser)
    Spinner spnUser;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    ArrayList<String> arrCi = new ArrayList();
    ArrayList<Integer> arridCi = new ArrayList();
    int keyUser;

    DatePickerDialog datePickerDialog;

    private static RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    Setup setup = new Setup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_employee);

        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        if (EmployeesListAdapter.flagUpdate)
            fillFields();
        else
                btnRegister.setText("REGISTER");
        init();

        calendars();
    }

    void fillFields(){
        btnRegister.setText("UPDATE");
        textView.setText("Update Employee");
        edtname.setText(EmployeesListAdapter.name);
        edtlastName.setText(EmployeesListAdapter.lastName);
        edtbornDate.setText(EmployeesListAdapter.bornDate);
        edtphone.setText(EmployeesListAdapter.phone);
        edtemail.setText(EmployeesListAdapter.email);
        edtrfc.setText(EmployeesListAdapter.rfc);
        edtentryDate.setText(EmployeesListAdapter.entryDate);
    }

    private void calendars() {
        imgCalendarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CRUDEmployee.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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

                datePickerDialog = new DatePickerDialog(CRUDEmployee.this, new DatePickerDialog.OnDateSetListener() {
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

    public void init(){
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/user/listusers/"+setup.getToken();
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
        arridCi.clear(); arrCi.clear();

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArrayG = jsonObject.getJSONArray("user");

            for (int i=0; i<jsonArrayG.length(); i++){
                JSONObject jsonObjectTP = jsonArrayG.getJSONObject(i);
                arridCi.add(jsonObjectTP.getInt("keyUser"));
                arrCi.add(jsonObjectTP.getString("userName"));
            }

            spnCity();
        }catch (JSONException e){
            Log.e("Error",e.toString());
        }
    }

    private void spnCity(){
        ArrayAdapter<String> adapterC =  new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,arrCi);
        spnUser.setAdapter(adapterC);
        spnUser.setSelection(0);
        spnUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                keyUser = (arridCi.get(pos));
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
            if(EmployeesListAdapter.flagUpdate) {
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
            String user = String.valueOf(keyUser);

            jsonObject =  new JSONObject();
            try {
                if (EmployeesListAdapter.flagUpdate)
                    jsonObject.put("keyEmployee", EmployeesListAdapter.key);
                else {
                    jsonObject.put("photo", "foto");
                    jsonObject.put("keyUser", user);
                }

                jsonObject.put("name", name);
                jsonObject.put("lastName", lastName);
                jsonObject.put("bornDate", bornDate);
                jsonObject.put("email", email);
                jsonObject.put("phone", phone);
                jsonObject.put("RFC", rfc);
                jsonObject.put("entryDate", entryDate);
            }
            catch (Exception e) {
                Log.e("Error", e.toString());
            }
            System.out.println("JSONOBJECT: "+jsonObject);

            String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/employee/"+task+"employee/"+setup.getToken();

            stringRequest = new StringRequest(method, URL, this, this) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonObject.toString().getBytes("utf-8");
                    }
                    catch (UnsupportedEncodingException uee) {
                        Log.e("Error", uee.toString());
                        return null;
                    }
                }
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
            Toast.makeText(this, "The Employee was "+message, Toast.LENGTH_SHORT).show();

            HomeAdminActivity.mNumber = 1;
            Intent intent = new Intent(this, HomeAdminActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void deleteEmployee(int key) {
        try {
            String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/employee/deleteemployee/" + key + "/"+setup.getToken();
            System.out.println("***************************************\nURL: " + URL);
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
            ListEmployeesFragment.requestQueue.add(stringRequest);
        }catch (Exception e){
            System.out.println("BALE GABER");
        }
    }
}