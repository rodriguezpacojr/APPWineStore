package com.rodriguezpacojr.winestore;

import android.os.Handler;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
    private android.app.ProgressDialog progressBar;

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
        String URL = "http://"+ setup.getIpAddress() +":"+ setup.getPortNumber() +"/IosAnd/api/route/getroutes/" + setup.getUserName() + "/"+ setup.getToken();
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
        if (response != null) {
            Route objRoute;
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("route");
                for (int i = 0; i < jsonArray.length(); i++) {
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

                                RoutesOnMapActivity.setKeyRoute(routeList.get(position).getNumber());
                                Intent intaboutOf = new Intent(RoutesActivity.this, RoutesOnMapActivity.class);
                                RoutesActivity.this.startActivity(intaboutOf);
                                finish();
                            }
                        });
            } catch (JSONException e) {
                Log.e("Error", e.toString());
            }
        }
        else {
            final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
            dialog.setTitle("Warnning!")
                    .setMessage("Your session has ended!\nYou must Login again!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            Intent intInicio = new Intent(RoutesActivity.this, LoginActivity.class);
                            intInicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intInicio);
                            finish();
                        }
                    });
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_logout:
                progressBar = new android.app.ProgressDialog(this);
                progressBar.setCancelable(false);
                progressBar.setMessage("LogOut...");
                progressBar.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.cancel();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);

                break;
            case R.id.menu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(RoutesActivity.this, R.style.AppTheme_AlertDialog);
                LayoutInflater inflater = RoutesActivity.this.getLayoutInflater();
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