package com.example.capston;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.object.AutoScrollAdapter;
import com.example.object.PublishedFiction;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    AutoScrollViewPager autoViewPager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        final String userEmail = mainActivity.mAuth.getCurrentUser().getEmail();
        final ArrayList<String> data = new ArrayList<>();
        final ArrayList<PublishedFiction> publishedFictionList =  new ArrayList<PublishedFiction>();


        CollectionReference userCollectionRef = MainActivity.firestore.collection("user");
        userCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference userDocumentRef = document.getReference();
                        CollectionReference myworkspaceRef =userDocumentRef.collection("myworkspace");
                        myworkspaceRef.whereEqualTo("published",true)
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
                                        String FictionImgCoverPath = publishedFiction.getFictionImgCoverPath();
                                        data.add(FictionImgCoverPath);

                                    }

                                    //Collections.sort(publishedFictionList);
                                    //Toast.makeText(getContext(),String.valueOf(publishedFictionList.size()),Toast.LENGTH_LONG).show();

                                    autoViewPager = (AutoScrollViewPager) view.findViewById(R.id.autoViewPager);
                                    AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getContext(), data);
                                    autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
                                    autoViewPager.setInterval(3000); // 페이지 넘어갈 시간 간격 설정
                                    autoViewPager.startAutoScroll(); //Auto Scroll 시작

                                }
                            }
                        });

                    }
                }
            }
        });






        return view;
    }

}
