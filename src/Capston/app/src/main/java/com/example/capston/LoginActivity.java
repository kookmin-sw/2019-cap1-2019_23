package com.example.capston;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, pwEditText;


    // firebase database
    private FirebaseFirestore firestore;
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth mAuth;

    CustomProgressDialog customProgressDialog;
    // 체크할 권한 목록
    String[] permission_list = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);


        // firebase 인증 객체 얻기
        mAuth = FirebaseAuth.getInstance();
        // firestore 객체 얻기
        firestore = FirebaseFirestore.getInstance();
        // Email과 pw editText 객체 얻기.
        emailEditText = (EditText) findViewById(R.id.login_email_edit);
        pwEditText = (EditText) findViewById(R.id.login_pw_edit);

        customProgressDialog = new CustomProgressDialog(this);

        findViewById(R.id.login_signup_button).setOnClickListener(this);
        findViewById(R.id.login_signin_button).setOnClickListener(this);
        checkPermission();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login_signup_button:
                // 회원가입 Activity로 이동.
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.login_signin_button:
                String userEmail = emailEditText.getText().toString();
                String userPW = pwEditText.getText().toString();

                if(  userEmail.isEmpty()  || userPW.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email과 비밀번호를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("test", userEmail);
                    Log.d("test", userPW);
                    customProgressDialog.show();
                    mAuth.signInWithEmailAndPassword(userEmail, userPW)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("test", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        customProgressDialog.dismiss();
                                        Log.w("test", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
                break;
        }
    }

    //권한 확인 메소드
    public void checkPermission(){
        //현재 안드로이드 버젼이 6.0 미만이면 메소드 종료
        if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.M){
            return ;
        }
        // 각 권한의 확인 여부
        for(String permission :permission_list){
            // 권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);
            // 거부상태이면
            if(chk== PackageManager.PERMISSION_DENIED){
                // 사용자에게 권한 요창 창을 생성
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customProgressDialog.dismiss();
    }
}
