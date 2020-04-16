package com.shuiyes.appstore.adpter;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shuiyes.appstore.BR;
import com.shuiyes.appstore.R;
import com.shuiyes.appstore.model.BindingAdapterItem;
import com.shuiyes.appstore.model.SoftAppModel;

import java.util.ArrayList;
import java.util.List;

public class DataBindingAdapter extends RecyclerView.Adapter<DataBindingAdapter.BindingHolder> {

    private static final String TAG = "DataBindingAdapter";

    protected List<BindingAdapterItem> items = new ArrayList<>();

    public List<BindingAdapterItem> getItems() {
        return items;
    }

    public BindingAdapterItem getItem(int index) {
        if (index < items.size()) return items.get(index);
        return null;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
        }else{
            SoftAppModel app = (SoftAppModel)items.get(position);
            Button btn = holder.itemView.findViewById(R.id.btn_app);
            btn.setText(app.getState());
            btn.setTag(app.getDownPercent());
        }
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        holder.bindData(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view, position);
                }
            }
        });

        Button btn = holder.itemView.findViewById(R.id.btn_app);
        btn.setTag(((SoftAppModel)items.get(position)).getDownPercent());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onBtnClick(view, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemClickListener != null) {
                    return onItemClickListener.onLongClick(view, position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    protected OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        void onClick(View view, int position);
        void onBtnClick(View view, int position);
        boolean onLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class BindingHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        public BindingHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(BindingAdapterItem item) {
            binding.setVariable(BR.item, item);
        }

    }

    @BindingAdapter({"listItems"})
    public static void setListItems(RecyclerView recyclerView, List<BindingAdapterItem> items) {
        DataBindingAdapter adapter = (DataBindingAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            if (items == null) {
                Log.d(TAG, "setListItems: null of " + recyclerView);
                return;
            }

//        Log.d(TAG, "setListItems(" + items.size() + ") update " + recyclerView);
            adapter.items = items;
            adapter.notifyDataSetChanged();
        }
    }

}