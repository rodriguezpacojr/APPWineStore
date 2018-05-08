package com.rodriguezpacojr.winestore.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.models.Product;

import java.util.List;

/**
 * Created by francisco on 4/21/18.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder> {

    private List<Product> productsList;
    Context context;

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

    public ProductsAdapter(List<Product> List, Context con) {
        productsList = List;
        context = con;
    }

    @Override
    public ProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_row, parent, false);
        ProductsAdapter.ProductsHolder productsHolder = new ProductsAdapter.ProductsHolder(view);
        return productsHolder;
    }

    @Override
    public void onBindViewHolder(ProductsHolder holder, int position) {
        final Product product = productsList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText("$"+product.getSalesPrice());
        holder.tvProductCat.setText(product.getTypeProduct());
        holder.tvmlNumber.setText(product.getMl()+" lts");
        holder.tvProductStock.setText(product.getStock().toString());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}