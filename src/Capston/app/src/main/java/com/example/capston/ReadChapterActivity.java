package com.example.capston;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReadChapterActivity extends AppCompatActivity {


    TextView chapterTitleTextView;
    TextView chapterContentsTextView;
    Button chapterReadButton;


    // firebase database
    static FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        String authorAccount =  intent.getStringExtra("authorAccount");
        String fictionTitle = intent.getStringExtra("fictionTitle");
        String chapterNumber = intent.getStringExtra("chapterNumber").substring(1,2);

        String chapterTitle = intent.getStringExtra("chapterTitle");
       //Toast.makeText(this, authorAccount+fictionTitle+"  "+chapterNumber+chapterTitle, Toast.LENGTH_SHORT).show();



        chapterTitleTextView = (TextView)findViewById(R.id.readchapteractivity_chaptertitle_textview);
        chapterTitleTextView.setText(chapterTitle);
        chapterContentsTextView = (TextView) findViewById(R.id.readchapteractivity_chaptercontents_textview);
        chapterReadButton=(Button) findViewById(R.id.readchapteractivity_tts_button);




        firestore.collection("user").document(authorAccount)
                .collection("myworkspace").document(fictionTitle)
                .collection("chapters").document(chapterNumber)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String chapterContents = (String)document.getData().get("chapterContents");
                       // Toast.makeText(ReadChapterActivity.this, chapterContents, Toast.LENGTH_LONG).show();
                        chapterContentsTextView.setText((String)document.getData().get("chapterContents"));
                    } else {
                    }
                } else {

                }
            }
        });
    }
}
