package com.fazal.zerocorruption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class CorruptionResultActivity extends Activity {

    private TextView honest,average,corrupted;
    private double[] sum;
    private long count;
    private double no;
    private String temp;
    private int track=0;
    private boolean complete=false;
    private double dummy;
    private double[] employee_array = new double[12];
    private double[] general_array = new double[12];
    private Points[] points = new Points[12];
    private Points[] centroids = new Points[3];
    private Points[][] clusters = new Points[3][12];
    private Points[] previous = new Points[3];
    private int[][] result = new int[3][12];
    private double d1,d2,d3;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corruption_result);
        sum = new double[5];

        honest = findViewById(R.id.honest);
        average = findViewById(R.id.average);
        corrupted = findViewById(R.id.corrupted);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(CorruptionResultActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };


        //determining average for employee feedback.

        FirebaseDatabase.getInstance().getReference().child("employee_feedback").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                for (MutableData child:mutableData.getChildren()){
                    for (int j=0;j<5;j++){
                        sum[j] = 0.0;
                    }
                    dummy = 0.0;
                    count = 0;
                    int i;
                    count = child.getChildrenCount();
                    while(count == 0){
                    }
                    no = count;
                    for (MutableData m:child.getChildren()){
                        count--;
                        if (count==0){
                            break;
                        }
                        i=0;
                        for(MutableData m2:m.getChildren()){
                            temp = m2.getValue(String.class);
                            sum[i] += Double.parseDouble(temp);
                            i++;
                        }
                    }
                    for (i=0;i<5;i++){
                        sum[i] = sum[i]/(no-1);
                        dummy += sum[i];
                    }
                    employee_array[track] = dummy;
                    track++;
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                track=0;
            }
        });


        // determining average for general feedback.

        FirebaseDatabase.getInstance().getReference().child("general_feedback").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                track = 0;

                for (MutableData child:mutableData.getChildren()){
                    for (int j=0;j<5;j++){
                        sum[j] = 0.0;
                    }
                    dummy = 0.0;
                    count = 0;
                    int i;
                    count = child.getChildrenCount();
                    while(count == 0){
                    }
                    no = count;
                    for (MutableData m:child.getChildren()){
                        count--;
                        if (count==0){
                            break;
                        }
                        i=0;
                        for(MutableData m2:m.getChildren()){
                            temp = m2.getValue(String.class);
                            sum[i] += Double.parseDouble(temp);
                            i++;
                        }
                    }
                    for (i=0;i<5;i++){
                        sum[i] = sum[i]/(no-1);
                        dummy += sum[i];
                    }
                    general_array[track] = dummy;
                    track++;
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                for (int i=0;i<12;i++){
                    points[i] = new Points(employee_array[i],general_array[i]);
                }
                centroids[0] = new Points(24,24);
                centroids[1] = new Points(16,16);
                centroids[2] = new Points(10,10);

                double distance1,distance2,distance3;
                int k1,k2,k3;
                boolean flag = true;


                do{
                    k1=k2=k3=0;
                    d1=d2=d3=0.0;
                    for (int i=0;i<12;i++){
                        distance1 = calculateDistance(centroids[0],points[i]);
                        distance2 = calculateDistance(centroids[1],points[i]);
                        distance3 = calculateDistance(centroids[2],points[i]);

                        if (distance1 <= distance2){
                            if (distance1 <= distance3){
                                clusters[0][k1] = points[i];
                                k1++;
                            }else{
                                clusters[2][k3] = points[i];
                                k3++;
                            }
                        }else{
                            if (distance2 <= distance3){
                                clusters[1][k2] = points[i];
                                k2++;
                            }else{
                                clusters[2][k3] = points[i];
                                k3++;
                            }
                        }
                    }
                    for (int i=0;i<3;i++){
                        previous[i] = centroids[i];
                    }

                    double x=0.0,y=0.0;
                    for(int i=0;i<k1;i++){
                        x += clusters[0][i].x;
                        y += clusters[0][i].y;
                    }
                    centroids[0].x = x / k1;
                    centroids[0].y = y / k1;
                    x=y=0.0;

                    for(int i=0;i<k2;i++){
                        x += clusters[1][i].x;
                        y += clusters[1][i].y;
                    }
                    centroids[1].x = x / k2;
                    centroids[1].y = y / k2;
                    x=y=0.0;

                    for(int i=0;i<k3;i++){
                        x += clusters[2][i].x;
                        y += clusters[2][i].y;
                    }
                    centroids[2].x = x / k3;
                    centroids[2].y = y / k3;

                    if (previous[0] == centroids[0] && previous[1] == centroids[1] && previous[2]== centroids[2]){
                        flag = false;
                    }else{
                        flag = true;
                    }


                }while(flag);

                //static K-means clustering goes here.

                k1=k2=k3=0;
                for (int i=0;i<12;i++) {
                    distance1 = calculateDistance(centroids[0], points[i]);
                    distance2 = calculateDistance(centroids[1], points[i]);
                    distance3 = calculateDistance(centroids[2], points[i]);

                    if (distance1 <= distance2){
                        if (distance1 <= distance3){
                            clusters[0][k1] = points[i];
                            result[0][k1] = i;
                            k1++;
                        }else{
                            clusters[2][k3] = points[i];
                            result[2][k3] = i;
                            k3++;
                        }
                    }else{
                        if (distance2 <= distance3){
                            clusters[1][k2] = points[i];
                            result[1][k2] = i;
                            k2++;
                        }else{
                            clusters[2][k3] = points[i];
                            result[2][k3] = i;
                            k3++;
                        }
                    }
                }


                /*Time to predict results*/

                String honestList = new String("");
                String averageList = new String("");
                String corruptedList = new String("");

                for (int i=0;i<k1;i++){
                    honestList += name(result[0][i]) + " ";
                }
                for (int i=0;i<k2;i++){
                    averageList += name(result[1][i]) + " ";
                }
                for (int i=0;i<k3;i++){
                    corruptedList += name(result[2][i]) + " ";
                }

                honest.setText("Honest people list : "+"\n\t\t"+honestList);
                average.setText("Less honest people list : "+"\n\t\t"+averageList);
                corrupted.setText("Corrupted people list : "+"\n\t\t"+corruptedList);
            }
        });


        /*K means clustering .........
        * goes here ............*/



    }


    public double calculateDistance(Points a,Points b){
        return Math.sqrt( (a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y) );
    }

    public String name(int x){
        if (x==0) return "A";
        else if (x==1) return "B";
        else if (x==2) return "C";
        else if (x==3) return "D";
        else if (x==4) return "E";
        else if (x==5) return "F";
        else if (x==6) return "G";
        else if (x==7) return "H";
        else if (x==8) return "I";
        else if (x==9) return "J";
        else if (x==10) return "K";
        else if (x==11) return "L";
        return "";
    }

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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}

class Points{
    public double x;
    public double y;
    Points(double x,double y){
        this.x = x;
        this.y = y;
    }
}
