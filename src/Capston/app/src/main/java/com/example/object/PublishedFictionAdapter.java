package com.example.object;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.capston.ChapterListActivity;
import com.example.capston.GlideApp;
import com.example.capston.R;
import com.example.capston.ReaderBookInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.ContextCompat.startActivity;

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
        publishedFictionViewHolder.publishedFictionAuthorTextView.setText("작가:"+publishedFiction.getAuthor());
        publishedFictionViewHolder.publishedFictionTitleTextView.setText(publishedFiction.getFictionTitle());
        publishedFictionViewHolder.publishedFictionCategoeryTextView.setText("장르:"+publishedFiction.getFictionCategory());
        publishedFictionViewHolder.publishedFictioncLastchaterTextView.setText("연재:"+publishedFiction.getFictionLastChapter()+"장");
        publishedFictionViewHolder.publishedFicttionLikeCount.setText(publishedFiction.getFictionLikeCount());

        publishedFictionViewHolder.authorAccount =  publishedFiction.getAuthorAccount();

        if(publishedFiction.isUserLike()){
            publishedFictionViewHolder.publishedfictionLikeImageview.setImageResource(R.drawable.heart_bt_on);
            publishedFictionViewHolder.isUserLike= true;
        }else {
            publishedFictionViewHolder.publishedfictionLikeImageview.setImageResource(R.drawable.heart_bt);
            publishedFictionViewHolder.isUserLike= false;
        }

        if(publishedFiction.isSubscribe()){
            publishedFictionViewHolder.publishedFictionBookMark.setImageResource(R.drawable.star_bt_on);
            publishedFictionViewHolder.isUserBookMark= true;
        }else {
            publishedFictionViewHolder.publishedFictionBookMark.setImageResource(R.drawable.star_bt);
            publishedFictionViewHolder.isUserBookMark= false;
        }

        PublishedFictionViewHolder.publishedFictioncoverImageview.setImageDrawable(null);

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

    static class PublishedFictionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView publishedFictionAuthorTextView;
        private TextView publishedFictionTitleTextView;
        private TextView publishedFictionCategoeryTextView;
        private TextView publishedFictioncLastchaterTextView;
        private TextView publishedFicttionLikeCount;

        private static ImageView publishedFictioncoverImageview;
        private ImageView publishedfictionLikeImageview;
        private ImageView publishedFictionINFO;
        private ImageView publishedFictionBookMark;

        private String authorAccount;
        private boolean isUserLike;
        private boolean isUserBookMark;



        FirebaseFirestore firestore = FirebaseFirestore.getInstance();


        public PublishedFictionViewHolder(@NonNull View itemView) {
            super(itemView);
            publishedFictionAuthorTextView = (TextView) itemView.findViewById(R.id.item_publishedfictionAuthor_textview);
            publishedFictionTitleTextView =  (TextView) itemView.findViewById(R.id.item_publishedfictiontitle_textview);
            publishedFictionCategoeryTextView =  (TextView) itemView.findViewById(R.id.item_publishedfictioncategory_textview);
            publishedFictioncLastchaterTextView =  (TextView) itemView.findViewById(R.id.item_publishedfictionclastchater_textview);
            publishedFicttionLikeCount = (TextView)itemView.findViewById(R.id.item_publishedfictionLikeCount_textview);


            publishedFictioncoverImageview = (ImageView)itemView.findViewById(R.id.item_publishedfictioncover_imageview);

            publishedFictionINFO = (ImageView)  itemView.findViewById(R.id.item_publishedfictiondetail_imageView);
            publishedFictionINFO.setOnClickListener(this);
            publishedFictionBookMark = (ImageView) itemView.findViewById(R.id.item_publishedfictionbookmark_imageView);
            publishedFictionBookMark.setOnClickListener(this);
            publishedfictionLikeImageview = (ImageView) itemView.findViewById(R.id.item_publishedfictionLike_imageview);
            publishedfictionLikeImageview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            firestore=FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            final DocumentReference fictionRef = firestore.collection("user").document(authorAccount).collection("myworkspace").document(publishedFictionTitleTextView.getText().toString());

            //Toast.makeText(context,authorAccount,Toast.LENGTH_LONG).show();
            switch (id){
                case R.id.item_publishedfictiondetail_imageView:
                    // 책정보
                    Intent intent = new Intent(context, ReaderBookInfo.class);
                    intent.putExtra("fictionTitle",publishedFictionTitleTextView.getText().toString());
                    intent.putExtra("fictionCategory",publishedFictionCategoeryTextView.getText().toString());
                    intent.putExtra("authorAccount",authorAccount);
                    context.startActivity(intent);

                    break;
                case R.id.item_publishedfictionbookmark_imageView:
                    // 북마크
                    fictionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                final DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String,Object> bookmark = (HashMap<String, Object>) document.getData().get("bookmark");

                                    if(isUserBookMark){
                                        // 이미 눌렀으면 다시 눌르면 사진 변경및 숫자 낮추기.
                                        publishedFictionBookMark.setImageResource(R.drawable.star_bt);
                                        isUserBookMark=false;
                                        bookmark.remove(user.getEmail());
                                        fictionRef.update("bookmark", bookmark)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                        firestore.collection("user").document(user.getEmail()).collection("mybookmark")
                                                .document(authorAccount+"_"+publishedFictionTitleTextView.getText().toString()).delete();
                                    }else {
                                        // 새로 눌렀을때
                                        publishedFictionBookMark.setImageResource(R.drawable.star_bt_on);
                                        isUserBookMark=true;
                                        bookmark.put(user.getEmail(),true);
                                        fictionRef.update("bookmark", bookmark)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });

                                        Map<String,Object> data = new HashMap<String, Object>();
                                        data.put("authorAccount",authorAccount);
                                        data.put("fictionTitle",publishedFictionTitleTextView.getText().toString());
                                        firestore.collection("user").document(user.getEmail()).collection("mybookmark")
                                                .document(authorAccount+"_"+publishedFictionTitleTextView.getText().toString()).set(data);

                                    }
                                } else {

                                }
                            } else {
                            }
                        }
                    });
                    break;
                case R.id.item_publishedfictionLike_imageview:
                    //likes
                    fictionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String,Object> likse = (HashMap<String, Object>) document.getData().get("likes");

                                    if(isUserLike){
                                        // 이미 눌렀으면 다시 눌르면 사진 변경및 숫자 낮추기.
                                        publishedfictionLikeImageview.setImageResource(R.drawable.heart_bt);
                                        publishedFicttionLikeCount.setText(String.valueOf(Integer.parseInt(publishedFicttionLikeCount.getText().toString()) - 1));
                                        isUserLike=false;
                                        likse.remove(user.getEmail());
                                        fictionRef.update("likes", likse)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                    }else {
                                        // 새로 눌렀을때
                                        publishedfictionLikeImageview.setImageResource(R.drawable.heart_bt_on);
                                        publishedFicttionLikeCount.setText(String.valueOf(Integer.parseInt(publishedFicttionLikeCount.getText().toString()) + 1));
                                        isUserLike=true;

                                        likse.put(user.getEmail(),true);
                                        fictionRef.update("likes", likse)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                    }
                                } else {

                                }
                            } else {

                            }
                        }
                    });
                    break;
            }
        }
    }
}
