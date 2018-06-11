package com.hellodoc.abdullah.hellodoc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hellodoc.abdullah.hellodoc.Common.Common;
import com.hellodoc.abdullah.hellodoc.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Signin extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignIn;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        checkBox = findViewById(R.id.Rem);


            Paper.init(this);


        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(Common.isConnected(getBaseContext())) {

                if(checkBox.isChecked()) {
                    Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
                    Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());
                }



                final ProgressDialog mDialog = new ProgressDialog(Signin.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();


                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Chack if useer not exist in database

                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                            //Get Iser Informatation
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setPhone(edtPhone.getText().toString());//set phone
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                {
                                    finish();
                                    Intent intent = new Intent(Signin.this, DocHome.class);
                                    Common.currentUser = user;
                                    startActivity(intent);
                                    Toast.makeText(Signin.this, "Successfull", Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                Toast.makeText(Signin.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            mDialog.dismiss();
                            Toast.makeText(Signin.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
            else
            {
                Toast.makeText(Signin.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
            return;
             }
            }
        });

    }
}
