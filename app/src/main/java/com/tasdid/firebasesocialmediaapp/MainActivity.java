package com.tasdid.firebasesocialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private EditText edtEmail, edtUserName, edtPassword;
    private Button btnSignUp, btnSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.editEmail);
        edtUserName =findViewById(R.id.editUserName);
        edtPassword =findViewById(R.id.editPassword);
        btnSignUp =findViewById(R.id.btnSignUp);
        btnSignIn =findViewById(R.id.btnSignIn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUp();

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            //Transition to next activity
            transitionToSocialMediaActivity();
        }
    }

    private void signUp(){

        mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Toast.makeText(MainActivity.this,"Signing Up Successful",Toast.LENGTH_LONG).show();
                    FirebaseDatabase.getInstance().getReference()
                            .child("my_users").child(task.getResult().getUser()
                            .getUid()).child("username")
                            .setValue(edtUserName.getText().toString());
                    transitionToSocialMediaActivity();


                }else{

                    Toast.makeText(MainActivity.this,"Signing Up Failed",Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    private void signIn(){

        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(MainActivity.this,"Signing In successful",Toast.LENGTH_LONG).show();

                    transitionToSocialMediaActivity();

                }else {

                    Toast.makeText(MainActivity.this,"Signing In Failed",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void transitionToSocialMediaActivity(){

        Intent intent = new Intent(this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
