package com.hennonoman.blogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import  android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hennonoman.blogs.Fragments.BlogFragment;
import com.hennonoman.blogs.Fragments.Frgament_Myposts;

public class HomeActivity extends AppCompatActivity {



    private final Fragment[] PAGES = new Fragment[] {
            new BlogFragment(),
            new Frgament_Myposts()

    };
    private final String[] PAGE_TITLES = new String[] {
            "All Posts",
            "My Posts",

    };




    FragmentManager fragmentManager;

    private ViewPager mViewPager;
    private   TabLayout tabLayout;

    FloatingActionButton add ,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

         fragmentManager = getSupportFragmentManager();
         mViewPager =  findViewById(R.id.viewpager);

        add = findViewById(R.id.add);
        logout  = findViewById(R.id.logout);

        mViewPager.setAdapter(new MyPagerAdapter(fragmentManager));


         tabLayout =  findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.feed_me);
        tabLayout.getTabAt(1).setIcon(R.drawable.feed_me);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this,AddBlogActivity.class));
                finish();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(HomeActivity.this, "log", Toast.LENGTH_SHORT).show();
                signoutDialog();
            }
        });



    }



    void signoutDialog()
    {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("FEED ME");
        dialog.setIcon(R.drawable.feed_me);
        dialog.setMessage("Are you sure you want to sign out ?");

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        dialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                sign_out();


            }
        });


        new Dialog(getApplicationContext());
        dialog.show();
    }


    private void sign_out ()
    {


        GoogleSignInClient mGoogleSignInClient ;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener( HomeActivity.this,
                new OnCompleteListener<Void>() {  //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut(); //signout firebase
                        Intent setupIntent = new Intent(getApplicationContext(), MainActivity.class);
                        //  Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                     //   setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                });



    }

    public class MyPagerAdapter extends FragmentPagerAdapter {



        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }
    }
}


