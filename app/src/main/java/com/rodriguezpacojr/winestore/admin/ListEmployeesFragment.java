package com.rodriguezpacojr.winestore.admin;

import android.content.DialogInterface;
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
import com.rodriguezpacojr.winestore.LoginActivity;
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.adapters.EmployeesListAdapter;
import com.rodriguezpacojr.winestore.models.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListEmployeesFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private RecyclerView rvperson;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    //private FloatingActionButton btnAddItem;

    public static RequestQueue requestQueue;
    List<Person> personList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_list, container, false);

        personList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());

        rvperson = (RecyclerView) v.findViewById(R.id.rvCEPList);
        rvperson.setHasFixedSize(true);

        getEmployees();

        layoutManager = new LinearLayoutManager(getActivity());
        rvperson.setLayoutManager(layoutManager);

        return v;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Error", error.toString());
    }

    @Override
    public void onResponse(String response) {
        Log.d("RESPONSE_e", response);
        if (response != null) {
            Person obj;
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("employee");

                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObjectPerson = jsonArray.getJSONObject(i);

                    obj = new Person();
                    obj.setKeyPerson(jsonObjectPerson.getInt("keyEmployee"));
                    obj.setName(jsonObjectPerson.getString("name"));
                    obj.setLastName(jsonObjectPerson.getString("lastName"));
                    String cadena = jsonObjectPerson.getString("bornDate");
                    obj.setBornDate(cadena.substring(0,10));
                    obj.setRfc(jsonObjectPerson.getString("RFC"));
                    obj.setPhone(jsonObjectPerson.getString("phone"));
                    obj.setEmail(jsonObjectPerson.getString("email"));
                    String cadena2 = jsonObjectPerson.getString("entryDate");
                    obj.setEntryDate(cadena2.substring(0,10));
                    obj.setKeyUser(jsonObjectPerson.getInt("keyUser"));

                    personList.add(obj);
                }
                adapter = new EmployeesListAdapter(personList, getActivity());
                rvperson.setAdapter(adapter);
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

    private void getEmployees(){
        Setup setup = new Setup();
        String URL = "http://" + setup.getIpAddress() + ":" + setup.getPortNumber() + "/IosAnd/api/employee/listemployees/"+setup.getToken();
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