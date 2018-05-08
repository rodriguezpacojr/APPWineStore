package com.rodriguezpacojr.winestore.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.adapters.ProductsListAdapter;
import com.rodriguezpacojr.winestore.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListProductsFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private RecyclerView rvproduct;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton btnAddItem;

    public static RequestQueue requestQueue;
    List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        productList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());

        rvproduct = (RecyclerView) v.findViewById(R.id.rvCEPList);
        rvproduct.setHasFixedSize(true);

        getProducts();

        layoutManager = new LinearLayoutManager(getActivity());
        rvproduct.setLayoutManager(layoutManager);


        btnAddItem = (FloatingActionButton) v.findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductsListAdapter.flatUpdate = false;
                Intent intent = new Intent(getActivity(), CRUDProduct.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
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
                objProduct.setTaste(jsonObjectProduct.getString("taste"));
                objProduct.setColor(jsonObjectProduct.getString("color"));
                productList.add(objProduct);
            }
            adapter = new ProductsListAdapter(productList, getActivity());
            rvproduct.setAdapter(adapter);
        }catch (JSONException e){
            Log.e("Error",e.toString());
        }
    }

    private void getProducts(){
        Setup setup = new Setup();
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/product/listproducts/"+setup.getToken();

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
}