package com.pandasdroid.rpi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pandasdroid.rpi.databinding.ActivitySettingsBinding;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

    String booth;

    private ActivitySettingsBinding binding;
    private AdapterBooths adapterBooths;
    private ArrayList<BoothSelectModel> booth_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        booth = getSharedPreferences("Spref", Context.MODE_PRIVATE).getString("booth", "");

        for (int i = 1; i <= 18; i++) {
            booth_list.add(new BoothSelectModel("Booth " + i, false));
        }

        adapterBooths = new AdapterBooths(this);

        if (adapterBooths.booth_list.size() == 0) {
            adapterBooths.booth_list = booth_list;
            adapterBooths.saveBoothListState(this, booth_list);
        }

        binding.rvBooths.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        binding.rvBooths.setAdapter(adapterBooths);
        binding.rvBooths.setHasFixedSize(true);
        binding.rvBooths.setItemViewCacheSize(booth_list.size());

        /*
        btn.setOnClickListener((view) -> {
            btn.setText("Processing...");
            btn.setEnabled(false);

            if (!booth.equals("")) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(booth).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseMessaging.getInstance().subscribeToTopic(spinner.getSelectedItem().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getSharedPreferences("Spref", Context.MODE_PRIVATE).edit().putString("booth", spinner.getSelectedItem().toString()).apply();
                                    booth = spinner.getSelectedItem().toString();
                                    btn.setText("Success");
                                    booth_list.setText("Selected Booth: " + spinner.getSelectedItem().toString());
                                    btn.setEnabled(true);
                                }
                            });
                        } else {
                            btn.setText("Submit");
                            btn.setEnabled(true);
                            Toast.makeText(SettingsActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                FirebaseMessaging.getInstance().subscribeToTopic(spinner.getSelectedItem().toString()).addOnCompleteListener(task -> {
                    getSharedPreferences("Spref", Context.MODE_PRIVATE).edit().putString("booth", spinner.getSelectedItem().toString()).apply();
                    booth = spinner.getSelectedItem().toString();
                    btn.setText("Success");
                    booth_list.setText("Selected Booth: " + spinner.getSelectedItem().toString());
                    btn.setEnabled(true);
                });
            }
        });
*/
    }
}