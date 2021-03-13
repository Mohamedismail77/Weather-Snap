package com.application.weathersnap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.application.weathersnap.data.WeatherSnap;
import com.application.weathersnap.databinding.FragmentSnapsListBinding;
import com.application.weathersnap.paging.SnapsAdapter;
import com.application.weathersnap.paging.SnapsViewModel;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SnapsListFragment extends Fragment {


    public SnapsViewModel snapsViewModel;
    private FragmentSnapsListBinding snapsListBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        snapsListBinding = FragmentSnapsListBinding.inflate(inflater,container,false);

        snapsViewModel = new ViewModelProvider(requireActivity()).get(SnapsViewModel.class);

        //Initial snaps List
        SnapsAdapter snapsAdapter = new SnapsAdapter();
        snapsListBinding.snapsList.setAdapter(snapsAdapter);
        snapsViewModel.getSnapsList().observe(getViewLifecycleOwner(),snapsAdapter::submitList);

        initAction();

        snapsAdapter.setSnapClickListener(weatherSnap -> {
            // Todo Open Image In Image Intent
        });

        return snapsListBinding.getRoot();
    }

    public void initAction() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                WeatherSnap snap = ((SnapsAdapter.SnapViewHolder) viewHolder).getWeatherSnap();
                snapsViewModel.deleteSnap(snap);

                String text = getResources().getString(R.string.undo_deleted, snap.getWeatherConditionDescription());
                Snackbar.make(snapsListBinding.getRoot(), text, Snackbar.LENGTH_LONG)
                        .setAction("Undo", view -> snapsViewModel.saveSnap(snap)).show();
            }
        });

        itemTouchHelper.attachToRecyclerView(snapsListBinding.snapsList);
    }
}
