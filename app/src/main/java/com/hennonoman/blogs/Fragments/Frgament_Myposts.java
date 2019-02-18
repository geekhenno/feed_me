package com.hennonoman.blogs.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hennonoman.blogs.Adabter.BlogRecyclerAdapter;
import com.hennonoman.blogs.AddBlogActivity;
import com.hennonoman.blogs.HomeActivity;
import com.hennonoman.blogs.R;
import com.hennonoman.blogs.model.Blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Frgament_Myposts extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    RecyclerView recyclerView;
    ProgressBar load_progress;



    private BlogRecyclerAdapter blogRecyclerAdapter;
    private List<Blog> blogList;

    public Frgament_Myposts()
    {


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.myposts_fragment, container, false);

        blogList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MBlog");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();



        mDatabaseReference.keepSynced(true);

        recyclerView =  view.findViewById(R.id.recyclerView_mypost);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        load_progress = view.findViewById(R.id.load_progress_mypost);
        load_progress.setVisibility(View.VISIBLE);





        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

                Blog blog = dataSnapshot.getValue(Blog.class);

                if(blog.getUserid().equals(mUser.getUid()))
                {
                    blogList.add(blog);
                    Collections.reverse(blogList);
                    blogRecyclerAdapter = new BlogRecyclerAdapter(getContext(),blogList);
                    recyclerView.setAdapter(blogRecyclerAdapter);
                    blogRecyclerAdapter.notifyDataSetChanged();
                }




                load_progress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {




            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                load_progress.setVisibility(View.INVISIBLE);
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     //   inflater.inflate(R.menu.menu_calls_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
