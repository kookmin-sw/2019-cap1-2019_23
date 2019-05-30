package com.example.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.capston.GlideImageLoadingService;
import com.example.capston.R;
import com.example.capston.ReadFictionActivity;
import com.example.object.Author;
import com.example.object.AuthorRecommendationAdapter;
import com.example.object.AutoScrollAdapter;
import com.example.object.MainSliderAdapter;
import com.example.object.PublishedFiction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {
    AutoScrollViewPager autoViewPager;
    RecyclerView authorRecyclerView;
    AuthorRecommendationAdapter authorRecommendationAdapter;
    List<Author> authorList;

    Slider slider;
    FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        MainActivity mainActivity = (MainActivity) getActivity();
        final String userEmail = mainActivity.mAuth.getCurrentUser().getEmail();
        //추천 소설 리스트
        //final ArrayList<String> data = new ArrayList<>();
        // 추천작가 리사이클러뷰
        final ArrayList<PublishedFiction> publishedFictionList =  new ArrayList<PublishedFiction>();
        authorList =  new ArrayList<Author>();
        authorRecyclerView = view.findViewById(R.id.fragment_homefragment_authorlist_recyclerView);
        Slider.init(new GlideImageLoadingService(getContext()));
        slider = view.findViewById(R.id.banner_slider1);

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

                                    }
                                    //Collections.sort(publishedFictionList);
                                    //Toast.makeText(getContext(),String.valueOf(publishedFictionList.size()),Toast.LENGTH_LONG).show();
                                    /*
                                    autoViewPager = (AutoScrollViewPager) view.findViewById(R.id.autoViewPager);
                                    AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getContext(), publishedFictionList);
                                    autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
                                    autoViewPager.setInterval(4000); // 페이지 넘어갈 시간 간격 설정
                                    autoViewPager.startAutoScroll(); //Auto Scroll 시작
                                    */
                                    slider.setAdapter(new MainSliderAdapter(publishedFictionList));
                                    slider.setOnSlideClickListener(new OnSlideClickListener() {
                                        @Override
                                        public void onSlideClick(int position) {
                                            Intent intent =  new Intent(getContext(), ReadFictionActivity.class);
                                            intent.putExtra("fictionTitle",publishedFictionList.get(position).getFictionTitle());
                                            intent.putExtra("authorAccount",publishedFictionList.get(position).getAuthorAccount());
                                            intent.putExtra("author",publishedFictionList.get(position).getAuthor());
                                            intent.putExtra("fictionImgCoverPath",publishedFictionList.get(position).getFictionImgCoverPath());
                                            intent.putExtra("fictionCategory",publishedFictionList.get(position).getFictionCategory());
                                            intent.putExtra("isUserLike",publishedFictionList.get(position).isUserLike());
                                            intent.putExtra("isUserBookMark",publishedFictionList.get(position).isSubscribe());
                                            intent.putExtra("fictionLikeCount",publishedFictionList.get(position).getFictionLikeCount());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        });

                    }
                }
            }
        });




        userCollectionRef.orderBy("uesrPopularity").limit(6).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                       String userProfileImgPath = (String) document.getData().get("userProfileImgPath");
                        String userNickName = (String) document.getData().get("userNickName");
                        String authorAccount = (String) document.getData().get("userEmail");
                        Author author = new Author(authorAccount,userNickName,userProfileImgPath);
                        authorList.add(author);
                    }
                    authorRecommendationAdapter = new AuthorRecommendationAdapter(authorList,getContext());
                    authorRecyclerView.setAdapter(authorRecommendationAdapter);
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
