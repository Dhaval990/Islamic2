package com.islamicbookslibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islamicbookslibrary.interfaces.ActImpMethods;
import com.islamicbookslibrary.util.Logg;
import com.islamicbookslibrary.util.Util;


public abstract class BaseFragment extends Fragment implements ActImpMethods {

    protected Context mContext;
    private boolean isReloadFromBackStack = false, isAlreadyLoaded = false, hasOptionMenu = false;
    private View view;
    private int res;
    private ProgressDialog mProgressDialog;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * @param res
     */
    public void setContentView(int res) {
        this.res = res;
    }

    /**
     * @param res
     * @param isReloadFromBackStack
     */
    protected void setContentView(int res, boolean isReloadFromBackStack) {
        this.res = res;
        this.isReloadFromBackStack = isReloadFromBackStack;
        this.hasOptionMenu = false;
    }

    /**
     * ]
     *
     * @param res
     * @param isReloadFromBackStack
     * @param hasOptionMenu
     */
    public void setContentView(int res, boolean isReloadFromBackStack, boolean hasOptionMenu) {
        this.res = res;
        this.isReloadFromBackStack = isReloadFromBackStack;
        this.hasOptionMenu = hasOptionMenu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (isReloadFromBackStack) {
            if (view == null) {
                isAlreadyLoaded = false;
                view = inflater.inflate(res, container, false);
            } else {
                isAlreadyLoaded = true;
            }
        } else {
            isAlreadyLoaded = false;
            view = inflater.inflate(res, container, false);
        }
        setHasOptionsMenu(hasOptionMenu);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        try {
            super.onActivityCreated(savedInstanceState);
            initVariable();
            findViews();
            postInitView();
            loadData();
            addAdapter();
        } catch (Exception e) {
            e.printStackTrace();
            Util.showToast(mContext, e.toString());
            Logg.e(BaseFragment.class.getClass().getSimpleName(), e.toString());
        }
    }

    protected View links(int res) {
        return view.findViewById(res);
    }

    protected void setToolbarTitle(String title) {
        ((BaseActivity) mContext).setTitle(title);
    }

    protected void setToolbarcolor(int color) {
        ((BaseActivity) mContext).setToolbarcolor(color);
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
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
