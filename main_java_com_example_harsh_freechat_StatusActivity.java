package com.example.harsh.freechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mStatus;
    private Button mSaveBtn;
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=mCurrentUser.getUid();
        mStatusDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mToolbar=findViewById(R.id.status_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value=getIntent().getStringExtra("status value");
        mStatus=findViewById(R.id.status_input);
        mSaveBtn=findViewById(R.id.update_stat_btn);
        mStatus.getEditText().setText(status_value);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress=new ProgressDialog(StatusActivity.this);
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please wait while we save the changes");
                mProgress.show();
                String status=mStatus.getEditText().getText().toString();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            mProgress.dismiss();
                            Intent SettingActivity=new Intent(StatusActivity.this,SettingsActivity.class);
                            startActivity(SettingActivity);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"There was some error in saving changes !", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }
}
