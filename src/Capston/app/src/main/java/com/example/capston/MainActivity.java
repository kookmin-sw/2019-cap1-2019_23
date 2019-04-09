package com.example.capston;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment
    HomeFragment homeFragment= new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    MyBookFragment myBookFragment= new MyBookFragment();
    WorkplaceFragment workplaceFragment = new WorkplaceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        toolbar = (Toolbar) findViewById(R.id.app_toolbar) ;
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);


        //툴바를 앱바로 지정한다.
        setSupportActionBar(toolbar);
        //툴바를 ActionBar 타입으로 받아와서 툴바 끝에 navigation
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.round_menu_white_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, homeFragment ).commitAllowingStateLoss();
        navigationView.setNavigationItemSelectedListener(this);







        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //bottmNavigationView들.
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        transaction.replace(R.id.frame_layout,searchFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu3: {
                        transaction.replace(R.id.frame_layout,myBookFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu4: {
                        transaction.replace(R.id.frame_layout,workplaceFragment).commitAllowingStateLoss();
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
        inflater.inflate(R.menu.main_appbar,menu);
        // 검색뷰가 설정되어있는 메뉴 항목객체 추출
        MenuItem searchItem = menu.findItem(R.id.item1);
        // 액션 뷰로 설정된 뷰를 추출한다.
        SearchView searchView = (SearchView) searchItem.getActionView();
        // 안내문구 설정
        searchView.setQueryHint("검색어를 입력하세요");
        // 서치뷰의 리스너 설정을 해줘야 fragment에서 검색을 실제로 할수있다.
        return true;
    }

    // 햄버거 버튼오픈(클릭시)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // navigationdrawer 백버튼으로 닫기.
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }
    }
    // navigationdrawer 아이템 선택 리스너
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id =menuItem.getItemId();

        switch (id){
            case R.id.noticeboard:{

                break;
            }
            case R.id.report: {

                break;
            }
            case R.id.logout: {
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            }



        }



        return false;
    }
}
