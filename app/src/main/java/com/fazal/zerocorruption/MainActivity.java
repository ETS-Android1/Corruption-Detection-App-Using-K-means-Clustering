package com.fazal.zerocorruption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {

    private EditText email;
    private EditText password;
    private Button btn;
    private Button btn2;
    private String police_id;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        btn =  findViewById(R.id.logInBtn);
        btn2 = findViewById(R.id.setAccount);
        mAuth = FirebaseAuth.getInstance();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(pass)){
                    mAuth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkUserExist();

                            }else{
                                Toast.makeText(MainActivity.this,"Log in failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this,"Please fill all the fields correctly",Toast.LENGTH_LONG);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkUserExist(){
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("policeID");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               police_id = dataSnapshot.getValue(String.class);
               if (police_id.equals("")){
                   Intent intent = new Intent(MainActivity.this,UIActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
               }else{
                   Intent intent = new Intent(MainActivity.this,ComplaintsActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
