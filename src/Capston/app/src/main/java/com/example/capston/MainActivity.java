package com.example.capston;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    HomeFragment homeFragment= new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    MyBookFragment myBookFragment= new MyBookFragment();
    WorkplaceFragment workplaceFragment = new WorkplaceFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, homeFragment ).commitAllowingStateLoss();



        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //메뉴 인플레이터로 매뉴생성후 Activity에 설정.
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.main_actionbar,menu);
        // 검색뷰가 설정되어있는 메뉴 항목객체 추출
        MenuItem searchItem = menu.findItem(R.id.item1);
        // 액션 뷰로 설정된 뷰를 추출한다.
        SearchView searchView = (SearchView) searchItem.getActionView();
        // 안내문구 설정
        searchView.setQueryHint("검색어를 입력하세요");
        //서치뷰 리스너설정.
        //리턴true를 해야 보인다.
        return true;
    }
}
