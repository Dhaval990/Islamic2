package com.islamicbookslibrary;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.islamicbookslibrary.interfaces.ActImpMethods;
import com.islamicbookslibrary.util.Logg;
import com.islamicbookslibrary.util.Util;

/**
 * Created by Dhaval-Joshi on 12/6/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements ActImpMethods {

    public static final String TAG = BaseActivity.class.getSimpleName();
    protected Toolbar toolbar;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void setContentView(int layoutResID) {
        try {
            super.setContentView(layoutResID);
            initVariable();
            findViews();
            postInitView();
            loadData();
            addAdapter();
        } catch (Exception e) {
            e.printStackTrace();
            Util.showToast(BaseActivity.this, e.toString());
            Logg.e(TAG, e.toString());
        }
    }

    protected View links(int resId) {
        return findViewById(resId);
    }

    protected void setToolbar(int id) {
        setToolbar(id, true);
    }

    protected void setToolbarcolor(int toolbarcolor) {
        toolbar.setBackgroundColor(toolbarcolor);
    }

    protected void setToolbar(int id, boolean isBackEnabled) {
        toolbar = (Toolbar) links(id);
        super.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (isBackEnabled) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public void setBackIcon(Drawable backIcon) {
        toolbar.setNavigationIcon(backIcon);

    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    /**
     * @param layout
     * @param fragment
     * @param isBaskStack
     * @param TAG
     */
    public void changeFragment(int layout, Fragment fragment, boolean isBaskStack, String TAG) {
        //hide keyboard when fragment change
        Util.hideKeyboard(this);

        if (!isBaskStack) {
            getSupportFragmentManager().beginTransaction().replace(layout, fragment, TAG).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(layout, fragment, TAG).addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //hide keyboard when activity change
        Util.hideKeyboard(BaseActivity.this);
        //apply animations

    }

    /**
     * @param tag
     * @return
     */
    public Fragment findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
