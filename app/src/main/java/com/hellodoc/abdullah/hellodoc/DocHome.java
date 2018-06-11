package com.hellodoc.abdullah.hellodoc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hellodoc.abdullah.hellodoc.Common.Common;
import com.hellodoc.abdullah.hellodoc.Interface.ItemClickListener;
import com.hellodoc.abdullah.hellodoc.Model.Appoint;
import com.hellodoc.abdullah.hellodoc.Model.Category;
import com.hellodoc.abdullah.hellodoc.Model.Doctor;
import com.hellodoc.abdullah.hellodoc.Service.ListenApp;
import com.hellodoc.abdullah.hellodoc.Viewholder.DocCatviewHolder;
import com.hellodoc.abdullah.hellodoc.Viewholder.DocListViewHolder;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class DocHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category,doc;
    TextView txtFullName;
    RecyclerView recyler_menu;
    Doctor current;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,DocCatviewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Doctor's Category");

        setSupportActionBar(toolbar);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        doc= database.getReference("Doctor");

        Paper.init(this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set Name for user
        View headView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());



        //Load menu
        recyler_menu =  (RecyclerView)findViewById(R.id.recycler_menu);
        recyler_menu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyler_menu.setLayoutManager(layoutManager);

        if(Common.isConnected(getBaseContext())) {
            LoadMenu();
        }
        else
        {
            Toast.makeText(this, "Please Check your Connection", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(DocHome.this, ListenApp.class);
        startService(intent);
        finish();

    }

    private void LoadMenu() {
        try {
            adapter = new FirebaseRecyclerAdapter<Category, DocCatviewHolder>(Category.class, R.layout.doc_cat_list, DocCatviewHolder.class, category) {
                @Override
                protected void populateViewHolder(DocCatviewHolder viewHolder, Category model, int position) {
                    viewHolder.txtDocName.setText(model.getName());
                    Picasso.get().load(model.getImage())
                            .into(viewHolder.imageView);

                    final Category clickItem = model;
                    viewHolder.setItemClicklistener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongclick) {
                            //Get category Id and send to new Activity
                            Intent docList = new Intent(DocHome.this, doc_list.class);

                            //  Because Category is key , so we just get key of this item
                            docList.putExtra("doctor_Id", adapter.getRef(position).getKey());

                            startActivity(docList);
                        }
                    });

                }
            };
            recyler_menu.setAdapter(adapter);
        }catch (Exception ex)
        {

        }
    }

    @Override
    public void onBackPressed() {



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
         //   super.onBackPressed();

            new FancyAlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setBackgroundColor(Color.parseColor("#0c4fa6"))  //Don't pass R.color.colorvalue
                    .setMessage("Do you really want to Logout ?")
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

                            Intent sigIn = new Intent(DocHome.this,welcome.class);

                            sigIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(sigIn);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doc_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

if(item.getItemId() ==R.id.refresh)
{

    LoadMenu();
}
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.app_status) {

           ItemClickListener itemClickListener = new ItemClickListener() {
               @Override
               public void onClick(View view, int position, boolean islongclick) {
                   Intent docDetail = new Intent(DocHome.this,Appoint_Status.class);
                   startActivity(docDetail);
               }
           };

           Intent intent  =  new Intent(DocHome.this,Appoint_Status.class);
           startActivity(intent);

        } else if (id == R.id.logout) {

           new FancyAlertDialog.Builder(this)
                   .setTitle("Alert")
                   .setBackgroundColor(Color.parseColor("#0c4fa6"))  //Don't pass R.color.colorvalue
                   .setMessage("Do you really want to Logout ?")
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

                           Intent sigIn = new Intent(DocHome.this,welcome.class);

                           sigIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(sigIn);
                           Paper.book().destroy();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
