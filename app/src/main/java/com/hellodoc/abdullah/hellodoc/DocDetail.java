package com.hellodoc.abdullah.hellodoc;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hellodoc.abdullah.hellodoc.Common.Common;
import com.hellodoc.abdullah.hellodoc.Database.Database;
import com.hellodoc.abdullah.hellodoc.Model.Appoint;
import com.hellodoc.abdullah.hellodoc.Model.Appointment;
import com.hellodoc.abdullah.hellodoc.Model.Doctor;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.hoang8f.widget.FButton;

public class DocDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

    TextView name_doc,doc_avail,doc_edu,doc_add,settime,setdate;
    ImageView img_doc;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String Doctor_Id="",ph;
    FirebaseDatabase database;
    DatabaseReference doctors;

    DatabaseReference request;
static public String date,time;
    Button btncal,btnclo;

    FButton btnapoint;
    Doctor currentdoc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);

        database = FirebaseDatabase.getInstance();
        doctors= database.getReference("Doctor");

        btnapoint = findViewById(R.id.btnapoint);
        btnapoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertDialog();




            }
        });

        name_doc=(TextView)findViewById(R.id.name_doc);
        doc_add=(TextView)findViewById(R.id.doc_address);
        doc_edu=(TextView)findViewById(R.id.doc_edu);
        doc_avail=(TextView)findViewById(R.id.doc_avail);
        img_doc=findViewById(R.id.img_doc);
        setdate = findViewById(R.id.setdate);
        settime = findViewById(R.id.settime);

        collapsingToolbarLayout = findViewById(R.id.Collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandeAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);


        //get Doc Id from Intent
        if (getIntent()!=null)
            Doctor_Id=getIntent().getStringExtra("doctor_Id");
        if(!Doctor_Id.isEmpty())

        {
            if(Common.isConnected(getBaseContext())) {
                getDetailDoc(Doctor_Id);
            }
            else
            {
                Toast.makeText(this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        btncal = findViewById(R.id.btncal);
        btnclo = findViewById(R.id.btnclo);

        btnclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
 com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd =
         com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(DocDetail.this
         ,now.get(Calendar.HOUR_OF_DAY)
         ,now.get(Calendar.MINUTE)
         ,now.get(Calendar.AM_PM)
         ,false);
        tpd.setTitle("Choose Time for Oppointment");
        tpd.show(getFragmentManager(),"TimePicker");



            }
        });

        btncal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        DocDetail.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                        dpd.setTitle("Choose Date for oppintment");
                        dpd.show(getFragmentManager(),"DatePicker");



            }
        });


    }

    private  void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DocDetail.this);
        alertDialog.setTitle("One More Step!");

        final Context context = DocDetail.this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText edtAge = new EditText(context);
        edtAge.setHint("Age");
        layout.addView(edtAge); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText edtGen = new EditText(context);
        edtGen.setHint("Gender");
        layout.addView(edtGen); // Another add method

        alertDialog.setView(layout);



alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
Appointment ap = new Appointment();
final String dr  =ap.getDrName();

        database = FirebaseDatabase.getInstance();
        request= database.getReference("Request");
        request.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.child(Common.currentUser.getPhone().toString()).exists())
                {
                    Toast.makeText(context, "Already have an Appointment", Toast.LENGTH_SHORT).show();
                }
                else
                {


                  Appointment appointment = new Appointment(
                            Common.currentUser.getName(),
                            edtAge.getText().toString(),
                            edtGen.getText().toString(),
                            date,
                            time,
                            Common.currentUser.getPhone(),
                          currentdoc.getName()


                    );


                    request.child(String.valueOf(Common.currentUser.getPhone()))
                            .setValue(appointment);

                    Toast.makeText(DocDetail.this,"Thank You For Appointment",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DocDetail.this,Appoint_Status.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
});


        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        alertDialog.show();

            }

    private void getDetailDoc(String doctor_id) {
        try {
            doctors.child(Doctor_Id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentdoc = dataSnapshot.getValue(Doctor.class);


                    //set Image

                    Picasso.get().load(currentdoc.getImage())
                            .into(img_doc);

                    collapsingToolbarLayout.setTitle(currentdoc.getName());


                    doc_add.setText(currentdoc.getAddress());
                    doc_edu.setText(currentdoc.getQualification());
                    doc_avail.setText(currentdoc.getDays());
                    name_doc.setText(currentdoc.getName());


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e)
        {

        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setdate.setText("Date Of Oppointment :" + dayOfMonth+"/"+monthOfYear+"/"+year);
        date= dayOfMonth+"/"+monthOfYear+"/"+year;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        String timeSet = "";
        if (hourOfDay > 12) {
            hourOfDay -= 12;
            timeSet = "PM";
        } else if (hourOfDay == 0) {
            hourOfDay += 12;
            timeSet = "AM";
        } else if (hourOfDay == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (minute < 10)
            minutes = "0" + minutes;
        else
            minutes = String.valueOf(minute);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hourOfDay).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

            settime.setText("Time Of Oppointment :" + aTime );
        time = aTime;
    }
}
