package com.pandasdroid.rpi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.pandasdroid.rpi.databinding.ActivityMainBinding;
import com.pandasdroid.rpi.databinding.OrdersLayoutBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);

        for (int i = 0; i < 18; i++) {
            OrdersLayoutBinding ordersLayoutBinding = OrdersLayoutBinding.inflate(getLayoutInflater());
            ordersLayoutBinding.tvBooth.setText("Booth " + (i + 1));
            int finalI = i;
            ordersLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ordersLayoutBinding.tvOrderStatus.getText().toString().equals("Processing")) {
                        ordersLayoutBinding.cvOrderStatus.setCardBackgroundColor(Color.parseColor("#FFB300"));
                        ordersLayoutBinding.tvOrderStatus.setText("Processing");
                        UpdateStatus(String.valueOf(finalI + 1), "green");
                    } else {
                        ordersLayoutBinding.cvOrderStatus.setCardBackgroundColor(Color.parseColor("#43A047"));
                        ordersLayoutBinding.tvOrderStatus.setText("Completed");
                        UpdateStatus(String.valueOf(finalI + 1), "red");
                    }
                }
            });
            binding.llNewOrders.addView(ordersLayoutBinding.getRoot());
        }

        /*UpdateStatus(booth, "green");
        binding.tv.setText("Order Received");
        binding.btn.setText("Complete Order");

        binding.btn.setOnClickListener((view) -> {
            UpdateStatus(booth, "red");
        });*/


        findViewById(R.id.iv_setting).setOnClickListener((view) -> {
            Settings();
        });
    }

    public void UpdateStatus(String booth, String status) {
        Log.wtf("Status", status);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create(new Gson())).build();
        ApiInterface api = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = api.UpdateStatus(status, booth);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) {
                    toast("Something Went wrong!");
                } else {
                    try {
                        String str = response.body().string();
                        Log.wtf("Str", str);
                        toast(str);
                    } catch (Exception e) {
                        toast(e.getMessage());
                        Log.wtf("Str", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.wtf("Str", t.getMessage());
                toast(t.getMessage());
            }
        });
    }

    private void toast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    public void Settings() {
        Log.wtf("Ivsetting", "Activity");
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
