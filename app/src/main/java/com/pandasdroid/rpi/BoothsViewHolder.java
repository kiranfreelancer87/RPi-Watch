package com.pandasdroid.rpi;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandasdroid.rpi.databinding.ItemviewBoothSelectionsBinding;

public class BoothsViewHolder extends RecyclerView.ViewHolder {
    public ItemviewBoothSelectionsBinding binding;

    public BoothsViewHolder(@NonNull ItemviewBoothSelectionsBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }
}
