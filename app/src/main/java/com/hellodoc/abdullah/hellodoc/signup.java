package com.hellodoc.abdullah.hellodoc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hellodoc.abdullah.hellodoc.Common.Common;
import com.hellodoc.abdullah.hellodoc.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class signup extends AppCompatActivity {

    MaterialEditText edtPhone,edtName,edtPassword,edtEmail;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtEmail = (MaterialEditText)findViewById(R.id.edtEmail);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);


        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnected(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(signup.this);
                    mDialog.setMessage("Please Wait...");
                    mDialog.show();


                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if already user phone
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(signup.this, "Phone Number Already register", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString(), edtEmail.getText().toString());
                                table_user.child(edtPhone.getText().toString()).setValue(user);


                                Toast.makeText(signup.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(signup.this, Signin.class);
                                startActivity(intent);


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(signup.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
