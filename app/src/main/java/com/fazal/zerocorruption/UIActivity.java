package com.fazal.zerocorruption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class UIActivity extends Activity {
    private Button btn1;
    private Button btn2;
    private EditText employee_code;
    private CheckBox checkBox;
    private String isEmployee;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);

        btn1 = findViewById(R.id.first_btn);
        btn2 =   findViewById(R.id.second_btn);
        checkBox = findViewById(R.id.employee);
        employee_code = findViewById(R.id.code);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(UIActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = employee_code.getText().toString().trim();
                Boolean check = checkBox.isChecked();
                if (check){
                    isEmployee = "1";
                }else{
                    isEmployee = "2";
                }
                if ((code.equals("1234") && isEmployee.equals("1")) || isEmployee.equals("2")){
                    Intent intent = new Intent(UIActivity.this,AgainstActivity.class);
                    intent.putExtra("isEmployee",isEmployee);
                    startActivity(intent);
                }else{
                    Toast.makeText(UIActivity.this,"Employee code did not match",Toast.LENGTH_LONG).show();
                }


            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UIActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

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
