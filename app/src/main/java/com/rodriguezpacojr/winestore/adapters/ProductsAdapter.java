package com.rodriguezpacojr.winestore.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rodriguezpacojr.winestore.DataBase;
import com.rodriguezpacojr.winestore.ProductsActivity;
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by francisco on 4/21/18.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder>  {

    private List<Product> productsList;
    Context context;

    static DataBase objDB;
    static SQLiteDatabase objSQL;
    Activity activity;

    public static class ProductsHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductCat;
        TextView tvmlNumber;
        TextView tvProductStock;
        EditText edtNum;
        FloatingActionButton btnAdd;

        public ProductsHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductCat = itemView.findViewById(R.id.tvProductCat);
            tvmlNumber = itemView.findViewById(R.id.tvmlNumber);
            tvProductStock = itemView.findViewById(R.id.tvProductStock);
            edtNum = itemView.findViewById(R.id.edtNum);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }

    public ProductsAdapter(List<Product> List, Context con, Activity activity) {
        productsList = List;
        context = con;
        this.activity = activity;

        objDB = new DataBase(context, "winestore", null, 1);
        objSQL = objDB.getWritableDatabase();
    }

    @Override
    public ProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        ProductsAdapter.ProductsHolder productsHolder = new ProductsAdapter.ProductsHolder(view);
        return productsHolder;
    }

    @Override
    public void onBindViewHolder(final ProductsHolder holder, int position) {
        final Product product = productsList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText("$"+product.getSalesPrice());
        holder.tvProductCat.setText(product.getTypeProduct());
        holder.tvmlNumber.setText(product.getMl()+" lts");
        holder.tvProductStock.setText(product.getAvailables().toString());

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.edtNum.getText().toString().isEmpty()) {
                    final int quantity = Integer.parseInt(holder.edtNum.getText().toString());
                    if(quantity <= product.getStock() && quantity > 0) {

                        final JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("keyProduct", product.getKeyProduct());
                            jsonObject.put("availables", -(quantity));
                        } catch (JSONException e) {
                            Log.e("Eroor", e.toString());
                        }

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        String URL = "http://" + Setup.getIpAddress() + ":" + Setup.getPortNumber() + "/IosAnd/api/product/updateavailables/"+Setup.getToken();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.PUT, URL, jsonObject,
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

                        ContentValues row = new ContentValues();
                        row.put("keycustomer", Setup.getKeyCustomer());
                        row.put("keyproduct", product.getKeyProduct());
                        row.put("product", product.getName());
                        row.put("price", product.getSalesPrice());
                        row.put("quantity", quantity);

                        objSQL.insert("shoppingcart", null, row);
                        Toast.makeText(context,"YOU ADD "+quantity+" "+product.getName()+" TO SHOPPING CART",Toast.LENGTH_SHORT).show();

                        activity.recreate();
                    } else
                        Toast.makeText(context,"There aren't enough products",Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context,"Type the quantity",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}