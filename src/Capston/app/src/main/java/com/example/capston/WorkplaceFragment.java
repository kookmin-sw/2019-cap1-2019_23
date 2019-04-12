package com.example.capston;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkplaceFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton floatingActionButton;

    TextView fragment_workspace_nickname_TextView;
    CircleImageView fragment_workspace_user_CircleImageView;
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

        return view;
    }





    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MakeFictionActivity.class);
        startActivity(intent);
    }
}
