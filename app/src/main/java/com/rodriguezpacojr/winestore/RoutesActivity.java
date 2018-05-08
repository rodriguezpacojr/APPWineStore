package com.rodriguezpacojr.winestore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rodriguezpacojr.winestore.adapters.RoutesAdapter;
import com.rodriguezpacojr.winestore.models.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private RecyclerView rvroute;
    private RoutesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;
    List<Route> routeList;
    public static int routeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        routeList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        rvroute = (RecyclerView) findViewById(R.id.rvroutesList);
        rvroute.setHasFixedSize(true);

        getRoutes();
        System.out.println("ONCREATE");
        layoutManager = new LinearLayoutManager(this);
        rvroute.setLayoutManager(layoutManager);
    }

    private void getRoutes(){
        Setup setup = new Setup();
        String URL = "http://"+ setup.getIpAddress() +":"+ setup.getPortNumber() +"/IosAnd/api/route/getroutes/2/"+ setup.getToken();
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
        Route objRoute;
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("route");
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObjectRoute = jsonArray.getJSONObject(i);
                objRoute = new Route();
                objRoute.setNumber(jsonObjectRoute.getInt("keyRoute"));
                objRoute.setDestination(jsonObjectRoute.getString("destination"));
                objRoute.setCustomers(jsonObjectRoute.getInt("customers"));
                routeList.add(objRoute);
            }
            adapter = new RoutesAdapter(routeList, this);
            rvroute.setAdapter(adapter);

            adapter.setOnItemClickListener(
                    new RoutesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            routeNum = routeList.get(position).getKeyRoute();
                            //adapter.notifyItemChanged(position);

                            Intent intaboutOf = new Intent(RoutesActivity.this, RoutesOnMapActivity.class);
                            RoutesActivity.this.startActivity(intaboutOf);
                        }
                    });
        }catch (JSONException e){
            Log.e("Error",e.toString());
        }
    }
}