package com.example.object;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.capston.GlideApp;
import com.example.capston.PersonalAuthorActivity;
import com.example.capston.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorRecommendationAdapter extends RecyclerView.Adapter<AuthorRecommendationAdapter.AuthorViewHolder> {

    private List<Author> authorList;
    private static Context context;
    FirebaseStorage firebaseStorage;

    public AuthorRecommendationAdapter(List<Author> authorList, Context context) {
        this.authorList = authorList;
        this.context = context;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View list = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.author_item_recycler_view, viewGroup, false);
        return new AuthorRecommendationAdapter.AuthorViewHolder(list);
    }
    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder authorViewHolder, int i) {
        // i = position
        // List에서 하나씩 꺼내서 item의 각각의 view에 넣는다.
        Author author = authorList.get(i);

        // 바인딩
        authorViewHolder.userProfileImgPath = author.getUserProfileImgPath();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(author.getUserProfileImgPath());
        GlideApp.with(context)
                .load(storageRef)
                .error(R.mipmap.ic_launcher)
                .into(authorViewHolder.userProfileCircleImageView);


        authorViewHolder.userNickNameTextView.setText(author.getAuthorNickName());
        authorViewHolder.authorAccount = author.getAuthor();

    }
    @Override
    public int getItemCount() {
        return authorList.size();
    }





    static  class AuthorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView  userProfileCircleImageView;
        TextView userNickNameTextView;
        String userProfileImgPath;
        String authorAccount;


        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            userNickNameTextView = itemView.findViewById(R.id.item_authornickname_textview);
            userProfileCircleImageView = itemView.findViewById(R.id.item_authorprofileimg_imageview);
            userProfileCircleImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PersonalAuthorActivity.class);
            intent.putExtra("authorAccount",authorAccount);
            intent.putExtra("authorNickName",userNickNameTextView.getText().toString());
            intent.putExtra("userProfileImgPath",userProfileImgPath);
            context.startActivity(intent);
        }
    }
}
