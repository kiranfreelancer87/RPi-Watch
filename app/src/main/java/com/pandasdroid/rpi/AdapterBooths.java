package com.pandasdroid.rpi;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pandasdroid.rpi.databinding.ItemviewBoothSelectionsBinding;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;

public class AdapterBooths extends RecyclerView.Adapter<BoothsViewHolder> {

    public ArrayList<BoothSelectModel> booth_list;

    public AdapterBooths(Context context) {
        this.booth_list = loadBoothListState(context);
    }

    private ArrayList<BoothSelectModel> loadBoothListState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BoothListState", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("boothList", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<BoothSelectModel>>() {
            }.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>(); // Default empty list if no previous state is found
        }
    }


    @NonNull
    @Override
    public BoothsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BoothsViewHolder(ItemviewBoothSelectionsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BoothsViewHolder holder, int pos) {
        holder.binding.tvBoothNumber.setText(MessageFormat.format(booth_list.get(pos).booth + " ({0})", booth_list.get(pos).is_selected ? "ON" : "OFF"));
        if (booth_list.get(pos).is_selected) {
            holder.binding.cvBooth.setCardBackgroundColor(Color.parseColor("#00897B"));
        } else {
            holder.binding.cvBooth.setCardBackgroundColor(Color.parseColor("#E53935"));
        }

        holder.binding.cvBooth.setOnClickListener(view -> {
            if (!booth_list.get(pos).is_selected) {
                FirebaseMessaging.getInstance().subscribeToTopic("" + (pos + 1)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        booth_list.get(pos).setIs_selected(!booth_list.get(pos).is_selected);
                        saveBoothListState(view.getContext(), booth_list);
                        notifyItemChanged(pos);
                    } else {
                        Toast.makeText(view.getContext(), "Failed!" + task.getResult().toString(), Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                });
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("" + (pos + 1)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        booth_list.get(pos).setIs_selected(!booth_list.get(pos).is_selected);
                        saveBoothListState(view.getContext(), booth_list);
                        notifyItemChanged(pos);
                    } else {
                        Toast.makeText(view.getContext(), "Failed!" + task.getResult().toString(), Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public void saveBoothListState(Context context, ArrayList<BoothSelectModel> boothList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("BoothListState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(boothList);
        editor.putString("boothList", json);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return booth_list.size();
    }
}
