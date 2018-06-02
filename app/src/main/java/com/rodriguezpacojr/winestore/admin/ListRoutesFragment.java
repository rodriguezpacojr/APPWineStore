package com.rodriguezpacojr.winestore.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.rodriguezpacojr.winestore.LoginActivity;
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.adapters.RoutesListAdapter;
import com.rodriguezpacojr.winestore.models.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListRoutesFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = "ListRoutesFragment";

    private RecyclerView rvroute;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static RequestQueue requestQueue;
    List<Route> routeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        routeList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());

        rvroute = (RecyclerView) v.findViewById(R.id.rvCEPList);
        rvroute.setHasFixedSize(true);

        getRoutes();

        layoutManager = new LinearLayoutManager(getActivity());
        rvroute.setLayoutManager(layoutManager);
        return v;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Error", error.toString());
    }

    @Override
    public void onResponse(String response) {
        Log.d("RESPONSE_r", response);
        if (response != null) {
            Route objroute;
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("route");

                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObjectroute = jsonArray.getJSONObject(i);

                    objroute = new Route();
                    objroute.setKeyRoute(jsonObjectroute.getInt("keyRoute"));
                    objroute.setKeyEmployee(jsonObjectroute.getInt("keyEmployee"));
                    objroute.setDestination(jsonObjectroute.getString("destination"));
                    String name = jsonObjectroute.getString("employee") +" " +jsonObjectroute.getString("lastName");
                    objroute.setEmployee(name);
                    routeList.add(objroute);
                }
                adapter = new RoutesListAdapter(routeList, getActivity());
                rvroute.setAdapter(adapter);
            }catch (JSONException e){
                Log.e("Error",e.toString());
            }
        }
        else if (response == ""){
            final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
            dialog.setTitle("Warnning!")
                    .setMessage("Your session has ended!\nYou must Login again!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                            Intent intInicio = new Intent(getContext(), LoginActivity.class);
                            intInicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intInicio);
                            getActivity().finish();
                        }
                    });
            dialog.show();
        }
    }

    private void getRoutes(){
        Setup setup = new Setup();
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/route/listroutes/"+setup.getToken();

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