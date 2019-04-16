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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ChapterList extends AppCompatActivity implements View.OnClickListener {

    String TAG = "ChapterList";

    ImageView chapterListFictionCover;
    TextView chapterListFictionTitle;
    TextView chapterListFictionCategory;
    TextView chapterListFictionLikeCount;
    TextView chapterListFictionCreationdate;
    Button chapterListWriteButton;
    RecyclerView chapterListRecyclerView;
    String fictionImgCoverPath;
    String fictionLastChater;
    String fictionTitle;

    // firebase database
    static FirebaseFirestore firestore;
    // 파이어베이스 인증 객체 생성
    static FirebaseAuth mAuth;
    // firebase storage
    static FirebaseStorage firebaseStorage;

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
                        GlideApp.with(ChapterList.this)
                                .load(storageRef)
                                .override(100,100)
                                .into(chapterListFictionCover);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }


    // 버튼 리스너
    @Override
    public void onClick(View v) {
        int id  = v.getId();
        switch (id){
            case R.id.chapterlist_chapterwrite_buttion:
                Intent intent = new Intent(ChapterList.this,MakeChapterActivity.class);
                intent.putExtra("fictionTitle",fictionTitle);
                startActivity(intent);
                break;



        }
    }
}
