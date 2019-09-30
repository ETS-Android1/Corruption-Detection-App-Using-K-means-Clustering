package com.fazal.zerocorruption;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends Activity {

    private EditText criminalName;
    private EditText descriptionField;
    private Button evidenceButton;
    private String value;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        criminalName = (EditText) findViewById(R.id.criminal_name);
        descriptionField = (EditText) findViewById(R.id.description);
        evidenceButton = (Button)findViewById(R.id.submit);


        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };


        evidenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = criminalName.getText().toString();
                String description = descriptionField.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description)){
                    Intent intent = new Intent(HomeActivity.this,EvidenceActivity.class);
                    intent.putExtra(EvidenceActivity.NAME,name);
                    intent.putExtra(EvidenceActivity.DESCRIPTION,description);
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity.this,"Must fill all the fields",Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_logout){
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

}
