package com.example.lab_1_2_saikiranreddy_c0836167_android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    private EditText editTextId, editTextDesc, editTextName, editTextPrice;
    private TextView tvTextlocation;
    private static final int ADD_REQUEST_CODE = 101;
    private double lat = 0, lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextId = findViewById(R.id.editTextId);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextName = findViewById(R.id.editTexName);
        editTextPrice = findViewById(R.id.editTextPrice);
        tvTextlocation = findViewById(R.id.tvTextlocation);

        tvTextlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMapActivity();
            }
        });
        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();
            }
        });
    }

    private void navigateToMapActivity() {
        Intent intent = new Intent(AddProductActivity.this, MapsActivity.class);
        if (lat != 0 && lng != 0){
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
        }
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    private void saveProduct() {
        final String sId = editTextId.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sName = editTextName.getText().toString().trim();
        final String sPrice = editTextPrice.getText().toString().trim();
        final String sLocation = tvTextlocation.getText().toString().trim();

        if (sId.isEmpty()) {
            editTextId.setError("Product Id required");
            editTextId.requestFocus();
            return;
        }
        if (sDesc.isEmpty()) {
            editTextDesc.setError("Product desc required");
            editTextDesc.requestFocus();
            return;
        }
        if (sName.isEmpty()) {
            editTextName.setError("Product name required");
            editTextName.requestFocus();
            return;
        }
        if (sPrice.isEmpty()) {
            editTextPrice.setError("Product price required");
            editTextPrice.requestFocus();
            return;
        }

        if (sLocation.isEmpty()) {
            tvTextlocation.setError("Location required");
            tvTextlocation.requestFocus();
            return;
        }

        class SaveProduct extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Product product = new Product();
                product.setId(sId);
                product.setDescription(sDesc);
                product.setName(sName);
                product.setPrice(sPrice);
                product.setLatitude(lat);
                product.setLongitude(lng);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(product);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveProduct st = new SaveProduct();
        st.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == ADD_REQUEST_CODE){
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            tvTextlocation.setText(lat + ", " + lng);
        }
    }
}
