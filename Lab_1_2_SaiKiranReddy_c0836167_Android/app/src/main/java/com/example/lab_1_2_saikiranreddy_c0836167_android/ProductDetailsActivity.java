package com.example.lab_1_2_saikiranreddy_c0836167_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView textViewId, textViewName, textViewDescription, textViewPrice, textViewLocation;
    private Button btnViewAll;
    private Product product;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        prefs = this.getSharedPreferences(
                "com.example.lab_1_2_saikiranreddy_c0836167_android", Context.MODE_PRIVATE);

        textViewId = findViewById(R.id.textViewId);
        textViewName = findViewById(R.id.textViewName);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewLocation = findViewById(R.id.textViewLocation);
        btnViewAll = findViewById(R.id.btnViewAll);

        if (getIntent().getExtras() != null && getIntent().hasExtra("product")){
            product = (Product) getIntent().getSerializableExtra("product");
            btnViewAll.setVisibility(View.GONE);
            loadProduct(product);
        } else {
            checkDBProducts();
        }

        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMainActivity();
            }
        });
    }

    private void checkDBProducts() {
        if (!isDataAvailable()) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    addDefaultProducts();
                }
            });
        } else {
            LoadProduct st = new LoadProduct();
            st.execute();
        }
    }

    private void addDefaultProducts() {

        class AddTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                for (int i = 0; i < DefaultProductData.myList.length; i++) {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .taskDao()
                            .insert(DefaultProductData.myList[i]);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setDataAvailable(true);
                LoadProduct st = new LoadProduct();
                st.execute();
            }
        }

        AddTask st = new AddTask();
        st.execute();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadProduct(Product product) {
        textViewId.setText(product.getId());
        textViewDescription.setText(product.getDescription());
        textViewName.setText(product.getName());
        textViewPrice.setText(product.getPrice());
        textViewLocation.setText(product.getLatitude() + ", " + product.getLongitude());
    }

    class LoadProduct extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            initData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadProduct(product);
        }
    }

    private void initData() {
        if (DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .taskDao().getCount() > 0) {
            product = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .taskDao().getAll().get(0);
            btnViewAll.setVisibility(View.VISIBLE);
        } else {
            navigateToMainActivity();
            btnViewAll.setVisibility(View.GONE);
        }
    }

    private boolean isDataAvailable() {
        return prefs.getBoolean("is_first_time_launch", false);
    }

    private void setDataAvailable(boolean b) {
        prefs.edit().putBoolean("is_first_time_launch", b).apply();
    }
}
