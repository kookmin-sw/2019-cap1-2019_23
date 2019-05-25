package com.example.object;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capston.GlideApp;
import com.example.capston.R;
import com.example.capston.ReadFictionActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by gdtbg on 2017-09-16.
 */

public class AutoScrollAdapter extends PagerAdapter implements View.OnClickListener {

    Context context;
    ArrayList<PublishedFiction> data;
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    String fictionImageCoverPath;
    String fictionTitle;
    String authorAccount;


    public AutoScrollAdapter(Context context, ArrayList<PublishedFiction> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.auto_viewpager,null);
        v.setOnClickListener(this);


        fictionTitle =  data.get(position).getFictionTitle();
        authorAccount = data.get(position).getAuthorAccount();

        ImageView image_container = (ImageView) v.findViewById(R.id.image_container);
        fictionImageCoverPath =data.get(position).getFictionImgCoverPath();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(fictionImageCoverPath);

        Glide.with(context)
                .load(storageRef)
                .into(image_container);
        container.addView(v);
        Log.d("순서",fictionImageCoverPath);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onClick(View v) {
       // Toast.makeText(context,imgcoverpath,Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context, ReadFictionActivity.class);
        intent.putExtra("fictionTitle",fictionTitle);
        intent.putExtra("authorAccount",authorAccount);
        context.startActivity(intent);


    }
}
