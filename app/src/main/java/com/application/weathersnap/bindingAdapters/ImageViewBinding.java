package com.application.weathersnap.bindingAdapters;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageViewBinding {

    @BindingAdapter({"image_url"})
    public static void setImageUrl(ImageView imageView,String uri) {
        Glide.with(imageView.getContext().getApplicationContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


}
