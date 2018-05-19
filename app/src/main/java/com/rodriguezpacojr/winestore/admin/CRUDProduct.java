package com.rodriguezpacojr.winestore.admin;

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
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.adapters.ProductsListAdapter;

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

public class CRUDProduct extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    @BindView(R.id.txtinsertProduct)
    TextView tvtittle;
    @BindView(R.id.edtnameProduct)
    EditText edtnameProduct;
    @BindView(R.id.edtml)
    EditText edtml;
    @BindView(R.id.edtcolor)
    EditText edtcolor;
    @BindView(R.id.edttaste)
    EditText edttaste;
    @BindView(R.id.edtstock)
    EditText edtstock;
    @BindView(R.id.edtsalesPrice)
    EditText edtsalesPrice;
    @BindView(R.id.spnCategory)
    Spinner spnCategory;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    ArrayList<String> arrTP = new ArrayList();
    ArrayList<Integer> arridTP = new ArrayList();
    int keyCategory;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    Setup setup = new Setup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(this);
        getTypeProduct();

        if (ProductsListAdapter.flatUpdate)
            fillFields();
        else
            btnRegister.setText("REGISTER");
    }

    void fillFields(){
        btnRegister.setText("UPDATE");
        tvtittle.setText("Update Product");
        edtnameProduct.setText(ProductsListAdapter.name);
        edtcolor.setText(ProductsListAdapter.color);
        edtml.setText(ProductsListAdapter.ml+"");
        edttaste.setText(ProductsListAdapter.taste);
        edtsalesPrice.setText(ProductsListAdapter.price+"");
        edtstock.setText(ProductsListAdapter.stock+"");
    }

    public void getTypeProduct(){
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/typeproduct/listtypeproducts/"+setup.getToken();
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
        arridTP.clear();
        arridTP.clear();

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("typeproduct");

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObjectTP = jsonArray.getJSONObject(i);
                arridTP.add(jsonObjectTP.getInt("keyTypeProduct"));
                arrTP.add(jsonObjectTP.getString("nameTypeProduct"));
            }
            getSpn();
        }catch (JSONException e){
            Log.e("Error",e.toString());
        }
    }

    private void getSpn(){
        ArrayAdapter<String> adapterC =  new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,arrTP);
        spnCategory.setAdapter(adapterC);
        spnCategory.setSelection(0);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                keyCategory = (arridTP.get(pos));
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
            if(ProductsListAdapter.flatUpdate) {
                method = 2;
                task = "update";
                message = "updated";
            } else {
                method = 1;
                task = "insert";
                message = "inserted";
            }

            String name = edtnameProduct.getText().toString();
            String lts = edtml.getText().toString();
            String color = edtcolor.getText().toString();
            String taste = edttaste.getText().toString();
            String stock = edtstock.getText().toString();
            String salesPrice = edtsalesPrice.getText().toString();
            String category = String.valueOf(keyCategory);

            jsonObject = new JSONObject();
            try {
                if (ProductsListAdapter.flatUpdate)
                    jsonObject.put("keyProduct", ProductsListAdapter.key);
                else
                    jsonObject.put("availables", stock);

                jsonObject.put("name", name);
                jsonObject.put("ml", lts);
                jsonObject.put("color", color);
                jsonObject.put("taste", taste);
                jsonObject.put("stock", stock);
                jsonObject.put("salesPrice", salesPrice);
                jsonObject.put("keyTypeProduct", category);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }

            String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/product/"+task+"product/"+setup.getToken();
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
            Toast.makeText(this, "The Product was "+message, Toast.LENGTH_SHORT).show();

            HomeAdminActivity.mNumber = 2;
            Intent intent = new Intent(this, HomeAdminActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void deleteProduct(int key) {
        try {
            String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/product/deleteproduct/" + key + "/"+setup.getToken();

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
            ListProductsFragment.requestQueue.add(stringRequest);
        }catch (Exception e){
            System.out.println("BALE GABER");
        }
    }
}