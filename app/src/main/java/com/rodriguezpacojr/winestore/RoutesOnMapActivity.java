package com.rodriguezpacojr.winestore;

import android.os.Handler;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RoutesOnMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,Response.Listener<String>, Response.ErrorListener {

    private GoogleMap mMap;
    LatLng coordinate;
    private Marker[] markers;
    RequestQueue requestQueue;
    public static int keyRoute;
    private android.app.ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_on_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestQueue = Volley.newRequestQueue(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        getPlaces();
    }

    private void getPlaces() {
        Setup setup = new Setup();
        String URL = "http://"+ setup.getIpAddress() +":"+ setup.getPortNumber() +"/IosAnd/api/customer/listcustomersroute/" + keyRoute +"/"+ setup.getToken();
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

    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject objectJSON = new JSONObject(response);
            JSONArray items = objectJSON.getJSONArray("customer");

            markers = new Marker[items.length()];

            for (int i=0; i < items.length(); i++){
                JSONObject place = items.getJSONObject(i);

                coordinate = new LatLng(place.getDouble("latitude"), place.getDouble("longitude"));
                String name = place.getString("name") +  " " + place.getString("lastName");
                int key = place.getInt("keyCustomer");
                markers[i] = mMap.addMarker(
                        new MarkerOptions()
                                .position(coordinate)
                                .title(name)
                                .snippet(key+""));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 10));
        }
        catch (Exception e){

        }
    }

    public static void setKeyRoute(int keyRoute) {
        RoutesOnMapActivity.keyRoute = keyRoute;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(RoutesOnMapActivity.this);
        dialog.setTitle("Make Order")
                .setMessage("Make order to: " + marker.getTitle() + ".\nAre you Sure?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Setup.setKeyCustomer(Integer.parseInt(marker.getSnippet()));
                        Intent intent= new Intent(RoutesOnMapActivity.this, ProductsActivity.class);
                        RoutesOnMapActivity.this.startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(RoutesOnMapActivity.this, R.style.AppTheme_AlertDialog);
                LayoutInflater inflater = RoutesOnMapActivity.this.getLayoutInflater();
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