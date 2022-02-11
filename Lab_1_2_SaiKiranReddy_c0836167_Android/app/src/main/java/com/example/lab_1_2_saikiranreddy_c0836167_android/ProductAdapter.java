package com.example.lab_1_2_saikiranreddy_c0836167_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductsViewHolder> {

    private Context mCtx;
    private List<Product> productList;

    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_products, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {
        Product t = productList.get(position);
        holder.textViewName.setText(t.getName());
        holder.textViewId.setText(t.getId());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView textViewId, textViewName;

        public ProductsViewHolder(View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Product product = productList.get(getAdapterPosition());

            Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
            intent.putExtra("product", product);

            mCtx.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            Product product = productList.get(getAdapterPosition());

            Intent intent = new Intent(mCtx, UpdateProductActivity.class);
            intent.putExtra("product", product);

            mCtx.startActivity(intent);
            return true;
        }
    }
}