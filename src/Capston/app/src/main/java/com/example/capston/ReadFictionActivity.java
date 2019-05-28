package com.example.capston;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.object.Chapter;
import com.example.object.ChapterAdapterReader;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFictionActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView readfictionFictionCoverImageView;
    TextView readfictionFictionTitleTextView;
    TextView readfictionCategoryTextview;
    ImageView readfictionLikeImageview;
    TextView readfictionLikecountTextView;
    TextView readfictionAuthorTextview;
    ImageView readfictionBookmarkImageview;


    String authorAccount;
    String author;
    String fictionTitle;
    String fictionImgCoverPath;
    String fictionCategory;
    String fictionLikeCount;
    private boolean isUserLike;
    private boolean isUserBookMark;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

    RecyclerView chapterRecyclerView;
    ChapterAdapterReader chapterAdapterReader;
    private List<Chapter> chapterList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_fiction);

        Intent intent = getIntent();
        fictionTitle = intent.getStringExtra("fictionTitle");
        fictionCategory = intent.getStringExtra("fictionCategory");
        authorAccount =intent.getStringExtra("authorAccount");
        author = intent.getStringExtra("author");
        fictionImgCoverPath = intent.getStringExtra("fictionImgCoverPath");

        firestore= FirebaseFirestore.getInstance();

        isUserBookMark = intent.getBooleanExtra("isUserBookMark",true);
        isUserLike=intent.getBooleanExtra("isUserLike",true);
        fictionLikeCount = intent.getStringExtra("fictionLikeCount");

        readfictionFictionCoverImageView = (ImageView) findViewById(R.id.readfiction_fictioncover_imageview);

        readfictionFictionTitleTextView = (TextView) findViewById(R.id.readfiction_fictiontitle_textview);
        readfictionFictionTitleTextView.setText(fictionTitle);

        readfictionCategoryTextview =(TextView) findViewById(R.id.readfiction_category_textview);
        readfictionCategoryTextview.setText("장르:"+fictionCategory);

        readfictionLikeImageview = (ImageView) findViewById(R.id.readfiction_like_imageview);
        readfictionLikeImageview.setOnClickListener(this);

        readfictionLikecountTextView = (TextView) findViewById(R.id.readfiction_likecount_textview);
        readfictionLikecountTextView.setText(fictionLikeCount);

        readfictionAuthorTextview = (TextView) findViewById(R.id.readfiction_author_textview);
        readfictionAuthorTextview.setText("작가:"+author);
        readfictionBookmarkImageview = (ImageView) findViewById(R.id.readfiction_bookmark_imageview);
        readfictionBookmarkImageview.setOnClickListener(this);
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(fictionImgCoverPath);
        Glide.with(this)
                .load(storageRef)
                .into(readfictionFictionCoverImageView);

        // 챕터 재사용뷰
        chapterRecyclerView = findViewById(R.id.readfiction_recyclerview);
        chapterList = new ArrayList<Chapter>();


        if(isUserLike){
            readfictionLikeImageview.setImageResource(R.drawable.heart_bt_on);
           isUserLike= true;
        }else {
            readfictionLikeImageview.setImageResource(R.drawable.heart_bt);
          isUserLike= false;
        }

        if(isUserBookMark){
            readfictionBookmarkImageview.setImageResource(R.drawable.star_bt_on);
           isUserBookMark= true;
        }else {
            readfictionBookmarkImageview.setImageResource(R.drawable.star_bt);
           isUserBookMark= false;
        }
        // 챕터 리사이클러뷰
        firestore.collection("user").document(authorAccount)
                .collection("myworkspace").document(fictionTitle)
                .collection("chapters").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String chapterNumber=(String) document.getData().get("chapterNumber");
                                String chapterTitle =(String) document.getData().get("chapterTitle");

                                Chapter chapter = new Chapter(chapterNumber,chapterTitle,authorAccount,fictionTitle);
                                chapterList.add(chapter);

                            }
                            chapterAdapterReader = new ChapterAdapterReader(chapterList,ReadFictionActivity.this);
                            chapterRecyclerView.setAdapter(chapterAdapterReader);
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final DocumentReference authorAccountRef = firestore.collection("user").document(authorAccount);
        final CollectionReference authorAccountMyworkspaceRef =  authorAccountRef.collection("myworkspace");
        final DocumentReference fictionRef = authorAccountMyworkspaceRef.document(readfictionFictionTitleTextView.getText().toString());

        int id =   v.getId();

      switch (id){
          case R.id.readfiction_bookmark_imageview:
              fictionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if (task.isSuccessful()) {
                          final DocumentSnapshot document = task.getResult();
                          if (document.exists()) {
                              Map<String,Object> bookmark = (HashMap<String, Object>) document.getData().get("bookmark");
                              if(isUserBookMark){
                                  // 이미 눌렀으면 다시 눌르면 사진 변경및 숫자 낮추기.
                                  readfictionBookmarkImageview.setImageResource(R.drawable.star_bt);
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
                                  // 자신이 북마크한 소설 정리.
                                  firestore.collection("user").document(user.getEmail()).collection("mybookmark")
                                          .document(authorAccount+"_"+readfictionFictionTitleTextView.getText().toString()).delete();
                                  // 작가 인기 업데이트
                                  authorAccountRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                          if (task.isSuccessful()) {
                                              DocumentSnapshot document = task.getResult();
                                              if (document.exists()) {
                                                  Long uesrPopularity =(Long) document.getData().get("uesrPopularity");
                                                  authorAccountRef.update("uesrPopularity",uesrPopularity-1);
                                              }
                                          }
                                      }
                                  });

                              }else {
                                  // 새로 눌렀을때
                                  readfictionBookmarkImageview.setImageResource(R.drawable.star_bt_on);
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
                                  // 자신이 북마크한 소설 정리.
                                  Map<String,Object> data = new HashMap<String, Object>();
                                  data.put("authorAccount",authorAccount);
                                  data.put("fictionTitle",readfictionFictionTitleTextView.getText().toString());
                                  firestore.collection("user").document(user.getEmail()).collection("mybookmark")
                                          .document(authorAccount+"_"+readfictionFictionTitleTextView.getText().toString()).set(data);

                                  // 작가 인기 업데이트
                                  authorAccountRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                          if (task.isSuccessful()) {
                                              DocumentSnapshot document = task.getResult();
                                              if (document.exists()) {
                                                  Long uesrPopularity =(Long) document.getData().get("uesrPopularity");
                                                  authorAccountRef.update("uesrPopularity",uesrPopularity+1);
                                              }
                                          }
                                      }
                                  });
                              }
                          }
                      }
                  }
              });
              break;
          case R.id.readfiction_like_imageview:
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
                                  readfictionLikeImageview.setImageResource(R.drawable.heart_bt);
                                  readfictionLikecountTextView.setText(String.valueOf(Integer.parseInt(readfictionLikecountTextView.getText().toString()) - 1));
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
                                  readfictionLikeImageview.setImageResource(R.drawable.heart_bt_on);
                                  readfictionLikecountTextView.setText(String.valueOf(Integer.parseInt(readfictionLikecountTextView.getText().toString()) + 1));
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
                          }
                      }
                  }
              });
              break;
      }

    }
}
