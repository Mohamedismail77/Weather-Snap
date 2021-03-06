package com.application.weathersnap;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.application.weathersnap.data.WeatherSnap;
import com.application.weathersnap.databinding.FragmentSnapsListBinding;
import com.application.weathersnap.paging.SnapsAdapter;
import com.application.weathersnap.paging.SnapsViewModel;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SnapsListFragment extends Fragment {


    private static final int CAMERA_PERMISSION_REQUEST = 254;
    public SnapsViewModel snapsViewModel;
    private FragmentSnapsListBinding snapsListBinding;
    private SnapsAdapter snapsAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        snapsListBinding = FragmentSnapsListBinding.inflate(inflater,container,false);
        snapsListBinding.setLifecycleOwner(this);
        snapsViewModel = new ViewModelProvider(requireActivity()).get(SnapsViewModel.class);

        //Initial snaps List
        snapsAdapter = new SnapsAdapter();
        snapsListBinding.snapsList.setAdapter(snapsAdapter);
        snapsViewModel.getSnapsList().observe(getViewLifecycleOwner(),this::setSnapListData);

        initAction();

        snapsAdapter.setSnapClickListener(weatherSnap -> {
            File image = new File(weatherSnap.getImageUri());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            ShareDialog.show(this, content);
        });

        snapsListBinding.btnTakeSnap.setOnClickListener(btn->{
            startCameraActivity();
        });

        snapsListBinding.floatingTakeSnap.setOnClickListener(v -> {
            startCameraActivity();
        });

        return snapsListBinding.getRoot();
    }

    private void setSnapListData(PagedList<WeatherSnap> list) {
        snapsAdapter.submitList(list);
        snapsListBinding.emptyMessage.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
        snapsListBinding.btnTakeSnap.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
    }

    private void startCameraActivity() {
        checkCameraPermission();
    }

    private void initAction() {
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

    private void checkCameraPermission(){
        if(isPermissionsGranted()) {
            startActivity(new Intent(getActivity(),CameraActivity.class));
        } else if (isNeverAskAgainChecked()) {
            showPermissionMessage();
        } else {
            askForPermission();
        }
    }


    private boolean isPermissionsGranted() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isNeverAskAgainChecked() {
        return !(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA) &&
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private void showPermissionMessage() {
        Toast.makeText(getContext(),"Please Give Camera and storage permission",Toast.LENGTH_LONG).show();
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                CAMERA_PERMISSION_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (isPermissionsGranted()) {
                startActivity(new Intent(getActivity(),CameraActivity.class));
            } else {
                showPermissionMessage();
            }
        }
    }


}
