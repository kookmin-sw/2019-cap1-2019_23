package com.example.capston;

import android.app.DatePickerDialog;
import android.content.Intent;


import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    String TAG = "db";
    private Uri filePath;
    //프래그먼트의프로필 이미지위한 bitmap
    Bitmap bitmap;


    Toolbar toolbar;
    ActionBar actionBar;
    TextView toolbar_TextView;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;


    // FrameLayout에 각 메뉴의 Fragment를 추가,제거,삭제 해줌
    // 액티비티에서 프래그먼트의 교체와 제거를 허용하는 경우, 액티비티의 onCreate() 메서드 동안 초기 프래그먼트를 액티비티에 추가해야 합니다.
    FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment
    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    MyBookFragment myBookFragment = new MyBookFragment();
    WorkplaceFragment workplaceFragment = new WorkplaceFragment();

    // firebase database
    private FirebaseFirestore firestore;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth mAuth;
    // firebase storage
    private FirebaseStorage firebaseStorage;
    // 프로필참조.
    TextView navigationViewHeader_profile_nickname;
    TextView navigationViewHeader_profile_email;
    ImageView navigationViewHeader_profile_Image;
    StorageReference storageImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mainActivity 참조하는 view
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        toolbar_TextView = (TextView) findViewById(R.id.toolbar_title);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

       //서버 연동
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        //툴바를 앱바로 지정한다.
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //툴바를 ActionBar 타입으로 받아와서 툴바 끝에 navigation
        actionBar.setHomeAsUpIndicator(R.drawable.round_menu_white_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 첫 화면(Fragment) 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();
        navigationView.setNavigationItemSelectedListener(this);

        //프로필 사진 지정을 위한 참조변수 선언 불러오기,둥그렇게 만들기.
        // navigation header xml 가져오기
        View navigationViewHeaderView = navigationView.getHeaderView(0);
        //header에서 프로필 사진뷰 가져오기
        navigationViewHeader_profile_Image = (ImageView) navigationViewHeaderView.findViewById(R.id.navigation_header_profile_ImageView);
        navigationViewHeader_profile_Image.setOnClickListener(this);

        //header에서 프로필 변경뷰 가져오기
        ImageView navigationViewHeader_changeprofile_Image = (ImageView) navigationViewHeaderView.findViewById(R.id.navigation_header_changeprofileImg_ImageView);
        navigationViewHeader_changeprofile_Image.setOnClickListener(this);
        //header에서 프로필 이메일 및 Nickname testview 가져오기
        navigationViewHeader_profile_email = navigationViewHeaderView.findViewById(R.id.navigation_header_email_TextView);
        navigationViewHeader_profile_nickname = navigationViewHeaderView.findViewById(R.id.navigation_header_nickname_TextView);



        //서버 사용자 프로필 정보가져오기. 위에 선언한 참조변수에 담기.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email and profile photo Url
            final String username = user.getDisplayName();
            final String userEmail = user.getEmail();
            final Uri photoUrl = user.getPhotoUrl();
            navigationViewHeader_profile_email.setText(userEmail);

            firestore.collection("user").document(userEmail)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String userNickName = (String) documentSnapshot.getData().get("userNickName");
                    navigationViewHeader_profile_nickname.setText(userNickName);

                }
            });


            storageImageRef = firebaseStorage.getReferenceFromUrl("gs://capston-77d38.appspot.com/").child("images/" + userEmail + ".png");
            storageImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).asBitmap().load(uri.toString())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    navigationViewHeader_profile_Image.setImageBitmap(resource);
                                    bitmap=resource;
                                    //할일
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        // 바로 캐싱 없이 구글 Storage에서 가져오기. 일단 보류.
            /*
            GlideApp.with(this)
                    .load(storageImageRef)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(navigationViewHeader_profile_Image);
            */


        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //bottmNavigationView들.
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        //프레그먼트 교체
                        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();
                        //프레그먼트에 맞는 툴바 출력.
                        toolbar_TextView.setText("홈");

                        break;
                    }
                    case R.id.navigation_menu2: {
                        //프레그먼트 교체
                        transaction.replace(R.id.frame_layout, searchFragment).commitAllowingStateLoss();
                        //프레그먼트에 맞는 툴바 출력.
                        toolbar_TextView.setText("검색");
                        break;
                    }
                    case R.id.navigation_menu3: {
                        //프레그먼트 교체
                        transaction.replace(R.id.frame_layout, myBookFragment).commitAllowingStateLoss();
                        //프레그먼트에 맞는 툴바 출력.
                        toolbar_TextView.setText("MyBook");
                        break;
                    }
                    case R.id.navigation_menu4: {
                        //프레그먼트 교체
                        transaction.replace(R.id.frame_layout, workplaceFragment).commitAllowingStateLoss();
                        //프레그먼트에 맞는 툴바 출력.
                        toolbar_TextView.setText("내 작업실");
                        break;
                    }
                }
                return true;
            }
        });
    }
    //toolbar메뉴 생성.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_appbar, menu);
        // 검색뷰가 설정되어있는 메뉴 항목객체 추출
        MenuItem searchItem = menu.findItem(R.id.item1);
        // 액션 뷰로 설정된 뷰를 추출한다.
        SearchView searchView = (SearchView) searchItem.getActionView();
        // 안내문구 설정
        searchView.setQueryHint("검색어를 입력하세요");
        // 서치뷰의 리스너 설정을 해줘야 fragment에서 검색을 실제로 할수있다.
        return true;
    }

    // 햄버거 버튼으로 navigationView 오픈(클릭시)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // navigationView 백버튼으로 닫기.
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    // navigationdrawer 아이템 선택 리스너
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.noticeboard: {

                break;
            }
            case R.id.report: {

                break;
            }
            case R.id.logout: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }

        return false;
    }
    // 리턴받은 인텐트 처리.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        View navigationViewHeaderView = navigationView.getHeaderView(0);
        final ImageView navigationViewHeader_profile_Image = (ImageView) navigationViewHeaderView.findViewById(R.id.navigation_header_profile_ImageView);

            //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면 이미지뷰에 셋팅.
            if (requestCode == 0 && resultCode == RESULT_OK) {
                filePath = data.getData();
                Log.d(TAG, "uri:" + String.valueOf(filePath));
                /*
                    //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                    GlideApp.with(this)
                            .load(filePath)
                            .into(navigationViewHeader_profile_Image);
                */
                Glide.with(getApplicationContext()).asBitmap().load(filePath)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                navigationViewHeader_profile_Image.setImageBitmap(resource);
                                bitmap=resource;
                                //할일
                            }
                        });
            }
            //프로필을 서버 이미지에 저장
            if (filePath != null) {
                // 파일이름을 유저의 ID로 지정.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String filename = user.getEmail() + ".png";
                //스토리지 참조불러오기 및 업로드.
                firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://capston-77d38.appspot.com/").child("images/" + filename);
                storageRef.putFile(filePath)
                        //성공시
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(MainActivity.this, "서버 업로드 성공.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //실패시
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
            }
            filePath = null;
    }


    // 메인의 기본 버튼의 리스너설정.
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.navigation_header_profile_ImageView:
                //
                break;
            case R.id.navigation_header_changeprofileImg_ImageView:
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                // 암시적 인텐트 전송해서 폰내의 모든 이미지관리 앱 에 전송.
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
                //onActivityResult 실행
                break;
        }
    }
}


