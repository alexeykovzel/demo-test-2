package com.alexeykovzel.demo_test_2.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexeykovzel.demo_test_2.R;
import com.alexeykovzel.demo_test_2.model.Product;
import com.alexeykovzel.demo_test_2.view.adapter.ProductAdapter;
import com.alexeykovzel.demo_test_2.view.viewmodel.ProductViewModel;

import java.util.List;

public class ProductFragment extends Fragment implements ProductAdapter.ItemClickListener {
    private static ProductAdapter adapter;
    private ProductViewModel mViewModel;
    private SearchView searchView;
    RecyclerView recyclerView;

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductAdapter(getContext());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.product_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setIconified(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.setProductList(mViewModel.getProductList().getValue());
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        mViewModel.getProductList().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProductList(products);
                adapter.notifyDataSetChanged();
            }
        });
        mViewModel.initProductList();
    }

    @Override
    public void onItemClick(View view, int position) {
        mViewModel.deleteProduct(position);
    }
}