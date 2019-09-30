package com.fazal.zerocorruption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AgainstActivity extends Activity {
    private Button p;
    private RadioGroup rg;
    private String selection;
    private String isEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_against);

        Intent intent = getIntent();
        isEmployee = intent.getStringExtra("isEmployee");

        p = findViewById(R.id.proceed);
        rg = findViewById(R.id.choose_employee);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.option1:
                        selection = "A";
                        break;
                    case R.id.option2:
                        selection = "B";
                        break;
                    case R.id.option3:
                        selection = "C";
                        break;
                    case R.id.option4:
                        selection = "D";
                        break;
                    case R.id.option5:
                        selection = "E";
                        break;
                    case R.id.option6:
                        selection = "F";
                        break;
                    case R.id.option7:
                        selection = "G";
                        break;
                    case R.id.option8:
                        selection = "H";
                        break;
                    case R.id.option9:
                        selection = "I";
                        break;
                    case R.id.option10:
                        selection = "J";
                        break;
                    case R.id.option11:
                        selection = "K";
                        break;
                    case R.id.option12:
                        selection = "L";
                        break;
                }
            }
        });

        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = rg.getCheckedRadioButtonId();
                if (id == -1){
                    Toast.makeText(AgainstActivity.this,"Must choose an Employee",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(AgainstActivity.this,QuestionActivity.class);
                    intent.putExtra("selection",selection);
                    intent.putExtra("isEmployee",isEmployee);
                    startActivity(intent);
                }
            }
        });
    }
}
