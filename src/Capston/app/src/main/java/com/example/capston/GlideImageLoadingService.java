package com.example.capston;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ss.com.bannerslider.ImageLoadingService;

public class GlideImageLoadingService implements ImageLoadingService {
    public Context context;
    FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();


    public GlideImageLoadingService(Context context) {
        this.context = context;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
       StorageReference storageRef = firebaseStorage.getReferenceFromUrl(url);
        GlideApp.with(context).load(storageRef).into(imageView);
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        GlideApp.with(context).load(resource).into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(url);
        GlideApp.with(context).load(storageRef).placeholder(placeHolder).error(errorDrawable).into(imageView);

    }
}
