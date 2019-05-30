package com.example.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capston.R;
import com.example.object.BookmarkFictionAdapter;
import com.example.object.MyBookDecoration;
import com.example.object.PublishedFiction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyBookFragment extends Fragment {
    RecyclerView bookmarkRecyclerView;
    BookmarkFictionAdapter bookmarkFictionAdapter;
    private List<PublishedFiction> bookmarkFictionList;
    String userEmail;

    public MyBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_mybookfragment, container, false);
        setHasOptionsMenu(true);
        //서버 연동(메인엑티비티에서 가져온다.)
        MainActivity mainActivity = (MainActivity) getActivity();
        //현재유저
        FirebaseUser user = mainActivity.mAuth.getCurrentUser();
        userEmail = user.getEmail();

        bookmarkRecyclerView = view.findViewById(R.id.fragment_mybookfragment_bookmarkfictionList_recyclerView);
        bookmarkFictionList = new ArrayList<PublishedFiction>();

        final CollectionReference userCollectionRef = MainActivity.firestore.collection("user");
        CollectionReference userMyBookMarkRefuserCollectionRef = userCollectionRef.document(userEmail).collection("mybookmark");

        userMyBookMarkRefuserCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String authorAccount = (String) document.getData().get("authorAccount");
                            String fictionTitle = (String) document.getData().get("fictionTitle");

                            userCollectionRef.document(authorAccount).collection("myworkspace")
                                    .document(fictionTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
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
                                            //Toast.makeText(getContext(),fictionImgCoverPath, Toast.LENGTH_LONG).show();
                                            PublishedFiction bookmarkFiction =new PublishedFiction(authorAccount,author, fictionTitle, fictionCategory, fictionImgCoverPath,
                                                    fictionLastChapter, String.valueOf(fictionLikes.size()), isUserLike, isSubscribe);
                                            bookmarkFictionList.add(bookmarkFiction);
                                            //Toast.makeText(getContext(),String.valueOf(bookmarkFictionList.size()), Toast.LENGTH_LONG).show();
                                            bookmarkFictionAdapter = new BookmarkFictionAdapter(bookmarkFictionList,getContext());
                                            bookmarkRecyclerView.setAdapter(bookmarkFictionAdapter);
                                            bookmarkRecyclerView.addItemDecoration(new MyBookDecoration(6
                                            ));

                                        } else {

                                        }
                                    } else {
                                    }
                                }
                            });
                        }
                    } else {

                    }
                }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.all_fragment_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
