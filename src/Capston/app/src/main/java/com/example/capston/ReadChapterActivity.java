package com.example.capston;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Audio.AudioService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class ReadChapterActivity extends AppCompatActivity implements View.OnClickListener {


    TextView chapterTitleTextView;
    TextView chapterContentsTextView;
    Button chapterReadButton;
    Button chapterReadStopButton;

    RadioGroup voiceRadioGroup;
    RadioButton moonRadioButton, sonRadioButton;

    String authorAccount;
    String fictionTitle;
    String chapterNumber;
    String chapterTitle;
    String voicePath;

    // firebase database
    static FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        authorAccount = intent.getStringExtra("authorAccount");
        fictionTitle = intent.getStringExtra("fictionTitle");
        chapterNumber = intent.getStringExtra("chapterNumber").substring(1, 2);
        chapterTitle = intent.getStringExtra("chapterTitle");
        //Toast.makeText(this, authorAccount+fictionTitle+"  "+chapterNumber+chapterTitle, Toast.LENGTH_SHORT).show();


        chapterTitleTextView = (TextView) findViewById(R.id.readchapteractivity_chaptertitle_textview);
        chapterTitleTextView.setText(chapterTitle);
        chapterContentsTextView = (TextView) findViewById(R.id.readchapteractivity_chaptercontents_textview);
        chapterReadButton = (Button) findViewById(R.id.readchapteractivity_tts_button);
        chapterReadButton.setOnClickListener(this);

        chapterReadStopButton = (Button) findViewById(R.id.readchapteractivity_tts_stop_button);
        chapterReadStopButton.setOnClickListener(this);

        voiceRadioGroup = (RadioGroup) findViewById(R.id.readchapteractivity_voice_radiogroup);
        RadioListener listener = new RadioListener();
        voiceRadioGroup.setOnCheckedChangeListener(listener);


        moonRadioButton = (RadioButton) findViewById(R.id.readchapteractivity_moon_RadioButton);
        sonRadioButton = (RadioButton) findViewById(R.id.readchapteractivity_son_RadioButton);

        firestore.collection("user").document(authorAccount)
                .collection("myworkspace").document(fictionTitle)
                .collection("chapters").document(chapterNumber)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String chapterContents = (String) document.getData().get("chapterContents");
                        // Toast.makeText(ReadChapterActivity.this, chapterContents, Toast.LENGTH_LONG).show();
                        chapterContentsTextView.setText((String) document.getData().get("chapterContents"));
                    } else {
                    }
                } else {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        if(voicePath == null){
            Toast.makeText(ReadChapterActivity.this,"원하는 음성을 선택해주세요",Toast.LENGTH_LONG).show();
        }else {
            switch (id) {
                case R.id.readchapteractivity_tts_button:
                    intent = new Intent(this, AudioService.class);
                    intent.putExtra(AudioService.MESSAGE_KEY, true);
                    intent.putExtra("voicePath",voicePath);
                    startService(intent);
                    break;
                case R.id.readchapteractivity_tts_stop_button:
                    intent = new Intent(this, AudioService.class);
                    stopService(intent);
                    break;
            }
        }

    }

    class RadioListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 체크상태가 변경된 라디오 그룹의 id 추출
            //group의 id와 체크된 버튼의 id 인수로 받아옴
            StorageReference storageReference;
            int id = group.getId();
            switch (id) {
                case R.id.readchapteractivity_voice_radiogroup:
                    switch (checkedId) {
                        case R.id.readchapteractivity_moon_RadioButton:
                            storageReference = firebaseStorage.getReferenceFromUrl("gs://capston-77d38.appspot.com/audio/" +
                                    authorAccount + "/" +
                                    fictionTitle+ "/" +
                                    chapterNumber+ "_moon.mp3");
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    voicePath =  uri.toString();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    voicePath=null;
                                    Toast.makeText(ReadChapterActivity.this,"챕터에 맞는 음성이 없습니다.",Toast.LENGTH_LONG).show();
                                }
                            });
                            break;
                        case R.id.readchapteractivity_son_RadioButton:
                            storageReference = firebaseStorage.getReferenceFromUrl("gs://capston-77d38.appspot.com/audio/" +
                                    authorAccount + "/" +
                                    fictionTitle+ "/" +
                                    chapterNumber+ "_son.mp3");
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    voicePath =  uri.toString();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    voicePath=null;
                                    Toast.makeText(ReadChapterActivity.this,"챕터에 맞는 음성이 없습니다.",Toast.LENGTH_LONG).show();
                                }
                            });
                            break;
                    }
            }
        }


    }
}