package com.fazal.zerocorruption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class QuestionActivity extends Activity {
    private String checkEmployee;
    private Button submit;
    private RadioGroup rg1,rg2,rg3,rg4,rg5;
    private String v1,v2,v3,v4,v5,selection;
    private Integer opinion_no;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        rg1 =  findViewById(R.id.group1);
        rg2 =  findViewById(R.id.group2);
        rg3 =  findViewById(R.id.group3);

        rg4 =  findViewById(R.id.group4);
        rg5 =  findViewById(R.id.group5);
        submit =  findViewById(R.id.submit_evaluation);

        Intent intent = getIntent();
        checkEmployee = intent.getStringExtra("isEmployee");
        selection = intent.getStringExtra("selection");

       rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int i) {
               switch(i){
                   case R.id.sdisagree1:
                       v1 = "1";
                       break;
                   case R.id.disagree1:
                       v1 = "2";
                       break;
                   case R.id.neutral1:
                       v1 = "3";
                       break;
                   case R.id.agree1:
                       v1 = "4";
                       break;
                   case R.id.sagree1:
                       v1 = "5";
                       break;

               }
           }
       });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.sdisagree2:
                        v2 = "1";
                        break;
                    case R.id.disagree2:
                        v2 = "2";
                        break;
                    case R.id.neutral2:
                        v2 = "3";
                        break;
                    case R.id.agree2:
                        v2 = "4";
                        break;
                    case R.id.sagree2:
                        v2 = "5";
                        break;
                }
            }
        });



        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.sdisagree3:
                        v3 = "1";
                        break;
                    case R.id.disagree3:
                        v3 = "2";
                        break;
                    case R.id.neutral3:
                        v3 = "3";
                        break;
                    case R.id.agree3:
                        v3 = "4";
                        break;
                    case R.id.sagree3:
                        v3 = "5";
                        break;
                }
            }
        });

        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.sdisagree4:
                        v4 = "1";
                        break;
                    case R.id.disagree4:
                        v4 = "2";
                        break;
                    case R.id.neutral4:
                        v4 = "3";
                        break;
                    case R.id.agree4:
                        v4 = "4";
                        break;
                    case R.id.sagree4:
                        v4 = "5";
                        break;
                }
            }
        });

        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.sdisagree5:
                        v5 = "1";
                        break;
                    case R.id.disagree5:
                        v5 = "2";
                        break;
                    case R.id.neutral5:
                        v5 = "3";
                        break;
                    case R.id.agree5:
                        v5 = "4";
                        break;
                    case R.id.sagree5:
                        v5 = "5";
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //my new addition

                        int id1 = rg1.getCheckedRadioButtonId();
                        int id2 = rg2.getCheckedRadioButtonId();
                        int id3 = rg3.getCheckedRadioButtonId();
                        int id4 = rg4.getCheckedRadioButtonId();
                        int id5 = rg5.getCheckedRadioButtonId();

                        if (id1 == -1 || id2 == -2 || id3 == -1 || id4 == -1 || id5 == -1){
                            Toast.makeText(QuestionActivity.this,"must answer all the questions",Toast.LENGTH_LONG).show();
                        }

                        // new addition ends here.

                        else {
                            if (checkEmployee.equals("1")) {
                                myRef = database.getReference("employee_feedback").child(selection).child("opinion_no");
                                myRef.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        opinion_no = mutableData.getValue(Integer.class);
                                        if (opinion_no != null) {
                                            mutableData.setValue(++opinion_no);

                                        }
                                        else {
                                            opinion_no = 0;
                                            mutableData.setValue(opinion_no);
                                        }
                                        return Transaction.success(mutableData);
                                    }
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                        opinion_no = opinion_no % 50;
                                        myRef = database.getReference("employee_feedback").child(selection).child(""+opinion_no);
                                        myRef.child("1v").setValue(v1);
                                        myRef.child("2v").setValue(v2);
                                        myRef.child("3v").setValue(v3);
                                        myRef.child("4v").setValue(v4);
                                        myRef.child("5v").setValue(v5);

                                        Intent intentf = new Intent(QuestionActivity.this,UIActivity.class);
                                        intentf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intentf);
                                    }
                                });

                            }else {
                                myRef = database.getReference("general_feedback").child(selection).child("opinion_no");
                                myRef.runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        opinion_no = mutableData.getValue(Integer.class);
                                        if (opinion_no != null) {
                                            mutableData.setValue(++opinion_no);

                                        }
                                        else {
                                            opinion_no = 0;
                                            mutableData.setValue(opinion_no);
                                        }
                                        return Transaction.success(mutableData);
                                    }
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                        opinion_no = opinion_no % 50;
                                        myRef = database.getReference("general_feedback").child(selection).child(""+opinion_no);
                                        myRef.child("1v").setValue(v1);
                                        myRef.child("2v").setValue(v2);
                                        myRef.child("3v").setValue(v3);
                                        myRef.child("4v").setValue(v4);
                                        myRef.child("5v").setValue(v5);

                                        Intent intentf = new Intent(QuestionActivity.this,UIActivity.class);
                                        intentf.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intentf);
                                    }
                                });


//                                Intent intent = new Intent(QuestionActivity.this, UIActivity.class);
//                                startActivity(intent);
                            }

                        }

            }
        });

    }
}
