package com.example.capston;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText emailEditText, nicknameEditText,pwEditText, confirmPwEditText;
    private FirebaseFirestore firestore;
    private String userUniqueNumber;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailEditText= (EditText) findViewById(R.id.signup_email_edit);
        nicknameEditText =(EditText) findViewById(R.id.signup_nickname_edit);
        pwEditText = (EditText) findViewById(R.id.signup_pw_edit);
        confirmPwEditText =(EditText) findViewById(R.id.signup_pwConfirm_edit);

        findViewById(R.id.signup_complete_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(pwEditText.getText().toString().equals( confirmPwEditText.getText().toString() )) {

            final String userEmail= emailEditText.getText().toString();
            final String userNickName = nicknameEditText.getText().toString();
            final String userPW = pwEditText.getText().toString();

            mAuth.createUserWithEmailAndPassword(userEmail, userPW)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();


                                Map<String, Object> data = new HashMap<>();
                                data.put("userEmail", userEmail);
                                data.put("userNickName", userNickName);
                                data.put("userPW", userPW);
                                data.put("idCreationdate", FieldValue.serverTimestamp());

                                firestore.collection("user").document(userEmail).set(data);
                                Toast.makeText(SignUpActivity.this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }else{
            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
        }
    }
}
