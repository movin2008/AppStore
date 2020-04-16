/**
 * Copyright (C) 2018-2021 BDStar AEG all rights reserved.
 */
package com.shuiyes.appstore.ui.act;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.shuiyes.appstore.widget.Tips;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends android.support.v7.app.AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    private String[] mPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<String>();
            for (int i = 0; i < mPermissions.length; i++) {
                String permission = mPermissions[i];
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(permission);
                }
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 100) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = shouldShowRequestPermissionRationale(permissions[i]);
                    String permission = permissions[i];
                    if (showRequestPermission) {
                        Tips.show(getApplicationContext(), permissions);
                        finish();
                        break;
                    } else {
                        requestPermissions(new String[]{permission}, 100);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    finish();
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.w(TAG, "onConfigurationChanged: newConfig = " + newConfig);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
    }

    protected Fragment prevFragment, curFragment;

    public synchronized void showFragment(int layoutId, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment tagFragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == curFragment || (tagFragment != null && tagFragment == prevFragment))
            return;

        Log.e(TAG, "showFragment: " + tag);
        curFragment = fragment;
        if (tagFragment == null) {
            transaction.add(layoutId, fragment, tag);
            if (prevFragment != null) {
                transaction.hide(prevFragment);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            if (prevFragment != null) {
                transaction.hide(prevFragment);
            }
            transaction.show(fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        prevFragment = fragment;
    }

}
