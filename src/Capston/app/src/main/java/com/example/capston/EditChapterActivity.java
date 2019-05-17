package com.example.capston;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.speech.tts.TextToSpeech.ERROR;

public class EditChapterActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    String TAG ="EditChapterActivity";
    // 데이터베이스를 접근하기위한 변수
    String fictionTitle;
    String chapterNumber;
    String userEmail;
    EditText chapterTitle;
    EditText chapterContents;
    Button chapterClearButton;
    Button chapterSaveButton;
    //임시 TTS
    Button chapterTTSButton;
    private TextToSpeech tts;
    // firebase database
    FirebaseFirestore firestore;
    // 파이어베이스 인증 객체 생성
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chapter);

        // 인텐트에서 어떤소설에서 몇 챕터를 수정하기위한 데이터 받기.
        Intent intent = getIntent();
        fictionTitle=intent.getStringExtra("fictionTitle");
        chapterNumber = intent.getStringExtra("chapterNumber");
        chapterNumber =chapterNumber.substring(1,chapterNumber.length()-1);

        chapterTitle = (EditText) findViewById(R.id.editchapteractivity_chaptertitle_edittext);
        chapterContents = (EditText) findViewById(R.id.editchapteractivity_chaptercontents_editText);
        chapterClearButton = (Button) findViewById(R.id.editchapteractivity_chapterclear_button);
        chapterClearButton.setOnClickListener(this);
        chapterSaveButton= (Button)findViewById(R.id.editchapteractivity_chaptersave_button);
        chapterSaveButton.setOnClickListener(this);
        // TTS를 생성하고 OnInitListener로 초기화 한다.
        tts = new TextToSpeech(this, this);
        //TTS 임시로 해보기.
        chapterTTSButton = (Button)findViewById(R.id.editchapteractivity_ttstemp_button);
        chapterTTSButton.setOnClickListener(this);

        // 서버연동 싱글톤 받기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userEmail = user.getEmail();
        firestore = FirebaseFirestore.getInstance();


        firestore.collection("user").document(userEmail)
                .collection("myworkspace").document(fictionTitle)
                .collection("chapters").document(chapterNumber)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        chapterTitle.setText((String)document.getData().get("chapterTitle"));
                        chapterContents.setText((String)document.getData().get("chapterContents"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
    @Override
    public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.editchapteractivity_chapterclear_button:
                    chapterContents.setText("");
                    Toast.makeText(this, "잘못눌렀다면 저장하지마시고 다시 챕터를 수정하세요.", Toast.LENGTH_LONG).show();
                    break;
                case R.id.editchapteractivity_chaptersave_button:
                    firestore.collection("user").document(userEmail)
                            .collection("myworkspace").document(fictionTitle)
                            .collection("chapters").document(chapterNumber).update("chapterTitle",chapterTitle.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                    firestore.collection("user").document(userEmail)
                            .collection("myworkspace").document(fictionTitle)
                            .collection("chapters").document(chapterNumber).update("chapterContents",chapterContents.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                    finish();
                    break;
                case R.id.editchapteractivity_ttstemp_button:
                    tts.setPitch((float) 0.7);      // 목소리톤
                    tts.setSpeechRate((float) 0.8);    // 읽는 속도는 기본 설정
                    // editText에 있는 문장을 읽는다.
                    Toast.makeText(this, "TTS 시작.", Toast.LENGTH_SHORT).show();
                    tts.speak(chapterContents.getText().toString(),TextToSpeech.QUEUE_FLUSH, null);
                    break;

            }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // 작업 성공
            int language = tts.setLanguage(Locale.KOREAN);  // 언어 설정
            if (language == TextToSpeech.LANG_MISSING_DATA
                    || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어 데이터가 없거나, 지원하지 않는경우
                Toast.makeText(this, "지원하지 않는 언어입니다.", Toast.LENGTH_SHORT).show();
            } else {
                // 준비 완료
            }
        } else {
            // 작업 실패
            Toast.makeText(this, "TTS 작업에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "TTS 설정완료.", Toast.LENGTH_SHORT).show();
    }
    @Override

    protected void onDestroy() {

        if (tts != null) {

            tts.stop();

            tts.shutdown();

        }

        super.onDestroy();

    }

}

