package com.hellodoc.abdullah.hellodoc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hellodoc.abdullah.hellodoc.Common.Common;
import com.hellodoc.abdullah.hellodoc.Model.Appointment;
import com.hellodoc.abdullah.hellodoc.Model.Doctor;
import com.squareup.picasso.Picasso;

import java.net.CookieHandler;

public class Appoint_Status extends AppCompatActivity {


    String Doctor_Id="";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference doctors= database.getInstance().getReference().child("Request");
    Appointment currentdoc;
    TextView drn,pn,pag,pdat,pti,pgn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint__status);

        drn = findViewById(R.id.drname);
        pn = findViewById(R.id.pname);
        pdat = findViewById(R.id.pdate);
        pti = findViewById(R.id.ptime);
        pag = findViewById(R.id.page);
        pgn = findViewById(R.id.pgen);

        if(getIntent() == null)
        {

        }

        else
        {

        }



            doctors.child(Common.currentUser.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        currentdoc = dataSnapshot.getValue(Appointment.class);
                        drn.setText("Doctor's Name :" + currentdoc.getDrName());
                        pn.setText("Name :" + currentdoc.getPatient_Name());
                        pdat.setText("Date :" + currentdoc.getAppointment_Date_());
                        pti.setText("Time :" + currentdoc.getAppointment_Time());
                        pag.setText("Age :" + currentdoc.getPatient_Age());
                        pgn.setText("Status :" +Common.convertCodestatus(currentdoc.getStatus()));
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(Appoint_Status.this, "No Appointment Yet", Toast.LENGTH_SHORT).show();

                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    }



