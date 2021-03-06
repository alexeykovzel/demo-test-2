package com.alexeykovzel.demo_test_2.view.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexeykovzel.demo_test_2.DatabaseManager;
import com.alexeykovzel.demo_test_2.model.Product;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private MutableLiveData<List<Product>> products;
    private DatabaseManager database;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        products = new MutableLiveData<>();
    }

    public LiveData<List<Product>> getProductList() {
        if (products == null) {
            products = new MutableLiveData<>();
            initProductList();
        }
        return products;
    }

    public void setProductList(List<Product> productList) {
        if (productList != null) {
            products.setValue(productList);
        }
    }

    public void addProduct(Product product) {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        database.addProduct(product);
        products.setValue(productList);
    }

    public void deleteProduct(int position) {
        List<Product> productList = products.getValue();
        if (productList != null) {
            database.deleteProductByUuid(productList.get(position).getUuid());
            productList.remove(position);
        }
        products.setValue(productList);
    }

    public void initProductList() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://fir-test-1cbbc.appspot.com/testData.json");
        database = DatabaseManager.getInstance(getApplication());
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    List<Product> products = MAPPER.readValue(new String(bytes), new TypeReference<List<Product>>() {
                    });
                    for (Product product : products) {
                        database.addProduct(product);
                    }
                    setProductList(database.getAllProducts());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Error while trying to get data from Firebase");
            }
        });
    }

    static {
        MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }
}