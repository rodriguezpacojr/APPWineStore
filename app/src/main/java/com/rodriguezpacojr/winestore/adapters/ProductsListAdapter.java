package com.rodriguezpacojr.winestore.adapters;

import android.app.AlertDialog;
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

import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.admin.CRUDCustomer;
import com.rodriguezpacojr.winestore.admin.CRUDProduct;
import com.rodriguezpacojr.winestore.models.Product;

import java.util.List;

/**
 * Created by francisco on 4/21/18.
 */

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ProductsHolder> {

    private List<Product> productsList;
    Context context;

    public static String name, category, color, taste;
    public static int key, stock;
    public static boolean flatUpdate = false;
    public static double price, ml;

    public static class ProductsHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductCat;
        TextView tvmlNumber;
        TextView tvProductStock;

        ImageView btnDelete;
        ImageView btnEdit;

        public ProductsHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductCat = itemView.findViewById(R.id.tvProductCat);
            tvmlNumber = itemView.findViewById(R.id.tvmlNumber);
            tvProductStock = itemView.findViewById(R.id.tvProductStock);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public ProductsListAdapter(List<Product> List, Context con) {
        productsList = List;
        context = con;
    }

    @Override
    public ProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_row, parent, false);
        ProductsListAdapter.ProductsHolder productsHolder = new ProductsListAdapter.ProductsHolder(view);
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

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete")
                        .setMessage("You will delete "+product.getName()+".\nAre you Sure?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                    CRUDProduct objP = new CRUDProduct();
                                    objP.deleteProduct(product.getKeyProduct());
                                    Toast.makeText(context, "The Product was deleted", Toast.LENGTH_SHORT).show();
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
                key = product.getKeyProduct();
                name = product.getName();
                ml = product.getMl();
                price = product.getSalesPrice();
                color = product.getColor();
                stock = product.getStock();
                taste = product.getTaste();

                Intent intent = new Intent(context, CRUDProduct.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}