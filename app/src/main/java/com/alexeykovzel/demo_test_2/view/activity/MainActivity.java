package com.alexeykovzel.demo_test_2.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alexeykovzel.demo_test_2.R;
import com.alexeykovzel.demo_test_2.view.fragment.ProductFragment;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProductFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

