package com.example.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.capston.R;
import com.example.object.PublishedFiction;
import com.example.object.PublishedFictionAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    String userEmail;
    RecyclerView publishedFictionRecyclerView;
    PublishedFictionAdapter publishedFictionAdapter;

    private List<PublishedFiction> publishedFictionList;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_searchfragment, container, false);
        setHasOptionsMenu(true);
        //서버 연동(메인엑티비티에서 가져온다.)
        MainActivity mainActivity = (MainActivity) getActivity();
        //현재유저
        FirebaseUser user = mainActivity.mAuth.getCurrentUser();
        userEmail = user.getEmail();
        publishedFictionRecyclerView = view.findViewById(R.id.fragment_searchfragment_publishedfictionList_recyclerView);
        publishedFictionList =  new ArrayList<PublishedFiction>();

        CollectionReference userCollectionRef = MainActivity.firestore.collection("user");
        userCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference  userDocumentRef = document.getReference();
                        CollectionReference myworkspaceRef =userDocumentRef.collection("myworkspace");
                        myworkspaceRef.
                                whereEqualTo("published",true)
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
                                publishedFictionAdapter = new PublishedFictionAdapter(publishedFictionList,getContext());
                                publishedFictionRecyclerView.setAdapter(publishedFictionAdapter);
                            }
                        });
                    }
                }
            }
        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_fragment_toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.item1);
        // 액션 뷰로 설정된 뷰를 추출한다.
        SearchView searchView = (SearchView) searchItem.getActionView();
        // 안내문구 설정
        searchView.setQueryHint("검색어를 입력하세요");
        // 서치뷰의 리스너 설정을 해줘야 fragment에서 검색을 실제로 할수있다.
        searchView.onActionViewExpanded(); //바로 검색 할 수 있도록

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(getContext(),"텍스트 제출",Toast.LENGTH_LONG).show();
                publishedFictionAdapter.setFilter(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}
