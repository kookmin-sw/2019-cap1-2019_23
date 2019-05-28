package com.example.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.capston.GlideApp;
import com.example.capston.MakeFictionActivity;
import com.example.capston.R;
import com.example.object.Fiction;
import com.example.object.FictionAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkplaceFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton floatingActionButton;

    TextView fragment_workspace_nickname_TextView;
    CircleImageView fragment_workspace_user_CircleImageView;
    RecyclerView fictionRecyclerView;
    String TAG = "realtime";
    String userEmail;
    private List<Fiction> fictionList;
    private FictionAdapter recyclerViewAdapter;


    public WorkplaceFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_workplace, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.main_writeButton);
        floatingActionButton.setOnClickListener(this);

        fragment_workspace_user_CircleImageView = (CircleImageView) view.findViewById(R.id.fragment_workspace_profile_ImageView);
        fragment_workspace_nickname_TextView= (TextView) view.findViewById(R.id.fragment_workspace_nickname_TextView);
        //서버 연동(메인엑티비티에서 가져온다.)
        MainActivity mainActivity = (MainActivity) getActivity();
        //현재유저
        FirebaseUser user = mainActivity.mAuth.getCurrentUser();
        userEmail = user.getEmail();
        // 프로필 사진.
        StorageReference storageImageRef =mainActivity.firebaseStorage.getReferenceFromUrl("gs://capston-77d38.appspot.com/").child("images/" + userEmail+ ".png");
        GlideApp.with(this)
                .load(storageImageRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(100,100)
                .into(fragment_workspace_user_CircleImageView);
        final String userNickName = mainActivity.navigationViewHeader_profile_nickname.getText().toString();
        fragment_workspace_nickname_TextView.setText(userNickName);


        /// RecyclerView
        fictionRecyclerView =  view.findViewById(R.id.fragment_workspace_fictionList_recyclerView);
        fictionList = new ArrayList<Fiction>();

        MainActivity.firestore.collection("user").document(userEmail)
                .collection("myworkspace")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        fictionList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("author") != null) {
                                String author = (String) doc.getData().get("author");
                                String fictionTitle = (String) doc.getData().get("fictionTitle");
                                String fictionCategory = (String) doc.getData().get("fictionCategory");
                                String fictionImgCoverPath = (String) doc.getData().get("fictionImgCoverPath");

                                Map<String,Object> fictionLikes = (HashMap<String,Object>) doc.getData().get("likes");


                                String fictionLastChapter = (String) doc.getData().get("fictionLastChater");
                                Date fictionCreationdateDateType  = (Date) doc.getData().get("fictionCreationdate");


                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                                String fictionCreationdateString;
                                //Null Pointer 발생시 일시적으로 현재 시간을 넣는다.
                                try {
                                  fictionCreationdateString = simpleDateFormat.format(fictionCreationdateDateType);
                                }catch (Exception excetion){
                                    Date tempDate = new Date();
                                  fictionCreationdateString = simpleDateFormat.format(tempDate);
                                }
                                Fiction fiction = new Fiction(author, fictionTitle, fictionCategory, fictionCreationdateString,
                                        fictionImgCoverPath, String.valueOf(fictionLikes.size()), fictionLastChapter);
                                fictionList.add(fiction);

                            }
                        }
                        recyclerViewAdapter =  new FictionAdapter(fictionList,getContext());
                        fictionRecyclerView.setAdapter(recyclerViewAdapter);
                    }
                });

        return view;
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MakeFictionActivity.class);
        startActivity(intent);
    }
}
