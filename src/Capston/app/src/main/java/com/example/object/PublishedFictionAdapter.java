package com.example.object;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capston.GlideApp;
import com.example.capston.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PublishedFictionAdapter extends RecyclerView.Adapter<PublishedFictionAdapter.PublishedFictionViewHolder> {
    // Adapter란 Data 관리를 도와주고 list(RecyclerView)의 갱신을 관리하는 역활을한다.
    private List<PublishedFiction> publishedFictionList;
    private static Context context;
    FirebaseStorage firebaseStorage;
    // 생성자
    public PublishedFictionAdapter(List<PublishedFiction> publishedFictionList,Context context) {
        this.publishedFictionList = publishedFictionList;
        this.context= context;
    }
    @NonNull
    @Override
    public PublishedFictionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // onCreateViewHolder viewGroup에서 context를 얻어와서 인플레이트 한다
        View list = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.published_fiction_item_recycler_view, viewGroup, false);
        return new PublishedFictionViewHolder(list);
    }

    @Override
    public void onBindViewHolder(@NonNull PublishedFictionViewHolder publishedFictionViewHolder, int i) {
        // i = position
        // List에서 하나씩 꺼내서 item의 각각의 view에 넣는다.
        PublishedFiction publishedFiction = publishedFictionList.get(i);
        // 바인딩.
        publishedFictionViewHolder.publishedFictionAuthorTextView.setText(publishedFiction.getAuthor());
        publishedFictionViewHolder.publishedFictionTitleTextView.setText(publishedFiction.getFictionTitle());
        publishedFictionViewHolder.publishedFictionCategoeryTextView.setText(publishedFiction.getFictionCategory());
        publishedFictionViewHolder.publishedFictioncLastchaterTextView.setText(publishedFiction.getFictionLastChapter());


        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(publishedFiction.getFictionImgCoverPath());
        GlideApp.with(context)
                .load(storageRef)
                .override(200,200)
                .into(PublishedFictionViewHolder.publishedFictioncoverImageview);

    }

    @Override
    public int getItemCount() {
        return publishedFictionList.size();
    }

    static class PublishedFictionViewHolder extends RecyclerView.ViewHolder{
        private TextView publishedFictionAuthorTextView;
        private TextView publishedFictionTitleTextView;
        private TextView publishedFictionCategoeryTextView;
        private TextView publishedFictioncLastchaterTextView;

        private static ImageView publishedFictioncoverImageview;


        public PublishedFictionViewHolder(@NonNull View itemView) {
            super(itemView);
            publishedFictionAuthorTextView = itemView.findViewById(R.id.item_publishedfictionAuthor_textview);
            publishedFictionTitleTextView = itemView.findViewById(R.id.item_publishedfictiontitle_textview);
            publishedFictionCategoeryTextView = itemView.findViewById(R.id.item_publishedfictioncategory_textview);
            publishedFictioncLastchaterTextView = itemView.findViewById(R.id.item_publishedfictionclastchater_textview);
            publishedFictioncoverImageview =itemView.findViewById(R.id.item_publishedfictioncover_imageview);
        }
    }
}
