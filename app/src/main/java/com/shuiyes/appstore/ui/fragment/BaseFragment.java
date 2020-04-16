/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.shuiyes.appstore.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuiyes.appstore.ui.viewmodel.BaseViewModel;

/**
 * Base {@link Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment extends Fragment {

    public String TAG = this.getClass().getSimpleName();

    public BaseViewModel viewModel;
    public abstract void initViewModel();

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(viewModel == null){
            initViewModel();
        }
        if(viewModel != null){
            viewModel.start();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(viewModel != null){
            viewModel.destroy();
        }
    }

    protected Fragment prevFragment, curFragment;
    public synchronized void showFragment(int layoutId, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment tagFragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == curFragment || (tagFragment != null && tagFragment == prevFragment))
            return;

        Log.i(TAG, "showFragment: " + tag);
        curFragment = fragment;
        if (tagFragment == null) {
            transaction.add(layoutId, fragment, tag);
            if (prevFragment != null) {
                transaction.hide(prevFragment);
            }
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } else {
            if (prevFragment != null) {
                transaction.hide(prevFragment);
            }
            transaction.show(fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
        prevFragment = fragment;
    }
}
