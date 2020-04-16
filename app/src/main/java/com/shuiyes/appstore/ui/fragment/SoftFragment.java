package com.shuiyes.appstore.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuiyes.appstore.R;
import com.shuiyes.appstore.adpter.DataBindingAdapter;
import com.shuiyes.appstore.databinding.FragmentSoftBinding;
import com.shuiyes.appstore.model.SoftAppModel;
import com.shuiyes.appstore.ui.viewmodel.SoftViewModel;

public class SoftFragment extends BaseFragment {

    public static SoftFragment newInstance() {
        Bundle args = new Bundle();
        SoftFragment fragment = new SoftFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private DataBindingAdapter mAdapter;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int position = msg.arg1;
            ((SoftAppModel) mAdapter.getItem(position)).setState(msg.what, (String) msg.obj);
            mAdapter.notifyItemChanged(position, "payload");
        }
    };

    @Override
    public void initViewModel() {
        viewModel = new SoftViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentSoftBinding binding = FragmentSoftBinding.inflate(inflater, container, false);
        binding.rvCallog.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvCallog.setAdapter(mAdapter = new DataBindingAdapter());
        mAdapter.setOnItemClickListener(new DataBindingAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.e(TAG, "onClick " + ((SoftAppModel) mAdapter.getItem(position)).getTitle());
            }

            @Override
            public void onBtnClick(View view, int position) {
                ((SoftViewModel) viewModel).downloadAppIfNeed(mHandler, position);
            }

            @Override
            public boolean onLongClick(View view, int position) {
                return false;
            }
        });

        android.support.v7.widget.DividerItemDecoration dividerItemDecoration = new android.support.v7.widget.DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(android.support.v4.content.ContextCompat.getDrawable(getActivity(), R.drawable.item_line_divider));
        binding.rvCallog.addItemDecoration(dividerItemDecoration);

        binding.setViewModel((SoftViewModel) viewModel);

        return binding.getRoot();
    }

}