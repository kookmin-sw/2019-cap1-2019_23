package com.example.object;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.capston.GlideApp;
import com.example.capston.R;
import com.example.capston.ReadFictionActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.RecyclerView.*;

public class BookmarkFictionAdapter extends RecyclerView.Adapter<BookmarkFictionAdapter.BookmarkFictionViewHolder>{

    private List<PublishedFiction> bookmarkFictionList;
    private static Context context;
    FirebaseStorage firebaseStorage;


    public BookmarkFictionAdapter(List<PublishedFiction> publishedFictionList, Context context) {
        this.bookmarkFictionList = publishedFictionList;
        this.context = context;
    }
    @NonNull
    @Override
    public BookmarkFictionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // onCreateViewHolder viewGroup에서 context를 얻어와서 인플레이트 한다
        View list = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bookmarked_fiction_item_recycler_view, viewGroup, false);
        return new BookmarkFictionViewHolder(list);



    }
    @Override
    public void onBindViewHolder(@NonNull BookmarkFictionViewHolder bookmarkFictionViewHolder, int i) {
        // i = position
        // List에서 하나씩 꺼내서 item의 각각의 view에 넣는다.
        PublishedFiction bookmarkFiction = bookmarkFictionList.get(i);
        // 바인딩.
        firebaseStorage = FirebaseStorage.getInstance();
       // Toast.makeText(context,bookmarkFiction.getFictionImgCoverPath(), Toast.LENGTH_LONG).show();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(bookmarkFiction.getFictionImgCoverPath());
        GlideApp.with(context)
                .load(storageRef)
                .override(200,200)
                .into(BookmarkFictionViewHolder.BookmarkFictionImageView);



        bookmarkFictionViewHolder.author = bookmarkFiction.getAuthor();
        bookmarkFictionViewHolder.authorAccount = bookmarkFiction.getAuthorAccount();
        bookmarkFictionViewHolder.fictionTitle= bookmarkFiction.getFictionTitle();
        bookmarkFictionViewHolder.fictionCategory = bookmarkFiction.getFictionCategory();
        bookmarkFictionViewHolder.fictionImgCoverPath = bookmarkFiction.getFictionImgCoverPath();
        bookmarkFictionViewHolder.fictionLikeCount = bookmarkFiction.getFictionLikeCount();
        bookmarkFictionViewHolder.isUserLike = bookmarkFiction.isUserLike();
        bookmarkFictionViewHolder.isSubscribe = bookmarkFiction.isSubscribe();



    }
    @Override
    public int getItemCount() {
        return bookmarkFictionList.size();
    }

    // 내부 뷰홀더
    static class BookmarkFictionViewHolder extends ViewHolder implements OnClickListener{

        private static ImageView BookmarkFictionImageView;
        String author;
        String authorAccount;
        String fictionTitle;
        String fictionCategory;
        String fictionImgCoverPath;
        String fictionLikeCount;
        boolean isUserLike;
        boolean isSubscribe ;

        public BookmarkFictionViewHolder(@NonNull View itemView) {
            super(itemView);
            BookmarkFictionImageView =  itemView.findViewById(R.id.item_bookmarkedfictioncover_imageview);
            BookmarkFictionImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent =  new Intent(context, ReadFictionActivity.class);
            intent.putExtra("fictionTitle",fictionTitle);
            intent.putExtra("authorAccount",authorAccount);
            intent.putExtra("author",author);
            intent.putExtra("fictionImgCoverPath",fictionImgCoverPath);
            intent.putExtra("fictionCategory",fictionCategory);
            intent.putExtra("isUserLike",isUserLike);
            intent.putExtra("isUserBookMark",isSubscribe);
            intent.putExtra("fictionLikeCount",fictionLikeCount);
            context.startActivity(intent);


        }
    }
}
