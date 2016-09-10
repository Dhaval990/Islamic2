package com.islamicbookslibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.islamicbookslibrary.BaseActivity;
import com.islamicbookslibrary.R;

public class SplaceActivity extends BaseActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace);
    }

    @Override
    public void initVariable() {

    }

    @Override
    public void findViews() {

    }

    @Override
    public void postInitView() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void addAdapter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    if (mAuth.getCurrentUser().getProviders().get(0).toString().toLowerCase().contains("google.com")) {
                        Toast.makeText(SplaceActivity.this, "Normal User", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SplaceActivity.this, "Admin User", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplaceActivity.this, AdminMainActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(SplaceActivity.this, UserLoginActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}
