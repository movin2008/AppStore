package com.shuiyes.appstore.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuiyes.appstore.databinding.FragmentTopBinding;

public class TopFragment extends BaseFragment {

    public static TopFragment newInstance() {
        Bundle args = new Bundle();
        TopFragment fragment = new TopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViewModel() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return FragmentTopBinding.inflate(inflater, container, false).getRoot();
    }

}
