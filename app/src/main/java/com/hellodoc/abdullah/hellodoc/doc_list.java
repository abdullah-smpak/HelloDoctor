package com.hellodoc.abdullah.hellodoc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hellodoc.abdullah.hellodoc.Common.Common;
import com.hellodoc.abdullah.hellodoc.Interface.ItemClickListener;
import com.hellodoc.abdullah.hellodoc.Model.Doctor;
import com.hellodoc.abdullah.hellodoc.Viewholder.DocCatviewHolder;
import com.hellodoc.abdullah.hellodoc.Viewholder.DocListViewHolder;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class doc_list extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference docList;

    String categoryId="";
    FirebaseRecyclerAdapter<Doctor,DocListViewHolder> adapter;

    // search  Functionality
    FirebaseRecyclerAdapter<Doctor,DocListViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);


        database= FirebaseDatabase.getInstance();
        docList= database.getReference("Doctor");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_doc);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//Get Intent Here
        if (getIntent() != null)
            categoryId= getIntent().getStringExtra("doctor_Id");
        if (!categoryId.isEmpty() && categoryId != null)
        {
            if(Common.isConnected(getBaseContext())) {
                loadListdoc(categoryId);
            }
            else
            {
                Toast.makeText(this, "Please Check You Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        materialSearchBar = (MaterialSearchBar)findViewById(R.id.sbar);
        materialSearchBar.setHint("Enter Doctor's Name");
        
        loadSuggest(); // write function to load from firebase
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);

        materialSearchBar.addTextChangeListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener(){

            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchAdapter= new FirebaseRecyclerAdapter<Doctor, DocListViewHolder>(
                Doctor.class,
                R.layout.doc_item,
                DocListViewHolder.class,
                docList.orderByChild("name").equalTo(text.toString())) {


            @Override
            protected void populateViewHolder(DocListViewHolder viewHolder, Doctor model, int position) {
                viewHolder.Doc_name.setText(model.getName());
                Picasso.get().load(model.getImage())
                        .into(viewHolder.Doc_pic);

                final Doctor local = model;
                viewHolder.setItemClicklistener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongclick) {

                        //start new Activity
                        Intent docDetail = new Intent(doc_list.this,DocDetail.class);
                        docDetail.putExtra("doctor_Id",searchAdapter.getRef(position).getKey());
                        startActivity(docDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        docList.orderByChild("doctor_Id").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            Doctor item = postSnapshot.getValue(Doctor.class);
                            suggestList.add(item.getName()); // Add name of food to suggest list

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadListdoc(String categoryId) {
        try{
        adapter = new FirebaseRecyclerAdapter<Doctor, DocListViewHolder>(Doctor.class,
                R.layout.doc_item,
                DocListViewHolder.class,
                docList.orderByChild("doctor_Id").equalTo(categoryId)  // like : select * from Foods where MenuId =

        ) {
            @Override
            protected void populateViewHolder(DocListViewHolder viewHolder, Doctor model, int position) {

                viewHolder.Doc_name.setText(model.getName());
                Picasso.get().load(model.getImage())
                        .into(viewHolder.Doc_pic);

                final Doctor local = model;
                viewHolder.setItemClicklistener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongclick) {

                        //start new Activity
                        Intent docDetail = new Intent(doc_list.this,DocDetail.class);
                        docDetail.putExtra("doctor_Id",adapter.getRef(position).getKey());
                        startActivity(docDetail);
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
        }catch (Exception ex)
        {

        }
    }
}
