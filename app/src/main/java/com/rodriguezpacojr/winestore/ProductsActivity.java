package com.rodriguezpacojr.winestore;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rodriguezpacojr.winestore.adapters.ProductsAdapter;
import com.rodriguezpacojr.winestore.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private RecyclerView rvproduct;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JSONObject jsonObject;

    List<Product> productList;
    Activity activity = this;
    private android.app.ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        productList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        rvproduct = (RecyclerView) findViewById(R.id.rvProductList);
        rvproduct.setHasFixedSize(true);

        getProducts();

        layoutManager = new LinearLayoutManager(this);
        rvproduct.setLayoutManager(layoutManager);
    }

    private void getProducts(){
        Setup setup = new Setup();
        String URL = "http://"+ setup.getIpAddress() +":"+ setup.getPortNumber() +"/IosAnd/api/product/listproducts/"+ setup.getToken();
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
    public void onErrorResponse(VolleyError error) {
        Log.e("Error", error.toString());
    }

    @Override
    public void onResponse(String response) {
        Product objProduct;
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("product");

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObjectProduct = jsonArray.getJSONObject(i);

                objProduct = new Product();
                objProduct.setKeyProduct(jsonObjectProduct.getInt("keyProduct"));
                objProduct.setName(jsonObjectProduct.getString("name"));
                objProduct.setSalesPrice(jsonObjectProduct.getDouble("salesPrice"));
                objProduct.setTypeProduct(jsonObjectProduct.getString("tp"));
                objProduct.setMl(jsonObjectProduct.getDouble("ml"));
                objProduct.setStock(jsonObjectProduct.getInt("stock"));
                objProduct.setAvailables(jsonObjectProduct.getInt("availables"));
                productList.add(objProduct);
            }
            adapter = new ProductsAdapter(productList, this, activity);
            rvproduct.setAdapter(adapter);
        }catch (JSONException e) {
            Log.e("Error",e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_orders, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.itmShopping:
                i = new Intent(this, ShoppingCartActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.itmCustomers:
                i = new Intent(this, RoutesOnMapActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.itmRoutes:
                i = new Intent(this, RoutesActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.itmLogOut:
                progressBar = new android.app.ProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.setMessage("LogOut...");
                progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.cancel();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
                break;
            case R.id.itmAboutOf:
                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this, R.style.AppTheme_AlertDialog);
                android.view.LayoutInflater inflater = ProductsActivity.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.about_of, null))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}