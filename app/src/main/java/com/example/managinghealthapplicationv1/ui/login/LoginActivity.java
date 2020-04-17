package com.example.managinghealthapplicationv1.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.managinghealthapplicationv1.MainActivity;
import com.example.managinghealthapplicationv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    EditText emailId, passwordId;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        passwordId = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.sign_in);
        tvSignUp = findViewById(R.id.altsign_up);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null ){
                    Toast.makeText(LoginActivity.this, "Successfully logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please login to start MHA",Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailId.getText().toString();
                String password = passwordId.getText().toString();
                if (email.isEmpty()){
                    emailId.setError("Please enter a valid email address");
                    emailId.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordId.setError("Please enter a password");
                }
                else if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Both fields are empty, please fill them in", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && password.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login error, please try again shortly", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intToMain = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intToMain);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "An error occurred, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intSignUp);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
