package com.hennonoman.blogs.Adabter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hennonoman.blogs.R;
import com.hennonoman.blogs.model.Blog;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by paulodichone on 4/17/17.
 */

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Blog> blogList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencePost;
    private DatabaseReference mDatabaseReferenceLikes;

    Blog blog;
    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_list_item, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FirebaseAuth mAuth =FirebaseAuth.getInstance();
        final FirebaseUser mUser = mAuth.getCurrentUser();

        blog = blogList.get(position);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("users").child(mUser.getUid());
        mDatabaseReferencePost = mDatabase.getReference().child("MBlog");
        mDatabaseReferenceLikes = mDatabase.getReference().child("MBlog").child(blogList.get(position).getPostID());


        mDatabaseReferenceLikes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                String counterLike = dataSnapshot.getValue().toString();
                blogList.get(position).setLiksCounter(counterLike);
                holder.blog_like_count.setText(counterLike);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


               if( blogList.get(position).getPostID().equals(dataSnapshot.getKey()))
               {
                   holder.blog_like_btn.setImageResource(R.mipmap.action_like_red);
               }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

              }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                holder.blog_like_btn.setImageResource(R.mipmap.action_like_gray);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        String imageUrl = null;



        holder.blog_user_name.setText(blog.getNickname());
        holder.desc.setText(blog.getDesc());





        holder.blog_like_count.setText(blog.getLiksCounter());

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());









        holder.blog_date.setText(formattedDate);

        imageUrl = blog.getImage();


        //TODO: Use Picasso library to load image


        String p = "gs://blogs-c5fcb.appspot.com/images/";
        StorageReference storageReference =  FirebaseStorage.getInstance().getReferenceFromUrl(p+imageUrl);

        StorageReference storageReference2 =  FirebaseStorage.getInstance().getReferenceFromUrl(p+blog.getProfileImg());

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)    // you can pass url too
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {


                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        // you can do something with loaded bitmap here

                        holder.blog_image.setImageBitmap(resource);
                    }
                });


        Glide.with(context)
                .load(blog.getProfileImg())
                .centerCrop()
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.blog_user_image);




        holder.blog_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mDatabaseReference.child(blogList.get(position).getPostID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int counter = Integer.parseInt(blogList.get(position).getLiksCounter());

                        if(dataSnapshot.exists())
                        {


                            dataSnapshot.getRef().removeValue();
                            counter--;
                            holder.blog_like_btn.setImageResource(R.mipmap.action_like_gray);
                        }
                        else
                        {

                            dataSnapshot.getRef().setValue("liked");
                            counter++;
                            holder.blog_like_btn.setImageResource(R.mipmap.action_like_red);
                        }



                        mDatabaseReferencePost.child(blogList.get(position).getPostID()).child("liksCounter").setValue(counter+"");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView blog_user_name;
        public TextView desc;
        public TextView blog_date;
        public TextView blog_like_count;
        public ImageView blog_image ;
        CircleImageView blog_user_image;
        public ImageView blog_like_btn;
        String userid;

        public ViewHolder(View view, Context ctx)
        {
            super(view);

            context = ctx;

            blog_user_name = view.findViewById(R.id.blog_user_name);
            desc =  view.findViewById(R.id.blog_desc);
            blog_image =  view.findViewById(R.id.blog_image);
            blog_date =  view.findViewById(R.id.blog_date);
            blog_user_image =  view.findViewById(R.id.blog_user_image);
            blog_like_btn = view.findViewById(R.id.blog_like_btn);
            blog_like_count = view.findViewById(R.id.blog_like_count);







        }
    }
}
