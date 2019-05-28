package com.example.capston;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.object.Chapter;
import com.example.object.ChapterAdapterReader;
import com.example.object.PublishedFiction;
import com.example.object.PublishedFictionAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ReaderBookInfo extends AppCompatActivity {

    private ImageView readerBookInfoFictioncoverImageview;
    private TextView readerBookInfoFictiontitleTextview;
    private TextView readerBookInfoCategoryTextview;

    RecyclerView chapterRecyclerView;
    ChapterAdapterReader chapterAdapterReader;
    private List<Chapter> chapterList;



    // firebase database
    static FirebaseFirestore firestore;
    // 파이어베이스 인증 객체 생성
    static FirebaseAuth mAuth;
    // firebase storage
    static FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_book_info);

        Intent intent = getIntent();

        final String authorAccount = intent.getStringExtra("authorAccount");
        final String fictionTitle = intent.getStringExtra("fictionTitle");
        String fictionCategory = intent.getStringExtra("fictionCategory");

        readerBookInfoFictiontitleTextview = findViewById(R.id.readerbookinfo_fictiontitle_textview);
        readerBookInfoFictiontitleTextview.setText(fictionTitle);
        readerBookInfoCategoryTextview = findViewById(R.id.readerbookinfo_category_textview);
        readerBookInfoCategoryTextview.setText(fictionCategory);

        readerBookInfoFictioncoverImageview = (ImageView) findViewById(R.id.readerbookinfo_fictioncover_imageview);


        //서버 연동
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        // 재사용뷰
        chapterList = new ArrayList<Chapter>();
        chapterRecyclerView = findViewById(R.id.readerbookinfo_recyclerview);

        firestore.collection("user").document(authorAccount)
                .collection("myworkspace").document(fictionTitle)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                            String  fictionImgCoverPath =(String) document.getData().get("fictionImgCoverPath");

                        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(fictionImgCoverPath);
                        GlideApp.with(ReaderBookInfo.this)
                                .load(storageRef)
                                .override(200,200)
                                .into(readerBookInfoFictioncoverImageview);
                    } else {

                    }
                }
            }
        });

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
                            chapterAdapterReader = new ChapterAdapterReader(chapterList,ReaderBookInfo.this);
                            chapterRecyclerView.setAdapter(chapterAdapterReader);

                        } else {

                        }
                    }
                });
    }




}
