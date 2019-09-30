package com.fazal.zerocorruption;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class SignUpActivity extends Activity {
    private EditText emailField;
    private EditText passwordField;
    private EditText NIDField;
    private EditText Pid;
    private Button register;

    private FirebaseAuth mAuth;
   // private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        mAuth = FirebaseAuth.getInstance();
       // mDatabase = FirebaseDatabase.getInstance().getReference();
        mProgress = new ProgressDialog(this);

        emailField = (EditText) findViewById(R.id.emailSP);
        passwordField = (EditText) findViewById(R.id.passwordSP);
        NIDField = (EditText) findViewById(R.id.NID);
        Pid = (EditText) findViewById(R.id.policeId);
        register = (Button) findViewById(R.id.signBtn);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

    }

    private void startRegister(){
        final String policeID;
        if (Pid.getText().toString() == null) {
            policeID = "";
        }else{
            policeID = Pid.getText().toString().trim();
        }

        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        final String voter_id = NIDField.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(voter_id)){
            mProgress.setMessage("Signing up...");
            mProgress.show();


            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("users").child(user_id);

                        myRef.child("NID_no").setValue(voter_id);
                        myRef.child("policeID").setValue(policeID);
                        mProgress.dismiss();

                        if (policeID.equals("")){
                            Intent newIntent = new Intent(SignUpActivity.this,UIActivity.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(newIntent);
                        }else{
                            Intent newIntent = new Intent(SignUpActivity.this,ComplaintsActivity.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(newIntent);
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this,"sign up failed...",Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                    }
                }
            });
        }
    }
}
