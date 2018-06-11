package com.hellodoc.abdullah.hellodoc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import io.paperdb.Paper;


public class welcome extends AppCompatActivity {

    public static String user,pwd;
    Button btnSignin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnSignin = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        Paper.init(this);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(welcome.this, Signin.class);
                startActivity(homeIntent);

            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(welcome.this, signup.class);
                startActivity(homeIntent);
            }

        });


        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);


      if(user !=null && pwd !=null)
      {
          if(!user.isEmpty() && !pwd.isEmpty())
          {
              login(user,pwd);
          }
      }


        }
        private void login(final String phone, final String pwd)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_user = database.getReference("User");
            if(Common.isConnected(getBaseContext())) {




                final ProgressDialog mDialog = new ProgressDialog(welcome.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();


                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Chack if useer not exist in database

                        if (dataSnapshot.child(phone).exists()) {

                            //Get Iser Informatation
                            mDialog.dismiss();
                            User user = dataSnapshot.child(phone).getValue(User.class);
                            user.setPhone(phone);//set phone
                            if (user.getPassword().equals(pwd)) {
                                {
                                    finish();
                                    Intent intent = new Intent(welcome.this, DocHome.class);
                                    Common.currentUser = user;
                                    startActivity(intent);
                                    Toast.makeText(welcome.this, "Successfull", Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                Toast.makeText(welcome.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            mDialog.dismiss();
                            Toast.makeText(welcome.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
            else
            {
                Toast.makeText(welcome.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    @Override
    public void onBackPressed() {
        new FancyAlertDialog.Builder(this)
                .setTitle("Alert")
                .setBackgroundColor(Color.parseColor("#0c4fa6"))  //Don't pass R.color.colorvalue
                .setMessage("Do you really want to Exit ?")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#0c4fa6"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Yes")
                .setNegativeBtnBackground(Color.parseColor("#0c4fa6"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_error_outline_black_24dp, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        finish();

                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();

    }

}
