package com.application.weathersnap.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.application.weathersnap.data.WeatherSnap;
import com.application.weathersnap.databinding.WeatherSnapCardBinding;

public class SnapsAdapter extends PagedListAdapter<WeatherSnap, SnapsAdapter.SnapViewHolder> {

    public SnapsAdapter() {
        super(DIFF_CALLBACK);
    }
    private SnapClickListener snapClickListener;

    public interface SnapClickListener {
        void onSnapClicked(WeatherSnap weatherSnap);
    }

    @NonNull
    @Override
    public SnapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WeatherSnapCardBinding weatherSnapCardBinding = WeatherSnapCardBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SnapViewHolder(weatherSnapCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SnapViewHolder holder, int position) {
            holder.bindView(getItem(position));
    }

    public void setSnapClickListener(SnapClickListener snapClickListener) {
        this.snapClickListener = snapClickListener;
    }

    public class SnapViewHolder extends RecyclerView.ViewHolder {

        private WeatherSnapCardBinding weatherSnapCardBinding;
        private WeatherSnap weatherSnap;

        public SnapViewHolder(@NonNull WeatherSnapCardBinding weatherSnapCardBinding) {
            super(weatherSnapCardBinding.getRoot());
            this.weatherSnapCardBinding = weatherSnapCardBinding;

            weatherSnapCardBinding.getRoot().setOnClickListener(v -> {
                if(snapClickListener != null) {
                    snapClickListener.onSnapClicked(getWeatherSnap());
                }
            });
        }

        public void bindView(WeatherSnap weatherSnap) {
            this.weatherSnap = weatherSnap;
            weatherSnapCardBinding.setSnap(weatherSnap);
        }

        public WeatherSnap getWeatherSnap() {
            return weatherSnap;
        }
    }




    private static final DiffUtil.ItemCallback<WeatherSnap> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<WeatherSnap>() {
                @Override
                public boolean areItemsTheSame(@NonNull WeatherSnap oldItem, @NonNull WeatherSnap newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull WeatherSnap oldItem,
                                                  @NonNull WeatherSnap newItem) {
                    return oldItem.getLocation().equals(newItem.getLocation()) &&
                            oldItem.getImageUri().equals(newItem.getImageUri());
                }
            };
}
