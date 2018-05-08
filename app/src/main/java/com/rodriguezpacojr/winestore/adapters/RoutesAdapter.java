package com.rodriguezpacojr.winestore.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.models.Route;

import java.util.List;

/**
 * Created by francisco on 4/20/18.
 */

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RoutesHolder> {

    private List<Route> routesList;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class RoutesHolder extends RecyclerView.ViewHolder{
        TextView tvnumberNumber;
        TextView tvdestinityText;
        TextView tvcustomerNumber;

        public RoutesHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvnumberNumber = itemView.findViewById(R.id.tvnumberNumber);
            tvdestinityText = itemView.findViewById(R.id.tvdestinityText);
            tvcustomerNumber = itemView.findViewById(R.id.tvcustomerNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RoutesAdapter(List<Route> List, Context con) {
        routesList = List;
        context = con;
    }

    @Override
    public RoutesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_row, parent, false);
        RoutesHolder routesHolder = new RoutesHolder(view, mListener);
        return routesHolder;
    }

    @Override
    public void onBindViewHolder(final RoutesHolder holder, int position) {
        holder.tvnumberNumber.setText(routesList.get(position).getNumber().toString());
        holder.tvdestinityText.setText(routesList.get(position).getDestination());
        holder.tvcustomerNumber.setText(routesList.get(position).getCustomers().toString());
    }

    @Override
    public int getItemCount() {
        return routesList.size();
    }
}