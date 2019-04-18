package com.example.capston;

import android.content.Intent;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;


public class MakeChapterActivity extends AppCompatActivity implements View.OnClickListener {

     String TAG ="db";
    // firebase database
    FirebaseFirestore firestore;
    // 파이어베이스 인증 객체 생성
    FirebaseAuth mAuth;
    // firebase storage
    FirebaseStorage firebaseStorage;

    private EditText chaptertitle;
    private Button chapterClearButton;
    private Button chapterSaveButton;
    private EditText chapterContents;

    String fictionTitle;
    String currentChapter;
    String userEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_chapter);

        chaptertitle =(EditText) findViewById(R.id.makechapteractivity_chaptertitle_edittext);
        chapterClearButton = (Button) findViewById(R.id.makechapteractivity_chapterclear_button);
        chapterSaveButton = (Button) findViewById(R.id.makechapteractivity_chaptersave_button);
        chapterContents = (EditText) findViewById(R.id.makechapteractivity_chaptercontents_editText);

        Intent intent = getIntent();
        fictionTitle=intent.getStringExtra("fictionTitle");
        mAuth =FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userEmail = user.getEmail();
        firestore = FirebaseFirestore.getInstance();


        firestore.collection("user").document(userEmail)
                .collection("myworkspace").document(fictionTitle).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        currentChapter= String.valueOf(Integer.parseInt((String) document.getData().get("fictionLastChater"))+1);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        chapterClearButton.setOnClickListener(this);
        chapterSaveButton.setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
       int id = v.getId();
        switch (id){
            case R.id.makechapteractivity_chapterclear_button:
                chapterContents.setText("");
                break;
            case R.id.makechapteractivity_chaptersave_button:


                Map<String, Object> data = new HashMap<>();
                data.put("fictionTitle",fictionTitle);
                data.put("chapterNumber",currentChapter);
                data.put("chapterTitle",chaptertitle.getText().toString());
                data.put("chapterContents",chapterContents.getText().toString());
                data.put("chapterFinalModifieddate", FieldValue.serverTimestamp());
                Toast.makeText(MakeChapterActivity.this,fictionTitle+"의 "+currentChapter+"장 입니다.",Toast.LENGTH_LONG).show();

               // 서버에 임시저장.
                firestore.collection("user").document(userEmail)
                        .collection("myworkspace").document(fictionTitle)
                        .collection("chapters").document(currentChapter)
                        .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MakeChapterActivity.this,"서버에 임시저장 되었습니다.!",Toast.LENGTH_LONG).show();
                    }
                });

                firestore.collection("user").document(userEmail)
                        .collection("myworkspace").document(fictionTitle).update("fictionLastChater", currentChapter)
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


        }

    }
}


