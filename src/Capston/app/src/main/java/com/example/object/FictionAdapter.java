package com.example.object;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.capston.ChapterListActivity;
import com.example.capston.GlideApp;
import com.example.capston.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.List;

public class FictionAdapter extends RecyclerView.Adapter<FictionAdapter.FictionViewHolder> {

    FirebaseStorage firebaseStorage;

    // Adapter란 Data 관리를 도와주고 list(RecyclerView)의 갱신을 관리하는 역활을한다.
    //리스트
    private List<Fiction> fictionList;
    private static Context context;
    // 생성자.
    public FictionAdapter(List<Fiction> fictionList, Context context) {
        this.fictionList = fictionList;
        this.context = context;
    }
    @NonNull
    @Override
    public FictionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // onCreateViewHolder viewGroup에서 context를 얻어와서 인플레이트 한다
        View list = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fiction_item_recycler_view, viewGroup, false);
        return new FictionViewHolder(list);
    }
    @Override
    public void onBindViewHolder(@NonNull FictionViewHolder fictionViewHolder, int i) {
        // i = position
        // List에서 하나씩 꺼내서 item의 각각의 view에 넣는다.

        //뷰홀더 참조후 넣어준다.각각의 뷰에 할당.
        Fiction fiction = fictionList.get(i);
        //바인딩.
        fictionViewHolder.fictiontitleTextview.setText(fiction.getFictionTitle());
        fictionViewHolder.fictioncategoryTextview.setText(fiction.getFictionCategory());
        fictionViewHolder.fictionLikeCountTextview.setText(String.valueOf(fiction.getFictionLikeCount()));
        fictionViewHolder.fictioncreationdateTextview.setText(fiction.getFictionCreationdate());
        fictionViewHolder.fictionlastchapter.append(fiction.getFictionLastChapter());

        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(fiction.getFictionImgCoverPath());
        GlideApp.with(context)
                .load(storageRef)
                .override(200,200)
                .into(fictionViewHolder.fictioncoverImageview);
        }
    @Override
    public int getItemCount() {
        return fictionList.size();
    }



    //객체를 보여줄 뷰를 보관하는 내부 클래스.
    static class FictionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fictiontitleTextview;
        private TextView fictioncategoryTextview;
        private TextView fictioncreationdateTextview;
        private TextView fictionLikeCountTextview;
        private ImageView fictioncoverImageview;
        private ImageView fictionLikeImageview;
        private ImageView fictionwriteImageview;
        private TextView fictionlastchapter;

        public FictionViewHolder(@NonNull View itemView) {
            super(itemView);
            // item view => onCreateViewHolder 에서 넘겨준 뷰.
            // 이름 , 장르, 생성일, 좋아요,
            fictiontitleTextview =(TextView)itemView.findViewById(R.id.item_fictiontitle_textview);
            fictioncategoryTextview =(TextView)itemView.findViewById(R.id.item_fictioncategory_textview);
            fictioncreationdateTextview =(TextView)itemView.findViewById(R.id.item_fictioncreationdate_textview);
            fictionLikeCountTextview= (TextView)itemView.findViewById(R.id.item_fictionLikeCount_textview);
            fictionlastchapter= (TextView)itemView.findViewById(R.id.item_fictionclastchater_textview);
            // 책표지 및 좋아요 이미지
            fictioncoverImageview= (ImageView)itemView.findViewById(R.id.item_fictioncover_imageview);
            fictionLikeImageview = (ImageView)itemView.findViewById(R.id.item_fictionLike_imageview);


            //
            fictionwriteImageview=(ImageView)itemView.findViewById(R.id.item_fictionwrite_imageView);
            fictionwriteImageview.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
           //getAdaterPostion() ==> 몇번쨰 아이템 항목이 재사용된건지 알수있다.
            int postion =  getAdapterPosition();

           int id = v.getId();
           switch (id){
               case R.id.item_fictionwrite_imageView:
                   Intent intent = new Intent(context, ChapterListActivity.class);
                   intent.putExtra("fictionTitle",fictiontitleTextview.getText().toString());
                   context.startActivity(intent);
                break;

           }

        }
    }
}
