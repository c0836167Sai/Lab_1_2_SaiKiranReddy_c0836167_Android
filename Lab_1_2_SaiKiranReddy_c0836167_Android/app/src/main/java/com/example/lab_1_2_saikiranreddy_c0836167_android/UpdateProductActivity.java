package com.example.lab_1_2_saikiranreddy_c0836167_android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText editTextId, editTextDesc, editTextName, editTextPrice;
    private TextView tvTextlocation;
    private static final int UPDATE_REQUEST_CODE = 102;
    private double lat = 0, lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);


        editTextId = findViewById(R.id.editTextId);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextName = findViewById(R.id.editTexName);
        editTextPrice = findViewById(R.id.editTextPrice);
        tvTextlocation = findViewById(R.id.tvTextlocation);

        final Product product = (Product) getIntent().getSerializableExtra("product");

        loadTask(product);

        tvTextlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMapsActivity();
            }
        });

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                updateTask(product);
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(product);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });
    }

    private void navigateToMapsActivity() {
        Intent intent = new Intent(UpdateProductActivity.this, MapsActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        startActivityForResult(intent, UPDATE_REQUEST_CODE);
    }

    private void loadTask(Product product) {
        editTextId.setText(product.getId());
        editTextDesc.setText(product.getDescription());
        editTextName.setText(product.getName());
        editTextPrice.setText(product.getPrice());
        lat = product.getLatitude();
        lng = product.getLongitude();
        tvTextlocation.setText(lat + ", " + lng);
    }

    private void updateTask(final Product product) {
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

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                product.setId(sId);
                product.setDescription(sDesc);
                product.setName(sName);
                product.setPrice(sPrice);
                product.setLatitude(lat);
                product.setLongitude(lng);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(product);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateProductActivity.this, MainActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    private void deleteTask(final Product product) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(product);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateProductActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == UPDATE_REQUEST_CODE){
            lat = data.getExtras().getDouble("lat");
            lng = data.getExtras().getDouble("lng");
            tvTextlocation.setText(lat + ", " + lng);
        }
    }
}
