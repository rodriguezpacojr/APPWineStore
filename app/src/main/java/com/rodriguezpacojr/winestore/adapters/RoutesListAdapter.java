package com.rodriguezpacojr.winestore.adapters;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rodriguezpacojr.winestore.HomeAdminActivity;
import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.admin.CRUDRoute;
import com.rodriguezpacojr.winestore.admin.ListRoutesFragment;
import com.rodriguezpacojr.winestore.models.Route;

import java.util.List;

/**
 * Created by francisco on 4/21/18.
 */

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.routesHolder> {

    private List<Route> routesList;
    Context context;

    public static String name, employee;
    public static int key, keyemployee, customers;
    public static boolean flatUpdate = false;

    public static class routesHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvnameRoute;
        TextView tvnameEmployee;

        ImageView btnDelete;
        ImageView btnEdit;

        public routesHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvnameRoute = itemView.findViewById(R.id.tvnameRoute);
            tvnameEmployee = itemView.findViewById(R.id.tvnameEmployee);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public RoutesListAdapter(List<Route> List, Context con) {
        routesList = List;
        context = con;
    }

    @Override
    public routesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_row, parent, false);
        RoutesListAdapter.routesHolder routesHolder = new RoutesListAdapter.routesHolder(view);
        return routesHolder;
    }

    @Override
    public void onBindViewHolder(routesHolder holder, int position) {
        final Route route = routesList.get(position);

        holder.tvnameRoute.setText(route.getDestination());
        holder.tvnameEmployee.setText(route.getEmployee());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete")
                        .setMessage("You will delete "+route.getDestination()+".\nAre you Sure?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    CRUDRoute objR = new CRUDRoute();
                                    objR.deleteRoute(route.getKeyRoute());
                                    Toast.makeText(context, "The Route was deleted", Toast.LENGTH_SHORT).show();


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

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flatUpdate = true;
                key = route.getKeyRoute();
                name = route.getDestination();
                keyemployee = route.getKeyEmployee();

                Intent intent = new Intent(context, CRUDRoute.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routesList.size();
    }
}