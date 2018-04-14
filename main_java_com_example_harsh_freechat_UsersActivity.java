package com.example.harsh.freechat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar=findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersList=findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users users, int position) {

                UsersViewHolder.setName(users.getName());
                UsersViewHolder.setUserStatus(users.getStatus());
                UsersViewHolder.setUserImage(users.getThumb_image(),getApplicationContext());
                final String user_id= getRef(position).getKey();

                UsersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profile_Intent=new Intent(UsersActivity.this,ProfileActivity.class);
                        profile_Intent.putExtra("user_id", user_id);
                        startActivity(profile_Intent);
                    }
                });
            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

         static View  mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public static void setName(String name){
            TextView userNameView=mView.findViewById(R.id.users_single_name);
            userNameView.setText(name);
        }
        public static void setUserStatus(String status){
            TextView userStatusView=mView.findViewById(R.id.users_single_status);
            userStatusView.setText(status);
        }

        public static void setUserImage(String thumb_image, Context ctx)
        {
            CircleImageView userImageView=mView.findViewById(R.id.users_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.avatar).into(userImageView);
        }

    }
}
