package com.rodriguezpacojr.winestore;

import android.app.ProgressDialog;
import android.os.Handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.rodriguezpacojr.winestore.adapters.ShoppingCartAdapter;
import com.rodriguezpacojr.winestore.models.Order;
import com.rodriguezpacojr.winestore.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingCartActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    ArrayList<Product> shoppingCartList;
    ArrayList<Order> orderList;

    RecyclerView rvshoppingCartList;

    @BindView(R.id.btnFinish)
    FloatingActionButton btnFinish;

    Activity activity = this;
    android.content.Context context = this;

    private DataBase objDB;
    private SQLiteDatabase objSQL;

    private int maxOrder, keyemployee;
    private android.app.ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);

        objDB = new DataBase(this, "winestore", null, 1);
        objSQL = objDB.getReadableDatabase();

        shoppingCartList = new ArrayList<>();
        orderList = new ArrayList<>();
        rvshoppingCartList = (RecyclerView) findViewById(R.id.rvshoppingList);
        rvshoppingCartList.setLayoutManager(new LinearLayoutManager(this));

        getShoppingCart();

        ShoppingCartAdapter adapter = new ShoppingCartAdapter(shoppingCartList, this, activity);
        rvshoppingCartList.setAdapter(adapter);
        getLastOrder();

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
            keyemployee = jsonObject.getInt("keyEmployee");
            maxOrder = jsonObject.getInt("keyOrder");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getShoppingCart() {
        Product product = null;
        Order order = null;
        Cursor myCursor = objSQL.rawQuery("SELECT keyproduct, product, price, quantity, keycustomer, keyorder, keyemployee FROM shoppingcart WHERE keycustomer = " + Setup.getKeyCustomer(), null);

        while(myCursor.moveToNext()) {
            product = new Product();
            order = new Order();

            product.setKeyProduct(myCursor.getInt(0));
            product.setName(myCursor.getString(1));
            product.setSalesPrice(myCursor.getDouble(2));
            product.setQuantity(myCursor.getInt(3));

            order.setKeyProduct(myCursor.getInt(0));
            order.setQuantity(myCursor.getInt(3));
            order.setKeyCustomer(myCursor.getInt(4));
            order.setKeyOrder(myCursor.getInt(5));
            order.setKeyEmployee(myCursor.getInt(6));

            shoppingCartList.add(product);
            orderList.add(order);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.itmOrder:
                i = new Intent(this, ProductsActivity.class);
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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ShoppingCartActivity.this, R.style.AppTheme_AlertDialog);
                android.view.LayoutInflater inflater = ShoppingCartActivity.this.getLayoutInflater();
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

    @OnClick(R.id.btnFinish)
    public void onClick(View v) {
        if (v.getId() == R.id.btnFinish) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Finish")
                    .setMessage("Do you want finish the Order?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            insertOrder();
                            progressBar = new android.app.ProgressDialog(context);//Create new object of progress bar type
                            progressBar.setCancelable(false);//Progress bar cannot be cancelled by pressing any wher on screen
                            progressBar.setMessage("Finishing Order...");//Tiitle shown in the progress bar
                            progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);//Style of the progress bar
                            progressBar.setProgress(0);//attributes
                            progressBar.setMax(100);//attributes
                            progressBar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.cancel();
                                     finishOrder();
                                }
                            }, 3000);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();
        }
    }

    void finishOrder() {
        for (int i=0; i< orderList.size(); i++) {
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("keyOrder", maxOrder+1);
                jsonObject.put("keyProduct", orderList.get(i).getKeyProduct());
                jsonObject.put("quantity", orderList.get(i).getQuantity());
            } catch (JSONException e) {
                Log.e("Eroor", e.toString());
            }

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://" + Setup.getIpAddress() + ":" + Setup.getPortNumber() + "/IosAnd/api/orderdetail/insertorderdetail/"+Setup.getToken();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, URL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }){
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
            requestQueue.add(jsonObjectRequest);
        }

        objSQL.execSQL("DELETE FROM shoppingcart WHERE keycustomer = "+Setup.getKeyCustomer());
        Toast.makeText(this, "Order Finished", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RoutesOnMapActivity.class);
        startActivity(intent);
        finish();
    }

    void insertOrder() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("keyOrder", maxOrder+1);
            jsonObject.put("keyEmployee", keyemployee);
            jsonObject.put("keyCustomer", orderList.get(0).getKeyCustomer());
        } catch (JSONException e) {
            Log.e("Eroor", e.toString());
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://" + Setup.getIpAddress() + ":" + Setup.getPortNumber() + "/IosAnd/api/order/insertorder/"+Setup.getToken();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
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
        requestQueue.add(jsonObjectRequest);
    }

    private void getLastOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://" + Setup.getIpAddress() + ":" + Setup.getPortNumber() + "/IosAnd/api/order/getdata/"+Setup.getKeyCustomer()+"/"+Setup.getToken();
        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.GET, URL, this, this){
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
}