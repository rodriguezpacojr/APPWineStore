package com.rodriguezpacojr.winestore.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rodriguezpacojr.winestore.DataBase;
import com.rodriguezpacojr.winestore.ProductsActivity;
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.ShoppingCartActivity;
import com.rodriguezpacojr.winestore.admin.CRUDEmployee;
import com.rodriguezpacojr.winestore.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartHolder> {

    private List<Product> shoppingCartList;
    Context context;
    static DataBase objDB;
    static SQLiteDatabase objSQL;
    Activity activity;

    public static class ShoppingCartHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
        ImageView btnDelete;

        public ShoppingCartHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public ShoppingCartAdapter(List<Product> List, Context con, Activity activity) {
        shoppingCartList = List;
        context = con;

        this.activity = activity;

        objDB = new DataBase(context, "winestore", null, 1);
        objSQL = objDB.getWritableDatabase();
    }

    @Override
    public ShoppingCartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_row, parent, false);
        ShoppingCartAdapter.ShoppingCartHolder shoppingCartHolder = new ShoppingCartAdapter.ShoppingCartHolder(view);
        return shoppingCartHolder;
    }

    @Override
    public void onBindViewHolder(final ShoppingCartHolder holder, int position) {
        final Product product = shoppingCartList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText("$"+product.getSalesPrice());
        holder.tvQuantity.setText(product.getQuantity().toString());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete")
                        .setMessage("You will delete "+product.getName()+"\nAre you Sure?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                final JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("keyProduct", product.getKeyProduct());
                                    jsonObject.put("availables", product.getQuantity());
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

                                objSQL.execSQL("DELETE FROM shoppingcart WHERE keycustomer = "+Setup.getKeyCustomer()+" AND keyproduct = "+product.getKeyProduct());

                                Toast.makeText(context,"PRODUCT DELETED FROM SHOPPING CART",Toast.LENGTH_SHORT).show();

                                Intent intInicio = new Intent(context, ShoppingCartActivity.class);
                                context.startActivity(intInicio);
                                activity.finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCartList.size();
    }
}