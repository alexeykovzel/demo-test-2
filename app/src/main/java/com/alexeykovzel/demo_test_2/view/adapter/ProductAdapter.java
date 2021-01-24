package com.alexeykovzel.demo_test_2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeykovzel.demo_test_2.R;
import com.alexeykovzel.demo_test_2.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
    private List<Product> productList;
    private LayoutInflater mInflater;
    private ItemClickListener clickListener;

    public ProductAdapter(Context context) {
        this.productList = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    public void setProductList(List<Product> productList) {
        assert productList != null;
        this.productList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textView1.setText(product.getNames().getEn());
        holder.textView2.setText(String.format("P%s/F%s/C%s/%sKcal",
                product.getProteins(), product.getFats(), product.getCarbohydrates(), product.getCaloriesIn100g()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product product : productList) {
                    if (product.getNames().getEn().toLowerCase().contains(filterPattern)) {
                        filteredList.add(product);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProductAdapter.ItemClickListener mClickListener;
        public TextView textView1;
        public TextView textView2;
        Button button;

        public ProductViewHolder(View itemView, ProductAdapter.ItemClickListener mClickListener) {
            super(itemView);

            this.mClickListener = mClickListener;
            textView1 = itemView.findViewById(R.id.text_view1);
            textView2 = itemView.findViewById(R.id.text_view2);
            button = itemView.findViewById(R.id.button);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}