package com.example.capston;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.object.Chapter;
import com.example.object.ChapterAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ChapterListActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "ChapterListActivity";

    ImageView chapterListFictionCover;
    TextView chapterListFictionTitle;
    TextView chapterListFictionCategory;
    TextView chapterListFictionLikeCount;
    TextView chapterListFictionCreationdate;
    Button chapterListWriteButton;
    Button fictionpublishButtion;
    String fictionImgCoverPath;
    String fictionLastChater;
    public String fictionTitle;

    // firebase database
    static FirebaseFirestore firestore;
    // 파이어베이스 인증 객체 생성
    static FirebaseAuth mAuth;
    // firebase storage
    static FirebaseStorage firebaseStorage;


    private List<Chapter> chapterList;
    RecyclerView chapterListRecyclerView;
    private ChapterAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        // FictionViewHolder에서 보낸 소설 제목을 받는다.
        Intent intent =getIntent();
        fictionTitle=intent.getStringExtra("fictionTitle");
        //뷰 연결
        chapterListFictionCover = (ImageView) findViewById(R.id.chapterlist_fictioncover_imageview);
        chapterListFictionTitle = (TextView) findViewById(R.id.chapterlist_fictiontitle_textview);
        chapterListFictionCategory = (TextView) findViewById(R.id.chapterlist_category_textview);
        chapterListFictionLikeCount = (TextView) findViewById(R.id.chapterlist_likecount_textview);
        chapterListFictionCreationdate = (TextView) findViewById(R.id.chapterlist_fictioncreationdate_textview);
        chapterListWriteButton = (Button) findViewById(R.id.chapterlist_chapterwrite_buttion);
        chapterListWriteButton.setOnClickListener(this);
        fictionpublishButtion = (Button) findViewById(R.id.chapterlist_fictionpublish_buttion);
        // firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userEmail = user.getEmail();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();
        // 제목에 맞는 소설 firestore 연동
        firestore.collection("user").document(userEmail)
                .collection("myworkspace").document(fictionTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        chapterListFictionTitle.setText(fictionTitle);
                        chapterListFictionCategory.setText((String)document.getData().get("fictionCategory"));
                        chapterListFictionLikeCount.setText((String)document.getData().get("fictionLikeCount"));
                        fictionImgCoverPath=(String)document.getData().get("fictionImgCoverPath");
                        fictionLastChater= (String)document.getData().get("fictionLastChater");

                        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(fictionImgCoverPath);
                        GlideApp.with(ChapterListActivity.this)
                                .load(storageRef)
                                .override(200,200)
                                .into(chapterListFictionCover);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        ///RecyclerView
        chapterListRecyclerView = findViewById(R.id.chapterlist_recyclerview);
        chapterList = new ArrayList<Chapter>();

        firestore.collection("user").document(userEmail)
                    .collection("myworkspace").document(fictionTitle)
                    .collection("chapters")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                chapterList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("chapterNumber") != null) {
                        String chapterNumber = (String) doc.getData().get("chapterNumber");
                        String chapterTitle = (String) doc.getData().get("chapterTitle");
                        Chapter chapter = new Chapter(chapterNumber,chapterTitle);
                        chapterList.add(chapter);
                    }
                }
                recyclerViewAdapter =  new ChapterAdapter(chapterList, ChapterListActivity.this);
                chapterListRecyclerView.setAdapter(recyclerViewAdapter);
            }
        });

    }

    // 버튼 리스너
    @Override
    public void onClick(View v) {
        int id  = v.getId();
        switch (id){
            case R.id.chapterlist_chapterwrite_buttion:
                Intent intent = new Intent(ChapterListActivity.this,MakeChapterActivity.class);
                intent.putExtra("fictionTitle",fictionTitle);
                startActivity(intent);
                break;
        }
    }
}
