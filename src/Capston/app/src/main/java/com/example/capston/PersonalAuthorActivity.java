package com.example.capston;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.fragment.MainActivity;
import com.example.object.PublishedFiction;
import com.example.object.PublishedFictionAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalAuthorActivity extends AppCompatActivity {




    TextView personalAuthorNicknameTextView;
    CircleImageView personalAuthorProfileImageView;



    String authorAccount;
    String authorNickName;
    String userProfileImgPath;
    String userEmail;
    RecyclerView publishedFictionRecyclerView;
    PublishedFictionAdapter publishedFictionAdapter;
    private List<PublishedFiction> publishedFictionList;

    FirebaseStorage firebaseStorage;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_author);
        Intent intent = getIntent();
        authorAccount = intent.getStringExtra("authorAccount");
        authorNickName = intent.getStringExtra("authorNickName");
        userProfileImgPath = intent.getStringExtra("userProfileImgPath");


        personalAuthorNicknameTextView = (TextView)findViewById(R.id.personal_author_nickname_TextView);
        personalAuthorNicknameTextView.setText(authorNickName);
        personalAuthorProfileImageView = (CircleImageView) findViewById(R.id.personal_author_profile_ImageView);
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl(userProfileImgPath);
        GlideApp.with(this)
                .load(storageRef)
                .error(R.mipmap.ic_launcher)
                .into(personalAuthorProfileImageView);

        /// 추천 작가 소설 목록 recyclerView
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userEmail = user.getEmail();
        publishedFictionRecyclerView = findViewById(R.id.personal_author_fictionList_recyclerView);
        publishedFictionList =  new ArrayList<PublishedFiction>();

        CollectionReference userCollectionRef = firestore.collection("user");
        userCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference userDocumentRef = document.getReference();
                        CollectionReference myworkspaceRef =userDocumentRef.collection("myworkspace");
                        myworkspaceRef
                                .whereEqualTo("published",true)
                                .whereEqualTo("userEmail",authorAccount)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String authorAccount = (String) document.getData().get("userEmail");
                                        String author = (String) document.getData().get("author");
                                        String fictionTitle = (String) document.getData().get("fictionTitle");
                                        String fictionCategory = (String) document.getData().get("fictionCategory");
                                        String fictionLastChapter = (String) document.getData().get("fictionLastChater");
                                        String fictionImgCoverPath = (String) document.getData().get("fictionImgCoverPath");
                                        Map<String,Object> fictionLikes = (HashMap<String,Object>) document.getData().get("likes");
                                        Map<String,Object> fictionSubscribe = (HashMap<String,Object>) document.getData().get("bookmark");
                                        boolean isUserLike= false;
                                        boolean isSubscribe = false;

                                        if(fictionLikes.containsKey(userEmail)){
                                            isUserLike = true;
                                        }else{
                                            isUserLike = false;
                                        }

                                        if(fictionSubscribe.containsKey(userEmail)){
                                            isSubscribe = true;
                                        }else{
                                            isSubscribe = false;
                                        }
                                        PublishedFiction publishedFiction = new PublishedFiction(authorAccount,author, fictionTitle, fictionCategory, fictionImgCoverPath,
                                                fictionLastChapter, String.valueOf(fictionLikes.size()), isUserLike, isSubscribe);

                                        publishedFictionList.add(publishedFiction);
                                    }

                                }else{
                                }
                                publishedFictionAdapter = new PublishedFictionAdapter(publishedFictionList,PersonalAuthorActivity.this);
                                publishedFictionRecyclerView.setAdapter(publishedFictionAdapter);
                            }
                        });
                    }
                }
            }
        });

    }
}
