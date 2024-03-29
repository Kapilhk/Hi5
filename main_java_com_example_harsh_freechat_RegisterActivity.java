package com.example.harsh.freechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayname;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;
    private Toolbar mToolbar;
    private DatabaseReference mDatabase;
    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar=findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress=new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mDisplayname=findViewById(R.id.textInputLayout5);
        mEmail=findViewById(R.id.textInputLayout6);
        mPassword=findViewById(R.id.textInputLayout4);
        mCreateBtn=findViewById(R.id.reg_create_btn);
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name=mDisplayname.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password))
                {
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name,email,password);
                }



            }
        });
    }

    private void register_user(final String display_name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();
                            mDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            String device_token = FirebaseInstanceId.getInstance().getToken();
                            HashMap<String,String> usermap=new HashMap<>();
                            usermap.put("name",display_name);
                            usermap.put("status","Hi there I'm using Free Chat App.");
                            usermap.put("image","default");
                            usermap.put("thumb_image", "default");
                            usermap.put("device_token", device_token);
                            mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        mRegProgress.dismiss();
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            mRegProgress.hide();
                            Toast.makeText(RegisterActivity.this, "Failed to Sign in.Please try again",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }
}
