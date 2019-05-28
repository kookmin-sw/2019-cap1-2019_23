package com.example.capston;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MakeFictionActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "db";
    private Uri filePath;
    // 화면의 view 참조
    ImageView fictionCover;
    ImageView fictionCoverSelect;
    EditText fictionTitle, fictionCategory;
    Button categorySelectButton, makeFictionButton;
    CustomProgressDialog customProgressDialog;
    // firebase database
    private FirebaseFirestore firestore;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth mAuth;
    // firebase storage
    private FirebaseStorage firebaseStorage;
    private String userNickName;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makefiction);
        fictionCover = (ImageView) findViewById(R.id.makefiction_fictioncover_ImageView);
        fictionCoverSelect = (ImageView) findViewById(R.id.makefiction_fictioncoverselect_ImageView);
        fictionTitle = (EditText) findViewById(R.id.makefiction_title_edittext);
        fictionCategory = (EditText) findViewById(R.id.makefiction_category_edittext);
        categorySelectButton = (Button) findViewById(R.id.makefiction_categoryselect_button);
        makeFictionButton = (Button) findViewById(R.id.makefiction_makefiction_button);
        // 작동해야될 버튼들.
        fictionCoverSelect.setOnClickListener(this);
        categorySelectButton.setOnClickListener(this);
        makeFictionButton.setOnClickListener(this);
        customProgressDialog = new CustomProgressDialog(this);

        // 서버연동.
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        firestore.collection("user").document(user.getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userNickName = (String) documentSnapshot.getData().get("userNickName");
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.makefiction_fictioncoverselect_ImageView:
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                // 암시적 인텐트 전송해서 폰내의 모든 이미지관리 앱 에 전송.
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
                //onActivityResult 실행
                break;
            case R.id.makefiction_categoryselect_button:
                //popup menu 객체 생성
                PopupMenu pop = new PopupMenu(this, categorySelectButton);
                //popup menu 관리객체 추출
                Menu menu = pop.getMenu();
                //메뉴구성
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.makefiction_category_select_menu, menu);
                //리스너설정
                PopListener listener = new PopListener();
                pop.setOnMenuItemClickListener(listener);
                //메뉴를 나타게 한다.
                pop.show();
                break;
            case R.id.makefiction_makefiction_button:
                // 현재 로그인된 사용자의 정보를 받아온다.

                if (fictionTitle.getText().toString().isEmpty() || fictionCategory.getText().toString().isEmpty()) {
                    Toast.makeText(MakeFictionActivity.this, "제목과 장르를 입력하세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 서버 이미지에 저장
                if (filePath != null) {
                    final String folderName = user.getEmail();
                    final String fileName = fictionTitle.getText().toString() + ".png";
                    final Map<String, Object> data = new HashMap<>();
                    final Uri localBookCover = filePath;

                    //스토리지 참조불러오기 및 업로드.
                    StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://capston-77d38.appspot.com/")
                            .child("images/")
                            .child(folderName + "/" + fileName);
                    storageRef.putFile(localBookCover)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //Toast.makeText(MakeFictionActivity.this, "표지 업로드 성공.", Toast.LENGTH_SHORT).show();
                                    //저자, 제목,카테고리,경로,생성일,좋아요..
                                    data.put("userEmail",user.getEmail());
                                    data.put("author", userNickName);
                                    data.put("fictionTitle", fictionTitle.getText().toString());
                                    data.put("fictionCategory", fictionCategory.getText().toString());
                                    data.put("fictionCreationdate", FieldValue.serverTimestamp());
                                    data.put("fictionImgCoverPath", "gs://capston-77d38.appspot.com/images/" + folderName + "/" + fileName);
                                    data.put("fictionLastChater","0");
                                    data.put("published",false);
                                    // 맵추가
                                    data.put("likes",new HashMap<String,Object>());
                                    data.put("bookmark",new HashMap<String,Object>());
                                    // 데이터 베이스 하위 collection(Table)생성 및 저장.
                                    // 개인 문서 workspace
                                    firestore.collection("user").document(user.getEmail())
                                             .collection("myworkspace").document(fictionTitle.getText().toString())
                                             .set(data);
                                }
                            });
                    filePath = null;
                    finish();
                } else {
                    Toast.makeText(MakeFictionActivity.this, "표지가 없는 소설은 생성할수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // 리턴받은 인텐트 처리.
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면 이미지뷰에 셋팅.
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
            Glide.with(this)
                    .load(filePath)
                    .into(fictionCover);
        }
    }

    // 팝업메뉴 리스터 클래스를 내부에 정의한다.
    class PopListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            // 사용자가 선택한 메뉴의 아이디 값을 추출
            int id = menuItem.getItemId();

            switch (id) {
                case R.id.item1:
                    fictionCategory.setText("로멘스");
                    break;
                case R.id.item2:
                    fictionCategory.setText("추리");
                    break;
                case R.id.item3:
                    fictionCategory.setText("판타지");
                    break;
                case R.id.item4:
                    fictionCategory.setText("호러");
                    break;
                case R.id.item5:
                    fictionCategory.setText("SF");
                    break;
                case R.id.item6:
                    fictionCategory.setText("무협");
                    break;
                case R.id.item7:
                    fictionCategory.setText("기타");
                    break;
            }

            return false;
        }
    }

}
