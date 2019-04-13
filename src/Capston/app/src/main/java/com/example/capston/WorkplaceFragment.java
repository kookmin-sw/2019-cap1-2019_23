package com.example.capston;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.object.Fiction;
import com.example.object.FictionAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<Fiction> fictionList;
    private FictionAdapter adapter;

    public WorkplaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_workplace, container, false);
        MainActivity mainActivity =(MainActivity)getActivity();
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.main_writeButton);
        floatingActionButton.setOnClickListener(this);


        fragment_workspace_user_CircleImageView = (CircleImageView) view.findViewById(R.id.fragment_workspace_profile_ImageView);
        fragment_workspace_user_CircleImageView.setImageBitmap(mainActivity.bitmap);

        fragment_workspace_nickname_TextView= (TextView) view.findViewById(R.id.fragment_workspace_nickname_TextView);
        String userNicName = mainActivity.navigationViewHeader_profile_nickname.getText().toString();
        fragment_workspace_nickname_TextView.setText(userNicName);

        //RecyclerView 설정.
        fictionRecyclerView =(RecyclerView) view.findViewById(R.id.fragment_workspace_fictionList_recyclerView);
        fictionList = new ArrayList<Fiction>();
        settingRecyclerView();

        return view;
    }


    private void settingRecyclerView(){
        FirebaseUser user = MainActivity.mAuth.getCurrentUser();

        MainActivity.firestore.collection("user").document(user.getEmail())
                .collection("myworkspace").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange qds: queryDocumentSnapshots.getDocumentChanges()){

                    switch (qds.getType()){

                        case ADDED:
                            String author = (String)qds.getDocument().getData().get("author");
                            String fictionTitle = (String)qds.getDocument().getData().get("fictionTitle");
                            String fictionCategory = (String)qds.getDocument().getData().get("fictionCategory");
                            Date date =(Date) qds.getDocument().getData().get("fictionCreationdate");

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                            String fictionCreationdate = "2019-04-15";

                            String fictionImgCoverPath = (String)qds.getDocument().getData().get("fictionImgCoverPath");
                            int fictionLikeCount = Integer.parseInt((String)qds.getDocument().getData().get("fictionLikeCount"));

                            Fiction data = new Fiction(author,fictionTitle,fictionCategory,fictionCreationdate,fictionImgCoverPath,fictionLikeCount);
                            fictionList.add(data);
                            break;

                    }



                }
                adapter = new FictionAdapter(fictionList);
                fictionRecyclerView.setAdapter(adapter);
            }
        });

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MakeFictionActivity.class);
        startActivity(intent);
    }
}
