package com.shuiyes.appstore.ui.act;

import android.os.Bundle;
import android.view.View;

import com.shuiyes.appstore.R;
import com.shuiyes.appstore.ui.fragment.BaseFragment;
import com.shuiyes.appstore.ui.fragment.GameFragment;
import com.shuiyes.appstore.ui.fragment.MineFragment;
import com.shuiyes.appstore.ui.fragment.SoftFragment;
import com.shuiyes.appstore.ui.fragment.TopFragment;

public class MainActivity extends BaseActivity {

    private BaseFragment softFragment, gameFragment, topFragment, mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soft(null);
    }

    public void soft(View view) {
        if(softFragment == null){
            softFragment = SoftFragment.newInstance();
        }
        showFragment(R.id.content, softFragment, softFragment.getClass().getSimpleName());
    }

    public void game(View view) {
        if(gameFragment == null){
            gameFragment = GameFragment.newInstance();
        }
        showFragment(R.id.content, gameFragment, gameFragment.getClass().getSimpleName());
    }

    public void top(View view) {
        if(topFragment == null){
            topFragment = TopFragment.newInstance();
        }
        showFragment(R.id.content, topFragment, topFragment.getClass().getSimpleName());
    }

    public void mine(View view) {
        if(mineFragment == null){
            mineFragment = MineFragment.newInstance();
        }
        showFragment(R.id.content, mineFragment, mineFragment.getClass().getSimpleName());
    }

}
